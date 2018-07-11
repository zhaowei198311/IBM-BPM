package com.desmart.desmartsystem.util;

import org.junit.Test;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

public class GetPoFromSAP {

	@Test
	// ZIFFI_TBPM_LIFNR 供应商信息查询
	public void supplierSearch() {
		JCoFunction function = null;
		JCoDestination destination = SAPConn.connect();
		String result="";//调用接口返回状态
		String message="";//调用接口返回信息
		try {
			//调用ZRFC_GET_REMAIN_SUM函数
			function = destination.getRepository().getFunction("ZIFFI_TBPM_LIFNR");
			JCoParameterList input = function.getImportParameterList();
			//采购凭证号
			input.setValue("LIFNR_IN", "604655");
			
			function.execute(destination);
			result= function.getTableParameterList().getTable("RETURN").getString("TYPE");//调用接口返回状态
			message=function.getTableParameterList().getTable("RETURN").getString("MESSAGE");//调用接口返回信息
			
			if(result.equals("E")){
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				return;
			}else{
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				JCoParameterList tblexport = function.getTableParameterList();
				//JCoParameterList tblexport = function.getTableParameterList().getTable("ITB_IN");
				String msg = tblexport.toXML();
				System.out.println("调用返回表XML--->"+msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	//用户数据查询
	public void searchUser() {
		JCoFunction function = null;
		JCoDestination destination = SAPConn.connect();
		String result="";//调用接口返回状态
		String message="";//调用接口返回信息
		try {
			//调用ZRFC_GET_REMAIN_SUM函数
			function = destination.getRepository().getFunction("ZIFFI_TBPM_PERSON");
			JCoParameterList input = function.getImportParameterList();
			//采购凭证号
			input.setValue("PERNR_IN", "00025641");
			
			function.execute(destination);
			result= function.getTableParameterList().getTable("RETURN").getString("TYPE");//调用接口返回状态
			message=function.getTableParameterList().getTable("RETURN").getString("MESSAGE");//调用接口返回信息
			
			if(result.equals("E")){
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				return;
			}else{
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				JCoParameterList tblexport = function.getTableParameterList();
				//JCoParameterList tblexport = function.getTableParameterList().getTable("ITB_IN");
				String msg = tblexport.toXML();
				System.out.println("调用返回表XML--->"+msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//修改供应商数据
	public void update(){
		JCoFunction function = null;
		JCoDestination destination = SAPConn.connect();
		String result = "";// 调用接口返回状态
		String message = "";// 调用接口返回信息
		try {
			// 调用ZRFC_GET_REMAIN_SUM函数
			function = destination.getRepository().getFunction("ZIFSD_TBPM_LIFNR_M");
			if (null == function) {
				throw new RuntimeException("get function not found in sap");
			} else {
				JCoParameterList paramList = function.getImportParameterList();
				JCoTable table1 = function.getTableParameterList().getTable("ITB_IN");
				for (int i = 0; i < 1; i++) {
					table1.setRow(i);
					table1.appendRow();
					table1.setValue("PNO", MyDateUtils.getCurrentDate() +"_"+ "000003");
					table1.setValue("LIFNR", "604655");
					table1.setValue("NAME1", "南昌市东湖区迪卡乐娱乐休闲中心1");
					table1.setValue("NAME2", "AA01-江西省南昌市江大南路店");
					table1.setValue("SORT1", "AA01-南昌市东湖区迪卡乐娱乐休闲中心");
					table1.setValue("SORT2", "AA01-江西省南昌市江大南路店");
					table1.setValue("BUKRS", "3500");
					table1.setValue("KTOKK", "9002");
					table1.setValue("EKORG", "1000");
					table1.setValue("NAMEV", "杨晓霞");
					table1.setValue("NAME1_GP", "13907093150");
					table1.setValue("STREET", "江西省南昌市青山湖区湖坊镇江大南路193号");
					table1.setValue("TEL_NUMBER", "13907093150");
					table1.setValue("FAX_NUMBER", "");
					table1.setValue("BANKS", "CN");
					table1.setValue("BANKL", "");
					table1.setValue("BANKA", "招商银行南昌北京西路支行");
					table1.setValue("BANKN", "1");
					table1.setValue("KOINH", "杨晓霞_4100627917666969");
					table1.setValue("AKONT", "22020200");
					table1.setValue("ZTERM", "0001");
					table1.setValue("ZWELS", "T");
					table1.setValue("WAERS", "CNY");
					table1.setValue("QSZDT", "20180506");
					table1.setValue("ZINDT", "20180506");
					table1.setValue("DATLZ", "20210505");
					table1.setValue("CERDT", "20180906");
				}
			}
			function.execute(destination);
			
			result= function.getTableParameterList().getTable("RETURN").getString("TYPE");//调用接口返回状态
			message=function.getTableParameterList().getTable("RETURN").getString("MESSAGE");//调用接口返回信息
			
			
			
			if(result.equals("E")){
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				return;
			}else{
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				JCoParameterList tblexport = function.getTableParameterList();
				String msg = tblexport.toXML();
				System.out.println("调用返回表XML--->"+msg);
			}
			
//			JCoTable table2 = function.getTableParameterList().getTable("RETURN");// 调用接口返回信息
//			for (int i = 0; i < table2.getNumRows(); i++) {
//				table2.setRow(i);
//				String MESSAGE = table2.getString("MESSAGE");
//				System.out.println(MESSAGE);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			destination = null;
		}
	}

	
	//添加供应商数据
	public static void main(String[] args) {
		JCoFunction function = null;
		JCoDestination destination = SAPConn.connect();
		String result = "";// 调用接口返回状态
		String message = "";// 调用接口返回信息
		try {
			// 调用ZRFC_GET_REMAIN_SUM函数
			function = destination.getRepository().getFunction("ZIFSD_TBPM_LIFNR_M");
			if (null == function) {
				throw new RuntimeException("get function not found in sap");
			} else {
				JCoParameterList paramList = function.getImportParameterList();
				JCoTable table1 = function.getTableParameterList().getTable("ITB_IN");
				for (int i = 0; i < 1; i++) {
					table1.setRow(i);
					table1.appendRow();
					table1.setValue("PNO", "20180506");
					table1.setValue("NAME1", "南昌市东湖区迪卡乐娱乐休闲中心");
					table1.setValue("NAME2", "20210505");
					table1.setValue("SORT1", "20180906");
					table1.setValue("SORT2", "20180506");
					table1.setValue("BUKRS", "20180506");
					table1.setValue("VKORG", "20210505");
					table1.setValue("KTOKK", "20180906");
					table1.setValue("EKORG", "20180506");
					table1.setValue("LOCLB", "20180506");
					table1.setValue("NAMEV", "20210505");
					table1.setValue("NAME1_GP", "20180906");
					table1.setValue("STREET", "20180506");
					table1.setValue("REGION", "20180506");
					table1.setValue("KVERM", "20210505");
					table1.setValue("TEL_NUMBER", "20180906");
					table1.setValue("FAX_NUMBER", "20180506");
					table1.setValue("BANKS", "20180506");
					table1.setValue("BANKL", "20210505");
					table1.setValue("BANKA", "20180906");
					table1.setValue("BANKN", "20180506");
					table1.setValue("KOINH", "20180506");
					table1.setValue("AKONT", "20210505");
					table1.setValue("ZTERM", "20180906");
					table1.setValue("ZWELS", "20180506");
					table1.setValue("WAERS", "20180506");
					table1.setValue("QSZDT", "20210505");
					table1.setValue("ZINDT", "20180906");
					table1.setValue("DATLZ", "20180506");
					table1.setValue("CERDT", "20180506");
					table1.setValue("LTYPE", "20210505");
					
				}
			}
			function.execute(destination);
			
			result= function.getTableParameterList().getTable("RETURN").getString("TYPE");//调用接口返回状态
			message=function.getTableParameterList().getTable("RETURN").getString("MESSAGE");//调用接口返回信息
			if(result.equals("E")){
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				return;
			}else{
				System.out.println("调用返回状态--->"+result+";调用返回信息--->"+message);
				JCoParameterList tblexport = function.getTableParameterList();
				String msg = tblexport.toXML();
				System.out.println("调用返回表XML--->"+msg);
			}
			
//			JCoTable table2 = function.getTableParameterList().getTable("RETURN");// 调用接口返回信息
//			for (int i = 0; i < table2.getNumRows(); i++) {
//				table2.setRow(i);
//				String MESSAGE = table2.getString("MESSAGE");
//				System.out.println(MESSAGE);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			destination = null;
		}
	}
	
	
	
}