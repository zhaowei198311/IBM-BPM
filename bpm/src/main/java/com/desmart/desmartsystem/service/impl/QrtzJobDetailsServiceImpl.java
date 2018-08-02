package com.desmart.desmartsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.dao.QrtzJobDetailsMapper;
import com.desmart.desmartsystem.entity.QrtzJobDetails;
import com.desmart.desmartsystem.service.QrtzJobDetailsService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-07-31
 */
@Service
public class QrtzJobDetailsServiceImpl  implements QrtzJobDetailsService {

	
	@Autowired 
	private QrtzJobDetailsMapper qrtzJobDetailsMapper;

	@Override
	public QrtzJobDetails select(QrtzJobDetails qrtzJobDetails) {
		// TODO Auto-generated method stub
		return qrtzJobDetailsMapper.select(qrtzJobDetails);
	}
	
}
