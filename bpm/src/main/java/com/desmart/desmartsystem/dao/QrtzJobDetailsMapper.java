package com.desmart.desmartsystem.dao;

import com.desmart.desmartsystem.entity.QrtzJobDetails;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author xsf
 * @since 2018-07-31
 */
public interface QrtzJobDetailsMapper{
	
	/**
	 * 查询根据 qrtzJobDetails
	 * @param qrtzJobDetails
	 * @return  QrtzJobDetails
	 */
    QrtzJobDetails select(QrtzJobDetails qrtzJobDetails);
    
}
