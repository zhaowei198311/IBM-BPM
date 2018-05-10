package com.desmart.desmartbpm.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.desmart.desmartbpm.common.ServerResponse;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtil {
	private static ChannelSftp sftp;
	
	private static Session session;    
    /** SFTP 登录用户名*/      
    private static String username = "root";   
    /** SFTP 登录密码*/      
    private static String password = "w0ViZ4VH";    
    /** SFTP 服务器地址IP地址*/      
    private static String host = "10.0.4.201";    
    /** SFTP 端口*/    
    private static int port = 22;
    /** SFTP 文件上传地址 */
    public static String path = "/data/opt/IBM/HTTPServer/htdocs/bpmdata";
	
	/**  
     * 连接sftp服务器  
     */    
    private static void login(){    
        try {    
            JSch jsch = new JSch();    
            session = jsch.getSession(username, host, port);    
             
            if (password != null) {    
                session.setPassword(password);      
            }    
            Properties config = new Properties();    
            config.put("StrictHostKeyChecking", "no");    
                  
            session.setConfig(config);    
            session.connect();    
                
            Channel channel = session.openChannel("sftp");    
            channel.connect();    
      
            sftp = (ChannelSftp) channel;    
        } catch (JSchException e) {    
            e.printStackTrace();  
        }    
    }      
      
    /**  
     * 关闭连接 server   
     */    
    private static void logout(){    
        if (sftp != null) {    
            if (sftp.isConnected()) {    
                sftp.disconnect();    
            }    
        }    
        if (session != null) {    
            if (session.isConnected()) {    
                session.disconnect();    
            }    
        }    
    }    
  
    /**   
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory 
     * @param basePath  服务器的基础路径  
     * @param directory  上传到该目录   
     * @param sftpFileName  sftp端文件名   
     * @param in   输入流   
     */    
    public static void upload(String basePath,String directory, String sftpFileName, InputStream input) throws SftpException{    
    	login();
    	try {     
            sftp.cd(basePath);  
            sftp.cd(directory);    
        } catch (SftpException e) {
            //目录不存在，则创建文件夹  
            String [] dirs=directory.split("/");  
            String tempPath=basePath;  
            for(String dir:dirs){  
                if(null== dir || "".equals(dir)) {
                	continue;  
                }
                tempPath+="/"+dir;  
                try{   
                    sftp.cd(tempPath);  
                }catch(SftpException ex){ 
                    sftp.mkdir(tempPath);  
                    sftp.cd(tempPath);  
                }  
            }  
        }    
        sftp.put(input, sftpFileName);  //上传文件  
        logout();
    }   
    
    /** 
     * 改名SFTP上的文件 
     * @param basePath 要修改文件的路径
     * @param oldFname 目标文件名
     * @param newFname 新的文件名
     */  
    public static boolean renameFile(String basePath,String oldFname,String newFname){  
    	login();
    	boolean flag = true;
    	try {
    		sftp.cd(basePath);
			sftp.rename(oldFname,newFname);
		} catch (SftpException e) {
			flag = false;
		} 
    	logout();
    	return flag;
    }    
    
    /**
     * 删除SFTP上的文件
     * @param basePath 要删除文件的根目录
     * @param filename 要删除的文件名
     */
    public static boolean removeFile(String basePath,String filename) {
    	login();
    	boolean flag = true;
    	try {
			sftp.cd(basePath);
			sftp.rm(filename);
		} catch (SftpException e) {
			flag = false;
		}
    	logout();
    	return flag;
    }
    
    /**
     * 将远程文件夹下的某个文件复制生成另一个文件
     * @param basePath 目标路径
     * @param oldFilename 要复制的文件名
     * @param newFilename 新的文件名
     */
    public static boolean copyFile(String basePath,String oldFilename,String newFilename) {
    	login();
    	boolean flag = true;
    	try {
			sftp.cd(basePath);
			InputStream input = sftp.get(oldFilename);
			input.close();
			sftp.put(input, newFilename);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
    	logout();
		return flag;
    }
    
    
    public static ServerResponse getFileStream(String basePath,String filename) {
    	login();
    	String result = null;
    	try {
			sftp.cd(basePath);
			InputStream input = sftp.get(filename);
			result = readInputStream(input);
			input.close();
			logout();
	    	return ServerResponse.createBySuccess(result);
		} catch (Exception e) {
			logout();
			e.printStackTrace();
	    	return ServerResponse.createByErrorMessage("获取文件失败");
		}
    }
    
    /**
	 * 获得输入流中的内容
	 */
	private static String readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return new String(bos.toByteArray(),"utf-8");
    }  
    
    /**   
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory 
     * @param basePath 服务器的基础路径  
     * @param directory 上传到该目录   
     * @param sftpFileName sftp端文件名   
     * @param out 输出流   
     */
    public static void download(String basePath,String directory, String sftpFileName, OutputStream out) {
    	
    }
}
