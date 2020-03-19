package init.mat.mlproj.kmeans.compute.impl;


import init.mat.mlproj.kmeans.compute.CoordinateDistance;

public class SimpleCoordinateDistance extends CoordinateDistance {

    public double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double a = lat1 - lat2;
        double b = lng1 - lng2;
        return Math.sqrt(a * a + b * b);
    }

}
