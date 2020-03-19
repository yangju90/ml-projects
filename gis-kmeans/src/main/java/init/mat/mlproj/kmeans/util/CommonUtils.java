package init.mat.mlproj.kmeans.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;
import javafx.geometry.Point2D;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static JSONObject extractExternalRectangle(DataList dataList){
        double latMax = -1;
        double latMin = 0;
        double lngMin = 0;
        double lngMax = -1;

        for (Data data : dataList) {
            double x = data.getCoordinate().getX();
            double y = data.getCoordinate().getY();

            if(latMax < latMin){
                latMax = x;
                latMin = x;
                lngMin = y;
                lngMax = y;
            }

            if(latMax < x) latMax = x;
            if(latMin > x) latMin = x;
            if(lngMax < y) lngMax = y;
            if(lngMin > y) lngMin = y;
        }

        JSONObject result = new JSONObject();
        ArrayList top_left = new ArrayList();
        ArrayList bottom_right = new ArrayList();


        // 数组输出 经度在前，纬度在后
        top_left.add(lngMin);
        top_left.add(latMax);

        bottom_right.add(lngMax);
        bottom_right.add(latMin);

        result.put("top_left", top_left);
        result.put("bottom_right", bottom_right);

        return result;
    }

    public static JSONObject extractShapeExternalRectangle(List<JSONObject> listJson){
        DataList dataList = new DataList();
        // 处理geojson情况
        for (JSONObject jsonObject: listJson) {
            JSONObject jo = (JSONObject) jsonObject.get("geometry");
            //简单判断，排除抽稀数据 点为[]的情况
            if(jo.containsKey("coordinates") && jo.getString("coordinates").length() >2) {
                CharFilter.parseCoordinateString(jo.getString("coordinates"), dataList);
            }
        }

        return extractExternalRectangle(dataList);
    }


    public static Point2D computeShapeCenter (String str){
        DataList dataList = new DataList();
        // 处理geojson情况
        CharFilter.parseCoordinateString(str, dataList);

        JSONObject externalRectangle =  extractExternalRectangle(dataList);
        DataList datas = new DataList();
        JSONArray top_left = externalRectangle.getJSONArray("top_left");
        JSONArray bottom_right = externalRectangle.getJSONArray("bottom_right");
        datas.add(new Data(0, (double)top_left.get(1), (double)top_left.get(0)));
        datas.add(new Data(1, (double)bottom_right.get(1), (double)bottom_right.get(0)));

        return updateClusterCenter(datas);
    }


    public static Point2D updateClusterCenter(DataList datas) {

        double lat=0.0, lng=0.0, x=0.0, y=0.0, z=0.0;

        for (Data data : datas) {
            double x1 = data.getCoordinate().getX();
            double y1 = data.getCoordinate().getY();
            x1 = x1*Math.PI/180.0;
            y1 = y1*Math.PI/180.0;

            x += (Math.cos(x1) * Math.cos(y1));
            y += (Math.cos(x1) * Math.sin(y1));
            z += Math.sin(x1);
        }

        x /= datas.size();
        y /= datas.size();
        z /= datas.size();

        lng = Math.atan2(y, x) * 180 / Math.PI;
        double temp = Math.sqrt(x*x + y*y);
        lat = Math.atan2(z, temp) * 180 /Math.PI;

        return new Point2D(lat, lng);
    }

    public static void wirteExternalRectangleToGeoJsonFile(String path, int id, JSONObject externalRectangle){
        JSONObject resOut =  new JSONObject();

        JSONArray top_left = externalRectangle.getJSONArray("top_left");
        JSONArray bottom_right = externalRectangle.getJSONArray("bottom_right");
        double latMax = (double)top_left.get(1);
        double lngMin = (double)top_left.get(0);
        double latMin = (double) bottom_right.get(1);
        double lngMax = (double) bottom_right.get(0);
        double[] d1 = new double[]{lngMax,latMax};
        double[] d2 = new double[]{lngMax,latMin};
        double[] d3 = new double[]{lngMin,latMin};
        double[] d4 = new double[]{lngMin,latMax};

        JSONObject geometry = new JSONObject();
        List linel = new ArrayList();
        linel.add(d1);
        linel.add(d2);
        linel.add(d3);
        linel.add(d4);
        linel.add(d1);
        List coordinate = new ArrayList();
        coordinate.add(linel);
        geometry.put("type", "Polygon");
        geometry.put("coordinates", coordinate);
        JSONObject propCoordinate = new JSONObject();
        propCoordinate.put("geometry",geometry);
        propCoordinate.put("type", "Feature");
        propCoordinate.put("id", "Feature");
        List<JSONObject> props = new ArrayList<>();
        props.add(propCoordinate);
        resOut.put("type","FeatureCollection");
        resOut.put("features", props);

        byte[] bytes = resOut.toString().getBytes();

        path += File.separator + id + "external" + ".geo.json";

        File file = new File(path);
        try(FileOutputStream fo = new  FileOutputStream(file);
            BufferedOutputStream bw  = new BufferedOutputStream(fo);
        ) {
            bw.write(bytes, 0 , bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
