package init.mat.mlproj.kmeans.bean;


import init.mat.mlproj.kmeans.compute.ClusterCenterComputing;
import javafx.geometry.Point2D;


public class Cluster implements Comparable<Cluster> {
    private int clusterID;
    private Point2D coordinate;
    private final DataList datas = new DataList();
    private final ClusterCenterComputing clusterCenterComputing;

    public Cluster(int clusterID, Data data, ClusterCenterComputing clusterCenterComputing) {
        this.clusterID = clusterID;
        this.coordinate = data.getCoordinate();
        this.clusterCenterComputing = clusterCenterComputing;
    }

    public void add(Data data) {
        datas.add(data);
    }

    /** 将簇中的数据移除 */
    public void clear() {
        datas.clear();
    }

    /** 获取簇质心点 **/
    public Point2D getCoordinate() {
        return coordinate;
    }

    /** 获取数据 */
    public DataList getDatas() {
        return datas;
    }

    /** 簇中数据的数量 */
    public int size() {
        return datas.size();
    }

    public int getClusterID() {
        return clusterID;
    }

    /** 对簇中的数据进行排序. */
    @Override
    public int compareTo(Cluster cluster) {
        if (datas.isEmpty() || cluster.datas.isEmpty()) {
            return 0;
        }
        return datas.get(0).compareTo(cluster.datas.get(0));
    }

    /** 需要展示的结果 */
    @Override
    public String toString() {
        return datas.toString();
    }

    /** 基于数据点的id，排序 */
    public void sort() {
        datas.sort();
    }

    /** 更新该簇的质心, 若质心无变化，返回true */
    public boolean updateCentroid() {
        if(this.datas.size() == 0){
            return true;
        }

        Point2D updateCenter = clusterCenterComputing.updateClusterCenter(this.datas);
        boolean result = point2dEquals(updateCenter);
        coordinate = updateCenter;
        return result;
    }

    public boolean point2dEquals(Point2D point2D){
        if(coordinate.getY() == point2D.getY() && coordinate.getX() == point2D.getX()){
            return true;
        }
        return false;
    }
}
