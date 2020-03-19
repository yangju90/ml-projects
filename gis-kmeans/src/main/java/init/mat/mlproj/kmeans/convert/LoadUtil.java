package init.mat.mlproj.kmeans.convert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LoadUtil {

    public static <T> List<Object> gets(Class<T> clazz){
        List<Object> res  = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field f: fields) {
            try {
                T obj = clazz.newInstance();
                Method method = clazz.getMethod(getFuncName(f.getName()));
                Object o = method.invoke(obj);
                res.add(o);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        return res;
    }


    public static String getFuncName(String f){
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(f.substring(0,1).toUpperCase());
        sb.append(f.substring(1));
        return sb.toString();
    }
}
