package init.mat.mlproj.kmeans.convert;

import init.mat.mlproj.kmeans.convert.code.GtConvertEngine;

import java.util.HashMap;

public class ConvertFactory {
    private static HashMap<String, ConvertEngine> engineHashMap = new HashMap<String, ConvertEngine>();

    public static ConvertEngine get(ConvertEngineConstant cec) {
        ConvertEngine res = null;

        if (engineHashMap.containsKey(cec.getName())) {
            res = engineHashMap.get(cec.getName());
        } else {
            switch (cec.getValue()) {
                case 1:
                    res = new GtConvertEngine();
                    break;
                default:
                    res = new GtConvertEngine();
            }
        }
        return res;
    }
}
