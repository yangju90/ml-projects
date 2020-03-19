package init.mat.mlproj.kmeans.bean;


import javafx.geometry.Point2D;

/** 单个数据点表示的类 */
public class Data implements Comparable<Data> {
    private final long id;
    // coordinate. getX - lat; getY - lng
    private final Point2D coordinate;
    private boolean allocated = false;



    public Data(long id, double lat, double lng) {
        this.id = id;
        coordinate = new Point2D(lat, lng);
    }

    public Data(long id, Point2D coordinate) {
        this.id = id;
        this.coordinate = coordinate;
    }

    /** 继承,按照id排序输出 */
    @Override
    public int compareTo(Data data) {
        if (id > data.getId()) {
            return 1;
        } else if (id < data.getId()) {
            return -1;
        } else {
            return 0;
        }
    }

    public long getId() {
        return id;
    }

    public Point2D getCoordinate() {
        return coordinate;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    //默认输出
    public String toString() {
        return "Data: " + id + ", [lat: " + coordinate.getX() + ", lng: " + coordinate.getY() + "]";
    }
}
