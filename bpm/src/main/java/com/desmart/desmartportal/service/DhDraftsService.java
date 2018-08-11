/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhDrafts;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 草稿箱接口</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月28日  
*/
public interface DhDraftsService {
	
	/**
	 * 根据草稿箱主键id  删除草稿
	 * @param dfsId 草稿主键id
	 * @return
	 */
	int deleteDraftsBydfsId(String dfsId);
	
	/**
	 * 查询所有草稿数据
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhDrafts>>> selectDraftsList(Integer pageNum, Integer pageSize, String insTitle, String proName);
	
	/**
	 * 保存草稿数据
	 * @param drafts 草稿实体类
	 * @return
	 */
	int saveDrafts(DhDrafts drafts);
	
	/**
	 * 根据草稿id主键 查询草稿shuju
	 * @param dfsId 草稿id
	 * @return
	 */
	DhDrafts selectBydfsId(String dfsId);
	
	/**
	 *  根据草稿id主键和流程实例ID 查询草稿数据和表单数据
	 * @param dfsId 草稿id
	 * @param insUid 流程实例id
	 * @return
	 */
	Map<String, Object> selectDraftsAndFromInfo(String dfsId,String insUid);

	/**
	 * 根据insUid检查是否存在草稿数据，无草稿则保存一份草稿, 有则不更新
	 * @param dhDrafts
	 * @return
	 */
	ServerResponse saveIfNotExists(DhDrafts dhDrafts);

	/**
	 * 保存起草环节的草稿
	 * @param dhDrafts
	 * @return
	 */
	ServerResponse saveProcessDraft(DhDrafts dhDrafts);
}
