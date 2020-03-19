package init.mat.mlproj.kmeans.compute;

import init.mat.mlproj.kmeans.bean.DataList;
import javafx.geometry.Point2D;

public interface ClusterCenterComputing {

    /**
     * 更新计算簇中心点
     * @param datas
     * @return coordinate(latitude, longitude)
     */
    public Point2D updateClusterCenter(DataList datas);
}
