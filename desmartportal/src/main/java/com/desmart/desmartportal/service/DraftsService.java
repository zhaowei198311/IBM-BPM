/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartportal.entity.Drafts;
import com.desmart.desmartportal.common.ServerResponse;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 草稿箱接口</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月28日  
*/
public interface DraftsService {
	
	int deleteDraftsBydfsId(String dfsId);
	
	ServerResponse<PageInfo<List<Drafts>>> selectDraftsList(Integer pageNum, Integer pageSize);
	
	ServerResponse<PageInfo<List<Drafts>>> selectDraftsBydfsTitle(String title, Integer pageNum, Integer pageSize);
	
	int saveDrafts(Drafts drafts);
}
