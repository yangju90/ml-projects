package init.mat.mlproj.kmeans.util;

import init.mat.mlproj.kmeans.bean.Data;
import init.mat.mlproj.kmeans.bean.DataList;

import java.util.ArrayList;
import java.util.List;

public class CharFilter {


    // 将各种shape图形的coordinate坐标点提取出来
    // 如 [[[x1,y1],[x2,y2]],[x3,y3]] 转换为 [[x1,y1],[x2,y2],[x3,y3]]
    public static void parseCoordinateString(String value, DataList dataList) {

        char[] chars = value.toCharArray();

        boolean left = false;
        boolean mid = false;
        boolean right = false;
        boolean sign = false;

        StringBuffer temp = new StringBuffer();
        double lng = 0.0;
        double lat = 0.0;

        for (char c: chars) {
            switch(c){
                case '[':
                    left = true;
                    break;
                case ',':
                    if(left) {
                        mid = true;
                        sign = true;
                    }
                    break;
                case ']':
                    if(left && mid) {
                        right = true;
                        sign = true;
                    }
                    break;
                default:
                    temp.append(c);
            }

            if(mid && sign && !right){
                sign = false;
                lng = Double.parseDouble(temp.toString());
                temp = new StringBuffer();
            }

            if(mid && sign && right){
                left = false;
                mid = false;
                right = false;
                sign = false;

                lat = Double.parseDouble(temp.toString());
                temp = new StringBuffer();

                List coordinate =  new ArrayList<>();
                coordinate.add(lng);
                coordinate.add(lat);
                dataList.add(new Data(0,lat,lng));

            }

        }

        if(left || right || mid || sign){
            throw new RuntimeException("数据合适不正确，必须保证字符串[ 、，、] 闭合成对！");
        }

    }

}
