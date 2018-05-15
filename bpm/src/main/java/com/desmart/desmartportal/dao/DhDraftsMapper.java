/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhDrafts;
/**  
* <p>Title: 草稿箱接口</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月28日  
*/
@Repository
public interface DhDraftsMapper {
		
	int deleteBydfsId(String dfsId);
	
	List <DhDrafts> select();
	
	List <DhDrafts> selectBydfsTitle(String dfsTitle);
	
	int save(DhDrafts drafts);
	
	List <DhDrafts> selectBydfsId(String dfsId);
}
