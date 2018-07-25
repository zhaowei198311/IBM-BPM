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
	
	int save(DhDrafts drafts);
	
	DhDrafts selectBydfsId(String dfsId);
	
	DhDrafts queryDraftsByInsUid(String insUid);
	
	DhDrafts queryDraftsByTaskUid(String taskUid);
	
	int deleteByInsUid(String insUid);
	
	/**
	 * 根据insUid修改草稿数据
	 * @param dhDrafts
	 * @return
	 */
	Integer updateByInsUid(DhDrafts dhDrafts);

	int deleteByTaskUid(String taskUid);
	
	/**
	 * 根据条件查询草稿，包含关联映射
	 * @param dhDrafts
	 * @return
	 */
	List<DhDrafts> selectByCondition(DhDrafts dhDrafts);
}
