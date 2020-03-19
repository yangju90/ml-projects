package init.mat.mlproj.kmeans.compute.impl;

import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;
import init.mat.mlproj.kmeans.compute.ClusterCenterComputing;
import javafx.geometry.Point2D;


public class StandardClusterCenterComputing implements ClusterCenterComputing {

//    @Override
//    public Point2D updateClusterCenter(DataList datas) {
//        double latMax = -90.0;
//        double latMin = 90.0;
//        double lngMin = 180.0;
//        double lngMax = -180.0;
//
//        for (Data data : datas) {
//            double x = data.getCoordinate().getX();
//            double y = data.getCoordinate().getY();
//
//            if(latMax < x) latMax = x;
//            if(latMin > x) latMin = x;
//            if(lngMax < y) lngMax = y;
//            if(lngMin > y) lngMin = y;
//        }
//
//        double lat, lng, x, y, z;
//        latMax = latMax*Math.PI/180;
//        lngMax = lngMax*Math.PI/180;
//        latMin = latMin*Math.PI/180;
//        lngMin = lngMin*Math.PI/180;
//        x = (Math.cos(latMax) * Math.cos(lngMax) + Math.cos(latMin) * Math.cos(lngMin)) / 2.0;
//        y = (Math.cos(latMax) * Math.sin(lngMax) + Math.cos(latMin) * Math.sin(lngMin)) / 2.0;
//        z = (Math.sin(latMax) + Math.sin(latMin)) / 2;
//
//        lng = Math.atan2(y, x) * 180 / Math.PI;
//        double temp = Math.sqrt(x*x + y*y);
//        lat = Math.atan2(z, temp) * 180 /Math.PI;
//
//        return new Point2D(lat, lng);
//    }


    @Override
    public Point2D updateClusterCenter(DataList datas) {

        double lat=0.0, lng=0.0, x=0.0, y=0.0, z=0.0;

        for (Data data : datas) {
            double x1 = data.getCoordinate().getX();
            double y1 = data.getCoordinate().getY();
            x1 = x1*Math.PI/180.0;
            y1 = y1*Math.PI/180.0;

            x += (Math.cos(x1) * Math.cos(y1));
            y += (Math.cos(x1) * Math.sin(y1));
            z += Math.sin(x1);
        }

        x /= datas.size();
        y /= datas.size();
        z /= datas.size();

        lng = Math.atan2(y, x) * 180 / Math.PI;
        double temp = Math.sqrt(x*x + y*y);
        lat = Math.atan2(z, temp) * 180 /Math.PI;

        return new Point2D(lat, lng);
    }

}
