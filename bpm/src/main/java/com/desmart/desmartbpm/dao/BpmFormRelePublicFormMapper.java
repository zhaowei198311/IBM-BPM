/**
 * 
 */
package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmFormField;

/**  
* <p>Title: 表单and子表单接口</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年6月20日  
*/
@Repository
public interface BpmFormRelePublicFormMapper {
	
	/**
	 * 根据表单id 查询所有子表单数据信息
	 */
	List<BpmFormField> listPublicFormFieldByFormUid(String formUid);
}
