package com.desmart.desmartsystem.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

/**
 * 与SAP连接配置
 * @author jay
 */

public class SAPConn {
	private static Logger log = Logger.getLogger(SAPConn.class); // 初始化日志对象
	private static final String ABAP_AS_POOLED = "ZIF_EIP_HR_PERSON";
	public static void sapConfiguration(String JCO_ASHOST,String JCO_USER,String JCO_PASSWD){
		Properties connectProperties = new Properties();
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "10.1.0.43");//服务器
		//connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, JCO_ASHOST);
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");        //系统编号
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "800");       //SAP集团
		connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "00004456");  //SAP用户名
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "chen0808");     //密码
		
		//connectProperties.setProperty(DestinationDataProvider.JCO_USER,JCO_USER);
		//connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, JCO_PASSWD);
		
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "zh");        //登录语言
		connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");  //最大连接数  
		connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");     //最大连接线程
		
		createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);
	}
	
	static {
		Properties connectProperties = new Properties();
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "10.1.0.43");//服务器
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");        //系统编号
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "800");       //SAP集团
		connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "00004456");  //SAP用户名
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "chen0808");     //密码
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "zh");        //登录语言
		connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");  //最大连接数  
		connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");     //最大连接线程
		createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);
	}
	
	
	/**
	 * 创建SAP接口属性文件。
	 * @param name	ABAP管道名称
	 * @param suffix	属性文件后缀
	 * @param properties	属性文件内容
	 */
	private static void createDataFile(String name, String suffix, Properties properties){
		File cfg = new File(name+"."+suffix);
		if(cfg.exists()){
			cfg.deleteOnExit();
		}
		try{
			FileOutputStream fos = new FileOutputStream(cfg, false);
			properties.store(fos, "for tests only !");
			fos.close();
		}catch (Exception e){
			log.error("Create Data file fault, error msg: " + e.toString());
			throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);
		}
	}
	
	/**
	 * 获取SAP连接
	 * @return	SAP连接对象
	 */
	public static JCoDestination connect(){
		JCoDestination destination =null;
		try {
			destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
		} catch (JCoException e) {
			log.error("Connect SAP fault, error msg: " + e.toString());
		}
		return destination;
	}
	
	
	 public static void main(String[] args) {  
	        JCoFunction function = null;  
	        JCoDestination destination = SAPConn.connect();  
	        Object result="";//调用接口返回状态  
	        String message="";//调用接口返回信息  
	        try {  
	            //调用ZRFC_GET_REMAIN_SUM函数  
	            function = destination.getRepository().getFunction("ZIFFI_TBPM_PERSON");  
	            JCoParameterList input = function.getImportParameterList();  
	            //采购凭证号  
	            input.setValue("PERNR_IN", "00025513");  
	            function.execute(destination); 
	         // 获取RFC返回的字段值
	            JCoParameterList exportParam = function.getExportParameterList();
	            JCoParameterList exportTable= function.getTableParameterList();

	            JCoTable getTable1 = exportTable.getTable("ITB_OUT");// 这是调用后 RFC 返回的表名
	           System.out.println(getTable1);
	             boolean loopFlag1 = !getTable1.isEmpty(); //判断 这张表中有木有数据
	             
	             
	            while(loopFlag1){ //循环获取数据 
	                String PERNR = getTable1.getString("PERNR");  //根据表字段来获取值
	                System.out.println(PERNR);
	                loopFlag1 = getTable1.nextRow(); // 移动到下一行
	            }
	        }catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }
	 
	 
	 //供应商
	 @Test
	 public void theVendorCreates() {
		 
	 }
}
