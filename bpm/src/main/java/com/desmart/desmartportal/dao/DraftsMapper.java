/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.Drafts;
/**  
* <p>Title: 草稿箱接口</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月28日  
*/
@Repository
public interface DraftsMapper {
		
	int deleteBydfsId(String dfsId);
	
	List <Drafts> select();
	
	List <Drafts> selectBydfsTitle(String dfsTitle);
	
	int save(Drafts drafts);
	
	List <Drafts> selectBydfsId(String dfsId);
}
