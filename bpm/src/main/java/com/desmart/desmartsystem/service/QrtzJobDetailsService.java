package com.desmart.desmartsystem.service;

import com.desmart.desmartsystem.entity.QrtzJobDetails;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-07-31
 */
public interface QrtzJobDetailsService{
	
	/**
	 * 查询根据 qrtzJobDetails
	 * @param qrtzJobDetails
	 * @return  QrtzJobDetails
	 */
    QrtzJobDetails select(QrtzJobDetails qrtzJobDetails);
}
