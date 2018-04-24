package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmFormField;

public interface BpmFormFieldService {

	/**
	 * 保存表单字段数据
	 */
	ServerResponse saveFormField(BpmFormField[] fields);

}
