package init.mat.mlproj.kmeans.io;

import com.alibaba.fastjson.JSONObject;
import init.mat.mlproj.kmeans.bean.Cluster;
import init.mat.mlproj.kmeans.bean.ClusterList;
import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;
import init.mat.mlproj.kmeans.util.CommonUtils;
import init.mat.mlproj.kmeans.util.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class GeoIO {

    public JSONObject geoFileReader(String path){
        JSONObject result = null;

        result = JSONObject.parseObject(IOBase.geoFileReader(path));

        return result;
    }

    public void geoFileWriter(String path, JSONObject resOut) {


        byte[] bytes = resOut.toString().getBytes();

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

    public List<JSONObject> getCoordinateList(String path) {
        JSONObject json = geoFileReader(path);
        List<JSONObject> res = getCoordinateList(json);

        return res;
    }


    public abstract List<JSONObject> getCoordinateList(JSONObject json);


    public abstract DataList geoCoordinateToDataList(List<JSONObject> listJson);


    /**
     *
     * @param clusterList 训练完成后的簇集合
     * @param listJson 读入的原始文件核心信息集合
     * @param path 输出结果集合文件的目录（不输出则为空）
     * @return
     */
    public List<JSONObject> createGeoByClusterList(ClusterList clusterList, List<JSONObject> listJson, String path) {
        List<JSONObject> res = new ArrayList<JSONObject>();

        Iterator clusterIterator = clusterList.iterator();
        while(clusterIterator.hasNext()){
            Cluster cluster = (Cluster) clusterIterator.next();

            // 簇中无数据不分组
            if(cluster.getDatas().size() == 0) continue;

            // 按照geojson格式输出整理蔟中数据集
            List<JSONObject> listArray = assembleData(cluster, listJson);

            JSONObject resOut = new JSONObject();

            JSONObject coordinateProp = new JSONObject();
            coordinateProp.put("features", listArray);
            coordinateProp.put("type", "FeatureCollection");

            JSONObject externalRectangle = CommonUtils.extractShapeExternalRectangle(listArray);

            resOut.put(Constant.CLUSTER_ID, cluster.getClusterID());
            resOut.put(Constant.EXTERNAL_RECTANGLE, externalRectangle);
            resOut.put(Constant.GEO_JSON, coordinateProp);

            if(path != null && !"".equals(path)){
                String fileName = cluster.getClusterID() + ".geo.json";

                String outPath = path + System.getProperty("file.separator") + fileName;
                coordinateProp.put(Constant.EXTERNAL_RECTANGLE, externalRectangle);
                coordinateProp.put(Constant.CLUSTER_ID, cluster.getClusterID());
                geoFileWriter(outPath, coordinateProp);
                coordinateProp.remove(Constant.EXTERNAL_RECTANGLE);
                coordinateProp.remove(Constant.CLUSTER_ID);
                CommonUtils.wirteExternalRectangleToGeoJsonFile(path, cluster.getClusterID(), externalRectangle);
            }

            res.add(resOut);

        }

        return res;
    }


    /**
     * 标准聚类文件 字段3维 id, lat, lng
     *
     * @param input
     * @return
     * @throws IOException
     */
    public DataList getDataByStandardFile (String input) throws IOException {
        DataList res = new DataList();

        BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( new File(input)),"utf-8"));
        String s = null;
        int i = 0;
        while ((s=reader.readLine())!=null) {
            //向量内容
            String[] vectorString = s.split("\\s+");
            Data data = new Data(Long.parseLong(vectorString[0]), Double.parseDouble(vectorString[1]), Double.parseDouble(vectorString[2]));
            res.add(data);
            i++;
        }
        reader.close();

        return res;

    }


    /**
     * 按照原始数据下标整合数据
     *
     * @param cluster 聚类簇
     * @param listJson 原始数据
     * @return
     */
    public abstract List<JSONObject> assembleData(Cluster cluster, List<JSONObject> listJson);

}
