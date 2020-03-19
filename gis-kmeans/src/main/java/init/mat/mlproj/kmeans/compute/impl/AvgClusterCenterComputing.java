package init.mat.mlproj.kmeans.compute.impl;

import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;
import init.mat.mlproj.kmeans.compute.ClusterCenterComputing;
import javafx.geometry.Point2D;


public class AvgClusterCenterComputing implements ClusterCenterComputing {

    /**
     * 计算400KM以内的点的均值
     *
     * @param datas
     * @return
     */
    @Override
    public Point2D updateClusterCenter(DataList datas) {
        double lat = 0.0;
        double lng = 0.0;
        for (Data data : datas) {
            lat += data.getCoordinate().getX();
            lng += data.getCoordinate().getY();
        }
        lat = lat / datas.size();
        lng = lng / datas.size();
        return new Point2D(lat, lng);
    }
}
