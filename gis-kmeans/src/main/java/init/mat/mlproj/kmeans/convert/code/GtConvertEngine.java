package init.mat.mlproj.kmeans.convert.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import init.mat.mlproj.kmeans.convert.ConvertEngine;
import init.mat.mlproj.kmeans.convert.LoadUtil;
import init.mat.mlproj.kmeans.exception.UnkonwDataFormatException;
import init.mat.mlproj.kmeans.io.IOBase;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

public class GtConvertEngine implements ConvertEngine {

    private Invocable inv = null;

    String outPath = "";


    public GtConvertEngine() {
        if (inv == null) {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void init() throws Exception {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("nashorn");
        List<Object> list = LoadUtil.gets(GtLoadedFunction.class);
        for (Object o : list) {
            if (o instanceof String) {
                se.eval((String) o);
            }
        }
        inv = (Invocable) se;
    }


    @Override
    public String convert(int code, String data, Object... args) throws Exception {
        String str = "";
        switch (code) {
            case 101:
                if (args.length == 0) {
                    str = geo2topo(data);
                } else {
                    str = geo2topo(data, args[0]);
                }
                break;
            case 102:
                str = topo2geo(data);
                break;
            default:
                throw new IllegalStateException("Unexpected code value: " + code);
        }


        return str;
    }


    private String geo2topo(String data, Object... args) throws Exception {
        String topo = "";
        String str = "{\"foo\":" + data + "}";
        if (args.length == 0) {
            topo = (String) inv.invokeFunction("geo2topo", str);
        } else {
            topo = (String) inv.invokeFunction("geo2topo", str, args[0]);
        }
        if (!"".equals(outPath))
            IOBase.geoFileWriter(outPath, topo);
        return topo;
    }


    private String topo2geo(String data) throws Exception {
        String topoObjects = data;
        String fooObjects = fooObjects(data);
        String geojson = (String) inv.invokeFunction("topo2geo", topoObjects, fooObjects);
        if (!"".equals(outPath))
            IOBase.geoFileWriter(outPath, geojson);
        return geojson;
    }


    private String fooObjects(String str) throws Exception {
        String res = "";

        JSONObject objects = JSON.parseObject(str).getJSONObject("objects");
        Object[] strs = objects.keySet().toArray();
        if (strs.length == 1) {
            res = objects.getString(strs[0].toString());
        } else {
            throw new UnkonwDataFormatException("数据错误或未知的数据格式！");
        }

        return res;
    }


}
