package init.mat.mlproj.kmeans.compute;


import init.mat.mlproj.kmeans.compute.impl.AvgClusterCenterComputing;
import init.mat.mlproj.kmeans.compute.impl.ClusterCenterConstant;
import init.mat.mlproj.kmeans.compute.impl.StandardClusterCenterComputing;

public class ClusterCenterComputingFactory {

    public static ClusterCenterComputing get(ClusterCenterConstant clusterCenterConstant){
        ClusterCenterComputing clusterCenterComputing = null;
        switch (clusterCenterConstant){
            case AVG:
                clusterCenterComputing = new AvgClusterCenterComputing();
                break;
            case STANDARD:
                clusterCenterComputing = new StandardClusterCenterComputing();
                break;
            default:
                clusterCenterComputing = new AvgClusterCenterComputing();
        }

        return clusterCenterComputing;
    }

}
