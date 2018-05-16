package com.desmart.desmartportal.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.desmart.desmartportal.dao.AccessoryFileUploadMapper;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.service.AccessoryFileUploadService;

@Service
public class AccessoryFileUploadServiceImpl implements AccessoryFileUploadService{

	@Resource
	private AccessoryFileUploadMapper accessoryFileuploadMapper;
	@Override
	public Integer insertDhInstanceDocuments(List<DhInstanceDocument> dhInstanceDocuments) {
		// TODO Auto-generated method stub
		return accessoryFileuploadMapper.insertDhInstanceDocuments(dhInstanceDocuments);
	}

	@Override
	public List<DhInstanceDocument> checkFileActivityIdByName(String appUid, String myFileName) {
		// TODO Auto-generated method stub
		return accessoryFileuploadMapper.checkFileActivityIdByName(appUid, myFileName);
	}

}