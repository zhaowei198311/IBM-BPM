package com.desmart.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlToPdfInterceptor extends Thread {
	private static Logger logger = LoggerFactory.getLogger(HtmlToPdfInterceptor.class);
    private InputStream is;
    
    public HtmlToPdfInterceptor(InputStream is){
        this.is = is;
    }
    
    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line.toString()); //输出内容
            }
        }catch (IOException e){
        	logger.error("生成预览页执行信息失败", e);
            e.printStackTrace();
        }
    }
}