package init.mat.mlproj.kmeans.io.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import init.mat.mlproj.kmeans.bean.Cluster;
import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;
import init.mat.mlproj.kmeans.exception.InvalidGeoJsonException;
import init.mat.mlproj.kmeans.io.GeoIO;
import init.mat.mlproj.kmeans.util.CommonUtils;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeoJsonGeoIO extends GeoIO {

    public List<JSONObject> getCoordinateList(JSONObject json) {
        if(json != null && json.containsKey("features")) {
            JSONArray jsonArray = json.getJSONArray("features");
            List<JSONObject> res = new ArrayList<JSONObject>();
            for (Object obj : jsonArray) {
                if (obj instanceof JSONObject) {
                    res.add((JSONObject) obj);
                }
            }

            return res;
        } else{
            return new ArrayList<JSONObject>();
        }
    }


    @Override
    public DataList geoCoordinateToDataList(List<JSONObject> listJson) {
        if(listJson.size() == 0 || listJson == null){
            throw new InvalidGeoJsonException("传入的数据源features数组，不能为空！");
        }

        DataList dataList = new DataList();

        for (int i=0; i< listJson.size(); i++) {
            JSONObject jo = (JSONObject)listJson.get(i).get("geometry");
            if(jo != null) {
                if ("point".equals(jo.getString("type").toLowerCase())) {
                    JSONArray l = jo.getJSONArray("coordinates");
                    if(l != null) {
                        if (l.size() == 2) {
                            dataList.add(new Data(i, Double.parseDouble(l.get(1).toString()), Double.parseDouble(l.get(0).toString())));
                        }
                    }
                } else {
                    // geojson中存在其他形式的的数据，转化为外接矩形，然后取中心点进行聚合
                    String str = jo.getString("coordinates");
                    if((!"".equals(str)) && str !=null && str.length()>2) {
                        Point2D shapeCenter = CommonUtils.computeShapeCenter(str);
                        dataList.add(new Data(i, shapeCenter));
                    }
                }
            }
        }

        return dataList;
    }


    public List<JSONObject> assembleData(Cluster cluster, List<JSONObject> listJson){
        List<JSONObject> res = new ArrayList<JSONObject>();

        if(cluster != null){
            DataList dataList = cluster.getDatas();
            if(dataList != null){
                Iterator dataIterator =  dataList.iterator();
                while(dataIterator.hasNext()){
                    Data data = (Data) dataIterator.next();
                    res.add(listJson.get((int) data.getId()));
                }

            }
        }

        return res;
    }

}
