package com.desmart.desmartsystem.util;

import java.util.ArrayList;
import java.util.List;

import com.desmart.desmartbpm.entity.DhActivityAssign;

public class ArrayUtil {
	
	 public static  String toArrayString(List<String> string){
   	  StringBuffer buf=new StringBuffer();
   	  for (int i = 0; i < string.size(); i++) {
				if(i==string.size()-1){
					buf.append("'"+string.get(i).toString()+"'");
				}else{
					buf.append("'"+string.get(i).toString()+"',");
				}
			}
   	  return String.valueOf(buf);
     }
	 
	 public static List<String> getIdListFromDhActivityAssignList(List<DhActivityAssign> assignList) {
         List<String> idList = new ArrayList<>();
         for (DhActivityAssign assign : assignList) {
             idList.add(assign.getActaAssignId());
         }
         return idList;
     }
}
