package init.mat.mlproj.kmeans.convert;

public enum ConvertEngineConstant {
    //定义转换引擎类型
    GT_CONVERT_ENGINE("GtConvertEngine", 1),

    // 定义方法类型
    //Geojson转topojson
    CONVERT_TYPE_GEO2TOPO("geo2topo", 101),
    //topojson转geojson
    CONVERT_TYPE_TOPO2GEO("topo2geo", 102);


    private String name;
    private int value;

    ConvertEngineConstant(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
