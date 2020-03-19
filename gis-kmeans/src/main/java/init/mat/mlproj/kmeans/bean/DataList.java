package init.mat.mlproj.kmeans.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** 数据集合表示和操作的类 */
public class DataList implements Iterable<Data> {
    private final List<Data> datas = new ArrayList<Data>();
    /** 构造空的DataList */
    public DataList() {
    }

    /** 添加数据至DataList */
    public void add(Data data) {
        datas.add(data);
    }

    /** 数据集合移除 */
    public void clear() {
        datas.clear();
    }

    /** 获取id为index的数据点 */
    public Data get(int index) {
        return datas.get(index);
    }
    /** 确定集合是否为空 */
    public boolean isEmpty() {
        return datas.isEmpty();
    }

    @Override
    public Iterator<Data> iterator() {
        return datas.iterator();
    }

    /** 数据的数量 */
    public int size() {
        return datas.size();
    }

    /** 集合排序 */
    public void sort() {
        Collections.sort(datas);
    }
    /** 需要展示的结果 */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Data data : datas) {
            sb.append("  ");
            //获取数据
            sb.append(data.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
