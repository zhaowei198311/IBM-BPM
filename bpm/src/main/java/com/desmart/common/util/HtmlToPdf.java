package com.desmart.common.util;

import java.io.File;

public class HtmlToPdf {

	//wkhtmltopdf在系统中的路径  
    private static final String toPdfTool = "D:\\wkhtmltox\\bin\\wkhtmltopdf.exe";  
      
    public static boolean convert(String srcPath, String destPath){  
    	File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if(!parent.exists()){
            parent.mkdirs();
        }
        
        StringBuilder cmd = new StringBuilder();
        cmd.append(toPdfTool);
        cmd.append(" ");
        cmd.append("  --header-line");//页眉下面的线
        cmd.append("  --header-center 这里是页眉 ");//页眉中间内容
        //cmd.append("  --margin-top 30mm ");//设置页面上边距 (default 10mm) 
        cmd.append(" --header-spacing 10 ");//    (设置页眉和内容的距离,默认0)
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);
        
        boolean result = true;
        try{
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        }catch(Exception e){
            result = false;
            e.printStackTrace();
        }
        return result;
    }  
    
	/*public String getCommand(String sourceFilePath, String targetFilePath) {
		String system = System.getProperty("os.name");
		if (system.contains("Windows")) {
			return "D:\\wkhtmltox\\bin\\wkhtmltopdf.exe " + sourceFilePath + " " + targetFilePath;
		} else if (system.contains("Linux")) {
			return "wkhtmltopdf " + sourceFilePath + " " + targetFilePath;
		}
		return "";
	}

	public static void main(String[] args) throws Exception {
		HtmlToPdf util = new HtmlToPdf();
		String command = util.getCommand("http://172.19.55.73:8080/bpm/user/menus", "D:\\result.pdf");
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor(); // 这个调用比较关键，就是等当前命令执行完成后再往下执行
		System.out.println("执行完成");
	}*/
}

