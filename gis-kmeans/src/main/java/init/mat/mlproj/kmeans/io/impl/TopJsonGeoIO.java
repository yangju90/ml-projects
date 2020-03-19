package init.mat.mlproj.kmeans.io.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import init.mat.mlproj.kmeans.bean.Cluster;
import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;
import init.mat.mlproj.kmeans.io.GeoIO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TopJsonGeoIO extends GeoIO {

    @Override
    public List<JSONObject> getCoordinateList(JSONObject json) {
        if(json != null && json.containsKey("objects")) {
            JSONArray jsonArray = new JSONArray();
            JSONObject object = json.getJSONObject("objects");
            Set<String> set = object.keySet();
            if(set.size() == 1){
                Object[] strArr = set.toArray();
                String key = (String)strArr[0];
                jsonArray = object.getJSONObject(key).getJSONArray("geometries");
            }else{
                throw new RuntimeException("新的topjson格式，需要分析！");
            }

            List<JSONObject> res = new ArrayList<JSONObject>();
            for (Object obj : jsonArray) {
                if (obj instanceof JSONObject) {
                    res.add((JSONObject) obj);
                }
            }

            return res;
        }else {
            return new ArrayList<>();
        }
    }

    public DataList geoCoordinateToDataList(List<JSONObject> listJson){
        if(listJson.size() == 0 || listJson == null){
            throw new RuntimeException("传入的数据源数组，不能为空！");
        }

        DataList dataList = new DataList();

        for (int i=0; i< listJson.size(); i++) {
            Object object = listJson.get(i).get("coordinates");
            if(object instanceof List){
                JSONArray l = (JSONArray) object;
                if(l.size() == 2) {
                    dataList.add(new Data(i, Double.parseDouble(l.get(1).toString()), Double.parseDouble(l.get(0).toString())));
                }
            }

        }

        return dataList;
    }

    public List<JSONObject> assembleData(Cluster cluster, List<JSONObject> listJson){
        List<JSONObject> res = new ArrayList<JSONObject>();
        if(cluster != null){
            DataList dataList = cluster.getDatas();
            if(dataList != null){
                Iterator dataIterator =  dataList.iterator();
                while(dataIterator.hasNext()){
                    Data data = (Data) dataIterator.next();
                    JSONObject jo = listJson.get((int) data.getId());
                    JSONObject geometry = new JSONObject();
                    geometry.put("type", jo.getString("type"));
                    geometry.put("coordinates", jo.get("coordinates"));
                    jo.remove("type");
                    jo.remove("coordinates");
                    jo.put("geometry", geometry);
                    jo.put("type", "Feature");
                    res.add(jo);
                }

            }
        }

        return res;
    }


}
