package init.mat.mlproj.kmeans.compute;


import init.mat.mlproj.kmeans.bean.Cluster;
import init.mat.mlproj.kmeans.bean.ClusterList;
import init.mat.mlproj.kmeans.bean.Data;

public abstract class CoordinateDistance {

    /** 点到簇的距离 **/
    public double calcDistance(Data data, Cluster cluster) {
        return getDistance(data.getCoordinate().getX(), data.getCoordinate().getY(),
                cluster.getCoordinate().getX(), cluster.getCoordinate().getY());
    }

    /** 点到点的距离 **/
    public double calcDistance(Data data1, Data data2) {
        return getDistance(data1.getCoordinate().getX(), data1.getCoordinate().getY(),
                data2.getCoordinate().getX(), data2.getCoordinate().getY());
    }


    /**
     * 计算某一数据点data距离所有簇质心的最小值
     * @param data,数据点
     * @param clusterList,簇集合
     * @return distance,最小距离
     */
    public double calcDistance(Data data, ClusterList clusterList) {
        double distance = Double.MAX_VALUE;
        for (Cluster cluster : clusterList) {
            distance = Math.min(distance, calcDistance(data, cluster));
        }
        return distance;
    }


    /**
     * 通过经纬度获取距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离
     */
    public abstract double getDistance(double lat1, double lng1, double lat2, double lng2);

}
