package com.desmart.desmartbpm.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtil {
	private ChannelSftp sftp;
	
	private Session session;    
    
	/**  
     * 连接sftp服务器  
     */    
    private void login(String username,String password,String host,int port){    
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
    private void logout(){    
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
     * 将输入流的数据上传到sftp作为文件
     * @param gcfg 全局变量
     * @param directory  上传到该目录   
     * @param sftpFileName  sftp端文件名   
     * @param in   输入流   
     */    
    public void upload(BpmGlobalConfig gcfg,String directory, String sftpFileName, InputStream input) throws SftpException{    
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	try {     
            sftp.cd(gcfg.getSftpPath());  
            sftp.cd(directory);    
        } catch (SftpException e) {
            //目录不存在，则创建文件夹  
            String [] dirs=directory.split("/");  
            String tempPath=gcfg.getSftpPath();  
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
     * @param gcfg 全局变量
     * @param directory  上传到该目录   
     * @param oldFname 目标文件名
     * @param newFname 新的文件名
     */  
    public boolean renameFile(BpmGlobalConfig gcfg,String directory,String oldFname,String newFname){  
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	boolean flag = true;
    	try {
    		sftp.cd(gcfg.getSftpPath()+directory);
			sftp.rename(oldFname,newFname);
		} catch (SftpException e) {
			flag = false;
		} 
    	logout();
    	return flag;
    }    
    
    /**
     * 删除SFTP上的文件
     * @param gcfg 全局变量
     * @param directory  上传到该目录   
     * @param filename 要删除的文件名
     */
    public boolean removeFile(BpmGlobalConfig gcfg,String directory,String filename) {
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	boolean flag = true;
    	try {
			sftp.cd(gcfg.getSftpPath()+directory);
			sftp.rm(filename);
		} catch (SftpException e) {
			flag = false;
		}
    	logout();
    	return flag;
    }
    
    /**
     * 将远程文件夹下的某个文件复制生成另一个文件
     * @param gcfg 全局变量
     * @param directory 上传到该目录   
     * @param oldFilename 要复制的文件名
     * @param newFilename 新的文件名
     */
    public boolean copyFile(BpmGlobalConfig gcfg,String directory,String oldFilename,String newFilename) {
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	boolean flag = true;
    	try {
			sftp.cd(gcfg.getSftpPath()+directory);
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
    
    /**
     * 获得文件的输入流中的内容
     * @param gcfg 全局变量
     * @param directory 上传到该目录 
     * @param filename 文件名
     */
    public ServerResponse getFileStream(BpmGlobalConfig gcfg,String directory,String filename) {
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	String result = null;
    	try {
			sftp.cd(gcfg.getSftpPath()+directory);
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
	private String readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return new String(bos.toByteArray(),"GBK");
    }  
}
