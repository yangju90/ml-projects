package init.mat.mlproj.kmeans.io;

import init.mat.mlproj.kmeans.io.impl.GeoIOConstant;
import init.mat.mlproj.kmeans.io.impl.GeoJsonGeoIO;
import init.mat.mlproj.kmeans.io.impl.TopJsonGeoIO;

public class GeoIOFactory {

    public static GeoIO get(GeoIOConstant geoType){
        GeoIO geoIO = null;
        switch (geoType){
            case TOPJSON:
                geoIO = new TopJsonGeoIO();
                break;
            case GEOJSON:
                geoIO = new GeoJsonGeoIO();
                break;
            default:
                geoIO = new GeoJsonGeoIO();
        }

        return geoIO;
    }

}
