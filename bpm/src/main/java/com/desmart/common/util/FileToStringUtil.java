package com.desmart.common.util;

import java.io.*;

public class FileToStringUtil {
    /**
     * 从本地文件中获取字符串
     * @param filePath   "D:/test.xml"
     * @return
     */
    public static String getStringFromLocalFile(String filePath){
        File file = null;
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try{
            file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            return builder.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
