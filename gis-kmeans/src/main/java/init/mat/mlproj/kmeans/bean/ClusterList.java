package init.mat.mlproj.kmeans.bean;


import init.mat.mlproj.kmeans.compute.CoordinateDistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ClusterList implements Iterable<Cluster> {
    private final ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    private CoordinateDistance coordinateDistance;

    public ClusterList(CoordinateDistance coordinateDistance){
        this.coordinateDistance = coordinateDistance;
    }

    /** 添加一个簇 */
    public void add(Cluster cluster) {
        clusters.add(cluster);
    }
    /**
     * 迭代后需要将簇的数据全部移除
     */
    public void clear() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }
    /**
     * 计算未分配的数据离质心的距离，确定一个离质心最远的一个点
     * 方法释义： 为了合理初始化K-means簇中心点，一、可以缓解局部最优问题；二、为了加快迭代训练速度
     *        简单找出和当前簇集合距离最大的点（并非精确计算，因为没有意义）。
     * @param  dataList,数据集合
     * @return Data,数据点
     */
    public Data findFurthestData(DataList dataList) {
        double furthestDistance = Double.MIN_VALUE;
        Data furthestData = null;
        for (Data data : dataList) {
            if (!data.isAllocated()) {
                //找最远距离
                double dataDistance = coordinateDistance.calcDistance(data, this);
                if (dataDistance > furthestDistance) {
                    furthestDistance = dataDistance;
                    furthestData = data;
                }
            }
        }
        return furthestData;
    }
    /**
     * 寻找数据点data距离最近的簇
     *
     * @param data,数据
     * @return Cluster,簇
     */
    public Cluster findNearestCluster(Data data) {
        Cluster nearestCluster = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Cluster cluster : clusters) {
            //计算距离
            double clusterDistance = coordinateDistance.calcDistance(data, cluster);
            if (clusterDistance < nearestDistance) {
                nearestDistance = clusterDistance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    @Override
    public Iterator<Cluster> iterator() {
        return clusters.iterator();
    }

    /**返回簇的数量 */
    public int size() {
        return clusters.size();
    }
    /**
     *数据点序号排序,然后排序cluster
     */
    private void sort() {
        for (Cluster cluster : this) {
            cluster.sort();
        }
        Collections.sort(clusters);
    }
    /**
     * 输出情况下展示结果
     */
    public String toString() {
//        sort();
        StringBuilder sb = new StringBuilder();
        for (Cluster cluster : clusters) {
            sb.append("Cluster: ");
            sb.append(cluster.getClusterID());
            sb.append("[lat: " + cluster.getCoordinate().getX() + ", lng: " + cluster.getCoordinate().getY() + "]");
            sb.append("\n");
            sb.append(cluster);
        }
        return sb.toString();
    }

    /**基于各维度的算术平均值更新每个簇质心, 同时计算簇的质心有无变化
     * 若无变化，返回ture，否则为false
     *
     * */
    public boolean updateCentroids() {
        boolean result = true;
        for (Cluster cluster : clusters) {
            if(!cluster.updateCentroid()){
                result = false;
            }
        }
        return result;
    }
}
