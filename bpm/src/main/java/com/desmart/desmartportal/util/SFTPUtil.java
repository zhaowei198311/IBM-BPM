package com.desmart.desmartportal.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
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
			//拷贝读取到的文件流  
          	ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            byte[] buffer = new byte[1024];  
            int len;  
            while ((len = input.read(buffer)) > -1 ) {  
                baos.write(buffer, 0, len);  
            }  
            baos.flush();  
            InputStream newInput = new ByteArrayInputStream(baos.toByteArray());  
            newInput = new ByteArrayInputStream(baos.toByteArray());
			result = readInputStream(input);
			newInput.close();
			baos.close();
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
        return new String(bos.toByteArray(),"UTF-8");
    } 
	
	 /**
     * 获得服务器文件输入流
     * @param gcfg 全局变量
     * @param directory 上传到该目录 
     * @param filename 文件名
     */
    public Integer getBatchDown(BpmGlobalConfig gcfg,String directory,String filename,OutputStream outputStream) {
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	try {
			sftp.cd(gcfg.getSftpPath()+directory);
			InputStream input = sftp.get(filename);
			BufferedInputStream bis = new BufferedInputStream(input);  
			 byte[] buffer = new byte[1024];  
		        int len = 0;  
		        while((len = bis.read(buffer)) != -1) {  
		        	outputStream.write(buffer, 0, len);  
		        }
		    bis.close();
		    input.close();
			logout();
	    	return 1;
		} catch (Exception e) {
			logout();
			e.printStackTrace();
	    	return 0;
		}
    }
    
    /**
     * 将文件下载到outputstream
     * @param gcfg
     * @param directory
     * @param filename
     * @param outputStream
     * @return
     */
    public Long getOututStream(BpmGlobalConfig gcfg,String directory,String filename
    		,OutputStream outputStream) {
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	try {
			sftp.cd(gcfg.getSftpPath()+directory);
			SftpATTRS sftpAttrs = sftp.stat(gcfg.getSftpPath()+directory);
			sftp.get(filename, outputStream);
			logout();
			return sftpAttrs.getSize();
		} catch (Exception e) {
			logout();
			return null;
		}
    }
    /**
     * @param gcfg
     * @param directory
     * @param filename
     * @param outputStream
     */
    public void writeToOutput(BpmGlobalConfig gcfg,String directory,String filename
    	    ,OutputStream outputStream) {
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	
    	try {
    		sftp.cd(gcfg.getSftpPath()+directory);
			InputStream input = sftp.get(filename);	
			 byte[] b=new byte[2048];  //设置每次读取大小  
			    int i = 0;
			    do {
		            i = input.read(b, 0, b.length);
		            if (i > 0) {
		            	outputStream.write(b, 0, i);
		            }
		        } while (i >= 0);
			    input.close();
			    logout();
		} catch (Exception e) {
			logout();
			e.printStackTrace();
		}
    }
    
    /**
     * 将文件批量下载到outputstream
     * @param gcfg
     * @param directory
     * @param filename
     * @param outputStream
     * @param flag 标识是否保持连接   false表示退出连接
     * @return
     */
    public void getBatchOututStream(BpmGlobalConfig gcfg,String directory,String filename
    		,OutputStream outputStream,boolean flag) {
    	login(gcfg.getSftpUserName(), gcfg.getSftpPassword(), gcfg.getSftpIp(), gcfg.getSftpPort());
    	try {
			sftp.cd(gcfg.getSftpPath()+directory);
			sftp.get(filename, outputStream);
			if(flag==false) {
			logout();
			}
		} catch (Exception e) {
			logout();
			e.printStackTrace();
		}
    }
    
    public void uploadOutputStreamByBatyes(BpmGlobalConfig gcfg,String directory
    		,String sftpFileName,byte[] bytes) throws SftpException, IOException {
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
        OutputStream outputStream = sftp.put(directory+sftpFileName); //上传文件所需要的输出流  
        byte[] bs = new byte[4096];
        int num = 0;
        while (num<=bytes.length) {
			for (int i = 0; i < bs.length; i++) {
				bs[i] = bytes[num+i];
			}
        	outputStream.write(bs);
        	num=num+bs.length;
		}
		outputStream.flush();
		outputStream.close();
        logout();
    }
}
