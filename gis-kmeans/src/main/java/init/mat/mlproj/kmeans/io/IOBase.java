package init.mat.mlproj.kmeans.io;


import java.io.*;

public class IOBase {
    public static String geoFileReader(String path){
        File file = new File(path);
        return geoFileReader(file);
    }

    public static String geoFileReader(File file){
        String result = null;
        StringBuffer strBuffer = new StringBuffer();

        try (FileInputStream fi = new FileInputStream(file);
             InputStreamReader ir = new InputStreamReader(fi, "UTF-8");
             BufferedReader fr = new BufferedReader(ir);
        ){
            char[] chars = new char[1024];
            int i = 0;

            while((i = fr.read(chars)) > 0){
                strBuffer.append(new String(chars, 0 , i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = strBuffer.toString();

        return result;
    }

    public static String readInputStream(InputStream inputStream){

        String result = null;
        StringBuffer strBuffer = new StringBuffer();

        try (InputStreamReader ir = new InputStreamReader(inputStream, "UTF-8");
             BufferedReader fr = new BufferedReader(ir);
        ){
            char[] chars = new char[1024];
            int i = 0;

            while((i = fr.read(chars)) > 0){
                strBuffer.append(new String(chars, 0 , i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = strBuffer.toString();

        return result;
    }



    public static boolean geoFileWriter(String path, String text) throws IOException {
        boolean result = true;

        StringBuffer strBuffer = new StringBuffer();

        try (FileOutputStream fo = new FileOutputStream(path);
             OutputStreamWriter ow = new OutputStreamWriter(fo, "UTF-8");
             BufferedWriter fw = new BufferedWriter(ow);
        ){
            fw.write(text.toCharArray());
            fw.flush();
        } catch (IOException e){
            e.printStackTrace();
            result = false;
        }

        return result;
    }
}
