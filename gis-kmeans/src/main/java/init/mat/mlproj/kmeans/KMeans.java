package init.mat.mlproj.kmeans;

import com.alibaba.fastjson.JSONObject;
import init.mat.mlproj.kmeans.bean.ClusterList;
import init.mat.mlproj.kmeans.bean.DataList;
import init.mat.mlproj.kmeans.compute.ClusterCenterComputingFactory;
import init.mat.mlproj.kmeans.compute.CoordinateDistanceFactory;
import init.mat.mlproj.kmeans.compute.impl.ClusterCenterConstant;
import init.mat.mlproj.kmeans.compute.impl.DistanceConstant;
import init.mat.mlproj.kmeans.convert.ConvertEngine;
import init.mat.mlproj.kmeans.convert.ConvertEngineConstant;
import init.mat.mlproj.kmeans.convert.ConvertFactory;
import init.mat.mlproj.kmeans.core.KMeansCluster;
import init.mat.mlproj.kmeans.exception.InvalidGeoJsonException;
import init.mat.mlproj.kmeans.io.GeoIO;
import init.mat.mlproj.kmeans.io.GeoIOFactory;
import init.mat.mlproj.kmeans.io.impl.GeoIOConstant;

import java.util.List;

public class KMeans {

    int iter = 1000; //设置总迭代次数
    int k = 8;  //确定簇的数目

    Object[] o;

    public KMeans(){

    }

    /**
     * @param iter 最多训练多少轮
     * @param k  分为多少个组
     */
    public KMeans(int iter, int k){
        this.iter = iter;
        this.k = k;
    }


    /**
     *  k-means 运行方法入口
     *
     * @param jsonObject geo JSON信息
     * @param type 传入geo信息的类型
     * @return
     */
    public List<JSONObject> run(JSONObject jsonObject, GeoIOConstant type) {
        List<JSONObject> res = null;

        if(jsonObject.containsKey("type") && "Topology".equals(jsonObject.getString("type"))){
            try {
                jsonObject = convertTopo2Geojson(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GeoIO geoIO = GeoIOFactory.get(type);

        // 对原格式数据进行，训练预处理
        List<JSONObject> listJson = geoIO.getCoordinateList(jsonObject);
        DataList dataList = geoIO.geoCoordinateToDataList(listJson);

        KMeansCluster clusterer = new KMeansCluster(ClusterCenterComputingFactory.get(ClusterCenterConstant.STANDARD), CoordinateDistanceFactory.get(DistanceConstant.STANDARD), iter, k);
        //聚类
        ClusterList clusterList = clusterer.runKMeansClustering(dataList,  null);
        // 结果输出

        if(clusterList.size() == 0){
            throw new InvalidGeoJsonException("源数据有误，分组结果为0");
        }

        res = geoIO.createGeoByClusterList(clusterList, listJson, "");

        return res;
    }


    private JSONObject convertTopo2Geojson(JSONObject geo) throws Exception {
        ConvertEngine convertEngine = ConvertFactory.get(ConvertEngineConstant.GT_CONVERT_ENGINE);
        String res  = "";

        res = convertEngine.convert(ConvertEngineConstant.CONVERT_TYPE_TOPO2GEO.getValue(), geo.toJSONString());

        return (JSONObject) JSONObject.parse(res);
    }
}
