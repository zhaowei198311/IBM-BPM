package com.desmart.desmartsystem.util;
 
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
 
import org.apache.log4j.Logger;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;
 
/**
 * 与SAP连接配置
 * @author jay
 */
public class SAPConn {
	private static final String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";
	static{
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
	private static Logger log = Logger.getLogger(SAPConn.class); // 初始化日志对象
}