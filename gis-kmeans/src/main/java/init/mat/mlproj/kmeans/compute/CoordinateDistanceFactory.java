package init.mat.mlproj.kmeans.compute;


import init.mat.mlproj.kmeans.compute.impl.DistanceConstant;
import init.mat.mlproj.kmeans.compute.impl.SimpleCoordinateDistance;
import init.mat.mlproj.kmeans.compute.impl.StandardCoordinateDistance;

public class CoordinateDistanceFactory {

    public static CoordinateDistance get(DistanceConstant distanceConstant){

        CoordinateDistance res = null;

        switch (distanceConstant){
            case SIMPLE:
                res = new SimpleCoordinateDistance();
                break;
            case STANDARD:
                res = new StandardCoordinateDistance();
                break;
            default:
                res = new SimpleCoordinateDistance();
        }

        return res;
    }

}
