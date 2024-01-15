package init.mat.mlproj;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import init.mat.mlproj.kmeans.KMeans;
import init.mat.mlproj.kmeans.io.IOBase;
import init.mat.mlproj.kmeans.io.impl.GeoIOConstant;
import org.junit.Test;

import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Unit test for simple App.
 */
public class KmansAppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    /**
     * GeoJSON 、TopJSON转换完成分组聚类已经开发完成，但是输出只有GeoJSON
     *
     * 默认分类k = 8组，最多训练iter =1000轮次
     * KMeans(iter, k)
     *
     * 结果：
     * {
     *  external_rectangle:
     *  cluster_id:
     *  geo_json:
     * }
     *
     *  计算消耗内存较多, 特别是TopJson有转化GeoJSON的过程.....，有时间再优化！
     */
    @Test
    public void kmeansRun() throws URISyntaxException {

        KMeans k = new KMeans();
        String path = KmansAppTest.class.getResource("/world/world-ZN-name1.json").toURI().getPath();
        System.out.println(path);
        List<JSONObject> lists = k.run((JSONObject) JSONObject.parse(IOBase.geoFileReader(path)), GeoIOConstant.GEOJSON);

        for(int i=0; i<lists.size(); i++){
            try {
                IOBase.geoFileWriter("D:/out" + i + 1 + ".geo.json", ((JSONObject)lists.get(i)).toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test() {
        String pattern = "abba";
        String s = "dog cat cat fish";
        System.out.println(wordPattern(pattern, s));
    }

    public boolean wordPattern(String pattern, String s) {
        HashMap<Character, String> invoke1 = new HashMap<>();
        HashMap<String, Character> invoke2 = new HashMap<>();
        char[] chars = pattern.toCharArray();
        String[] strs = s.split(" ");
        if(chars.length != strs.length) return false;

        for(int i = 0; i< chars.length; i++){
            if(!invoke1.containsKey(chars[i]) && !invoke2.containsKey(strs[i])){
                invoke1.put(chars[i], strs[i]);
                invoke2.put(strs[i], chars[i]);
            }else{
                if(invoke1.containsKey(chars[i])){
                    String t = invoke1.get(chars[i]);
                    if(!t.equals(strs[i])) return false;
                }else{
                    return false;
                }
            }
        }
        return true;

    }

    @Test
    public void arrayListTest(){
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        map.compute(1 , (k, v) -> new ArrayList<>()).add(2);
        System.out.println(map.get(1));
    }
}
