package init.mat.mlproj.kmeans.exception;

public class InvalidGeoJsonException extends RuntimeException {

    public InvalidGeoJsonException() {
        super();
    }

    public InvalidGeoJsonException(String message) {
        super(message);
    }

    public InvalidGeoJsonException(String message, Throwable cause) {
        super(message, cause);
    }

}
