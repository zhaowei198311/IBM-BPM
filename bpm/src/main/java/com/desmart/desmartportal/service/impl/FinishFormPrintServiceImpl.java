package com.desmart.desmartportal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.dao.FinishFormPrintMapper;
import com.desmart.desmartportal.service.FinishFormPrintService;

@Service
public class FinishFormPrintServiceImpl implements FinishFormPrintService{
	@Autowired
	private FinishFormPrintMapper finishFormPrintMapper;

	@Override
	public ServerResponse saveFormPrintContent(String formUid, String webpage) {
		int countRow = finishFormPrintMapper.saveFormPrintContent(formUid,webpage);
		if(countRow!=1) {
			return ServerResponse.createByError();
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse queryPrintContentByFormUid(String formUid) {
		String webpage = finishFormPrintMapper.queryPrintContentByFormUid(formUid);
		if("".equals(webpage) || null == webpage) {
			return ServerResponse.createByErrorMessage("打印预览出错");
		}
		return ServerResponse.createBySuccess(webpage);
	}

	@Override
	public int deletePrintContentByFormUid(String formUid) {
		return finishFormPrintMapper.deletePrintContentByFormUid(formUid);
	}
}
