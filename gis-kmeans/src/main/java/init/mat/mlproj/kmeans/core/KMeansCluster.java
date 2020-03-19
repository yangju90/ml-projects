package init.mat.mlproj.kmeans.core;

import init.mat.mlproj.kmeans.bean.Cluster;
import init.mat.mlproj.kmeans.bean.ClusterList;
import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;
import init.mat.mlproj.kmeans.compute.ClusterCenterComputing;
import init.mat.mlproj.kmeans.compute.CoordinateDistance;
import javafx.geometry.Point2D;

import java.util.Random;

public class KMeansCluster {
    private static final Random RANDOM = new Random();
    private ClusterCenterComputing clusterCenterComputing;
    private CoordinateDistance coordinateDistance;
    private int maxIterations;
    private int k;

    /**
     *  clusterCenterComputing 中心点更新方式，
     *  coordinateDistance geo距离计算公式
     *  maxIterations 训练最大轮次
     *  k 聚类为k个簇
     **/
    public KMeansCluster(ClusterCenterComputing clusterCenterComputing, CoordinateDistance coordinateDistance, int maxIterations, int k){
        this.clusterCenterComputing = clusterCenterComputing;
        this.coordinateDistance = coordinateDistance;
        this.maxIterations = maxIterations;
        this.k = k;
    }

    /** 随机选择一个数据点作为质心 */
    private Cluster initClusterWithRandomly(DataList dataList) {
        int rndDataIndex = RANDOM.nextInt(dataList.size()); // 随机取数据编号
        //将该编号对应的数据点进行分配,并将数据标记为已分配
        Cluster initialCluster = new Cluster(1 , dataList.get(rndDataIndex), clusterCenterComputing);
        return initialCluster;
    }

    private Cluster createNextCluster(DataList dataList, ClusterList clusterList) {
        //找离该中心点最远的点
        Data furthestDocument = clusterList.findFurthestData(dataList);
        Cluster nextCluster = new Cluster(clusterList.size()+1, furthestDocument, clusterCenterComputing);
        return nextCluster;
    }

    private ClusterList initClusterList(DataList dataList, Point2D point2D){
        ClusterList clusterList = new ClusterList(coordinateDistance);

        if(point2D == null) {
            // geo计算以经纬度中心为起始点
            //若以中心点为起点，有可能造成有些蔟不被分配点
            // 改进算法，先计算中心点然后找到据中心点最近的点
            Point2D dataListCenterPoint = clusterCenterComputing.updateClusterCenter(dataList);
            double nearestDistance = Double.MAX_VALUE;
            Data nearestData = null;
            for (Data data:dataList) {
                double distance = coordinateDistance.calcDistance(data, new Data(0, dataListCenterPoint));
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestData = data;
                }
            }

            clusterList.add(new Cluster(1, nearestData, clusterCenterComputing));
        }else{
            clusterList.add(new Cluster(1, new Data(1, point2D), clusterCenterComputing));
        }

        //如果簇的数量小于定义的簇的数量,则基于离质点最远的点,创建新的簇
        while (clusterList.size() < k) {
            Cluster cluster = null;

            try {
                cluster = createNextCluster(dataList, clusterList);
            }catch (Exception e){
                break;
            }
            if(cluster != null) {
                clusterList.add(cluster);
            }
        }

        return clusterList;
    }

    private void allocatedDataPoint(DataList dataList, ClusterList clusterList){
        for (Data data : dataList) {
            //寻找离data数据点最近的簇
            Cluster nearestCluster = clusterList.findNearestCluster(data);
            //将数据data添加到该簇中
            nearestCluster.add(data);
        }
    }

    private ClusterList whenKEqual(DataList dataList, int num){
        double latMax = -90.0;
        double latMin = 90.0;
        double lngMin = 180.0;
        double lngMax = -180.0;

        for (Data data : dataList) {
            double x = data.getCoordinate().getX();
            double y = data.getCoordinate().getY();

            if(latMax < x) latMax = x;
            if(latMin > x) latMin = x;
            if(lngMax < y) lngMax = y;
            if(lngMin > y) lngMin = y;
        }

        double latH = latMax - latMin;
        double lngW = lngMax - lngMin;

        ClusterList clusterList = new ClusterList(coordinateDistance);
        if(num == 4) {
            clusterList.add(new Cluster(1, new Data(1, new Point2D(latMin + latH / 4, lngMin + lngW / 4)), clusterCenterComputing));
            clusterList.add(new Cluster(2, new Data(1, new Point2D(latMax - latH / 4, lngMin + lngW / 4)), clusterCenterComputing));
            clusterList.add(new Cluster(3, new Data(1, new Point2D(latMin + latH / 4, lngMax - lngW / 4)), clusterCenterComputing));
            clusterList.add(new Cluster(4, new Data(1, new Point2D(latMax - latH / 4, lngMax - lngW / 4)), clusterCenterComputing));
        }
        if(num == 8){
            clusterList.add(new Cluster(1, new Data(1, new Point2D(latMin + latH / 4, lngMin + lngW / 8)), clusterCenterComputing));
            clusterList.add(new Cluster(2, new Data(1, new Point2D(latMin + latH / 4, lngMin + lngW * 3.0/8)), clusterCenterComputing));
            clusterList.add(new Cluster(3, new Data(1, new Point2D(latMin + latH / 4, lngMax - lngW / 8)), clusterCenterComputing));
            clusterList.add(new Cluster(4, new Data(1, new Point2D(latMin + latH / 4, lngMax - lngW * 3.0/8)), clusterCenterComputing));
            clusterList.add(new Cluster(5, new Data(1, new Point2D(latMax - latH / 4, lngMax - lngW / 8)), clusterCenterComputing));
            clusterList.add(new Cluster(6, new Data(1, new Point2D(latMax - latH / 4, lngMax - lngW * 3.0/8)), clusterCenterComputing));
            clusterList.add(new Cluster(7, new Data(1, new Point2D(latMax - latH / 4, lngMin + lngW * 3.0/8)), clusterCenterComputing));
            clusterList.add(new Cluster(8, new Data(1, new Point2D(latMax - latH / 4, lngMin + lngW / 8)), clusterCenterComputing));
        }

        return clusterList;

    }


    public ClusterList dataSizeLteK(DataList dataList){
        ClusterList clusterList = new ClusterList(coordinateDistance);
        int i = 1;
        for (Data data: dataList) {
            Cluster cluster = new Cluster(i, data, clusterCenterComputing);
            cluster.add(data);
            clusterList.add(cluster);
            i++;
        }

        return clusterList;
    }

    /**
     *
     * @param dataList
     * @param point2D 传入初始计算时的中心点，没有时传入null
     * @return
     */
    public ClusterList runKMeansClustering(DataList dataList, Point2D point2D) {
        if(dataList.size() == 0 ){
            return null;
        }

        // 如果数据的大小小于分组数，将数据分配给每个Cluster
        if(dataList.size() <= k){
            return dataSizeLteK(dataList);
        }

        ClusterList clusterList = initClusterList(dataList, point2D);
        // 计算迭代
        for (int iter = 0; iter < maxIterations; iter++) {
            //基于质心和数据点的距离,分配没有分配的数据
            allocatedDataPoint(dataList, clusterList);
            //更新每个簇的质心
            boolean unUpdated = clusterList.updateCentroids();
            if(unUpdated){
                System.out.println("提前计算完成，第"+ iter + "轮！");
                break;
            }
            if (iter < maxIterations - 1) {
                //簇中的数据清空,进行重新迭代分配
                clusterList.clear();
            }
        }
        return clusterList;
    }

}
