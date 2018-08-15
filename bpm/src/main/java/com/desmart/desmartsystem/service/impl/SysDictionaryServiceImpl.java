package com.desmart.desmartsystem.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.excelForm.SysDictionaryDataForm;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.ExcelUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.util.UUIDTool;
import com.desmart.desmartsystem.dao.SysDictionaryMapper;
import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.entity.SysDictionaryData;
import com.desmart.desmartsystem.service.SysDictionaryService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @ClassName: SysDictionaryServiceImpl  
 * @Description: 数据字典业务逻辑处理  
 * @author WUZHUANG  
 * @date 2018年5月3日  
 *
 */
@Service
public class SysDictionaryServiceImpl implements SysDictionaryService{
	
	@Autowired
	private SysDictionaryMapper sysDictionaryMapper;

	@Override
	public PagedResult<SysDictionary> listSysDictionary(SysDictionary sysDictionary, Integer pageNo, Integer pageSize) {
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);
		return BeanUtil.toPagedResult(sysDictionaryMapper.selectSysDictionaryByName(sysDictionary));
	}

	@Override
	public int save(SysDictionary sysDictionary) {
		// 验证，dicCode唯一
		List<SysDictionary> checkList = sysDictionaryMapper.selectSysDictionaryByCode(sysDictionary);
		if (checkList.size() > 0) {
			return -1;
		}
		sysDictionary.setDicUid("dic"+UUID.randomUUID().toString());
		return sysDictionaryMapper.save(sysDictionary);
	}

	@Override
	public int delete(String dicUid) {
		sysDictionaryMapper.delete(dicUid);
		return sysDictionaryMapper.deleteBydicUid(dicUid);
	}

	@Override
	public int update(SysDictionary sysDictionary) {
		// 验证，dicCode唯一
		List<SysDictionary> checkList = sysDictionaryMapper.selectSysDictionaryByCode(sysDictionary);
		SysDictionary checkSysDictionary = sysDictionaryMapper.selectSysDictionaryById(sysDictionary.getDicUid());
		if (checkSysDictionary.getDicCode().equals(sysDictionary.getDicCode())) {
			return sysDictionaryMapper.update(sysDictionary);
		}else {
			if (checkList.size() > 0) {
				return -1;
			}else {
				return sysDictionaryMapper.update(sysDictionary);
			}
		}			
	}

	@Override
	public SysDictionary getSysDictionaryById(String dicUid) {
		return sysDictionaryMapper.selectSysDictionaryById(dicUid);
	}

	@Override
	public PagedResult<SysDictionaryData> listSysDictionaryDataById(SysDictionaryData sysDictionaryData, Integer pageNo, Integer pageSize) {
		try {
			pageNo = pageNo == null?1:pageNo;
			pageSize = pageSize == null?10:pageSize;
			PageHelper.startPage(pageNo,pageSize);
			return BeanUtil.toPagedResult(sysDictionaryMapper.selectSysDictionaryDataListById(sysDictionaryData));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SysDictionaryData getSysDictionaryDataById(String dicDataUid) {
		return sysDictionaryMapper.selectSysDictionaryDataById(dicDataUid);
	}

	@Override
	public int saveSysDictionaryData(SysDictionaryData sysDictionaryData) {
		// 验证，dicDataCode唯一
		List<SysDictionaryData> checkList = sysDictionaryMapper.selectSysDictionaryDataByCode(sysDictionaryData);
		if (checkList.size() > 0) {
			return -1;
		}
		sysDictionaryData.setDicDataUid("dic_data"+UUID.randomUUID().toString());
		return sysDictionaryMapper.saveSysDictionaryData(sysDictionaryData);
	}

	@Override
	public int deleteSysDictionaryData(String dicDataUid) {
		return sysDictionaryMapper.deleteSysDictionaryData(dicDataUid);
	}

	@Override
	public int updateSysDictionaryData(SysDictionaryData sysDictionaryData) {
		// 验证，dicDataCode唯一
		List<SysDictionaryData> checkList = sysDictionaryMapper.selectSysDictionaryDataByCode(sysDictionaryData);
		SysDictionaryData checkSysDictionaryData = sysDictionaryMapper.selectSysDictionaryDataById(sysDictionaryData.getDicDataUid());
		if (checkSysDictionaryData.getDicDataCode().equals(sysDictionaryData.getDicDataCode())) {
			return sysDictionaryMapper.updateSysDictionaryData(sysDictionaryData);
		}else {
			if (checkList.size() > 0) {
				return -1;
			}else {
				return sysDictionaryMapper.updateSysDictionaryData(sysDictionaryData);
			}
		}	
	}

	@Override
	public ServerResponse listAllOnSysDictitonary(String dicName) {
		List<SysDictionary> dicList = sysDictionaryMapper.listAllOnSysDictitonary(dicName);
		return ServerResponse.createBySuccess(dicList);
	}

	@Override
	public ServerResponse listOnDicDataBydicUid(String dicUid,String dicDataName) {
		List<SysDictionaryData> dicDataList = sysDictionaryMapper.listOnDicDataBydicUid(dicUid,dicDataName);
		return ServerResponse.createBySuccess(dicDataList);
	}

	@Override
	public ServerResponse getOnSysDictionaryList(Integer pageNum,Integer pageSize,String dicName) {
		PageHelper.startPage(pageNum,pageSize);
		List<SysDictionary> dicList = sysDictionaryMapper.listAllOnSysDictitonary(dicName);
		PageInfo<List<SysDictionary>> pageInfo = new PageInfo(dicList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse getOnSysDictionaryDataList(Integer pageNum,Integer pageSize,String dicUid) {
		PageHelper.startPage(pageNum,pageSize);
		List<SysDictionaryData> dicList = sysDictionaryMapper.listOnDicDataBydicUid(dicUid,null);
		PageInfo<List<SysDictionaryData>> pageInfo = new PageInfo(dicList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse listOnDicDataBydicUidMove(String dicUid, String dicDataUid, String condition) {
		List<SysDictionaryData> dicDataList = sysDictionaryMapper.listOnDicDataBydicUidMove(dicUid,dicDataUid,condition);
		return ServerResponse.createBySuccess(dicDataList);
	}

	@Override
	public ServerResponse<?> deleteSysDictionaryDataList(String[] dicDataUidArr) {
		for(String dicDataUid:dicDataUidArr) {
			int rowCount = sysDictionaryMapper.deleteSysDictionaryData(dicDataUid);
			if(rowCount!=1) {
				throw new PlatformException("删除数据字典数据失败");
			}
		}
		return ServerResponse.createBySuccess();
	}
	

	@Override
	@Transactional
	public ServerResponse importSysDictionaryData(MultipartFile multipartFile) {
		//判断是否已经上传了文件数据
		ExcelUtil excelUtil = ExcelUtil.getInstance();
		if (excelUtil.checkExcelTitleAndSort(multipartFile, SysDictionaryDataForm.class,0)) {
			ServerResponse serverResponse = excelUtil.checkExcelContent(multipartFile, SysDictionaryDataForm.class,0);
			if (serverResponse.isSuccess()) {
				String myFileName = multipartFile.getOriginalFilename();
				if (myFileName.trim() != "") {
						// 读取数据
						ServerResponse<Sheet> serverResponse2 = excelUtil.loadSheet(multipartFile);
						if(!serverResponse2.isSuccess()) {
							return serverResponse2;
						}
						List<SysDictionaryDataForm> dictionaryDataFormList;
						try {
							dictionaryDataFormList = excelUtil
										.importExcelToEntity(serverResponse2.getData(), SysDictionaryDataForm.class,0);
						} catch (Exception e) {
							return ServerResponse.createByErrorMessage("导入数据失败,数据转换异常。");
						}
						
						//验证数据字典代码是否有重复
						Boolean flag = true;
						for (SysDictionaryDataForm sysDictionaryDataForm : dictionaryDataFormList) {
							Integer count = 0;
							String key = sysDictionaryDataForm.getDicDataCode();
							for (SysDictionaryDataForm sysDictionaryDataForm2 : dictionaryDataFormList) {
								if(key!=null&&key.equals(sysDictionaryDataForm2.getDicDataCode())) {
									count++;
								}
							}
							if(count>1) {
								flag = false;
								break;
							}
						}
						if(flag == false) {
							return ServerResponse.createByErrorMessage("导入数据失败,数据字典代码不能重复。");
						}
						
						List<SysDictionaryDataForm> checkDataCodeList = sysDictionaryMapper.selectByDataCodeBatch(dictionaryDataFormList);
						if(checkDataCodeList!=null&&checkDataCodeList.size()>0) {
							StringBuffer mStringBuffer = new StringBuffer();
							for (SysDictionaryDataForm sysDictionaryDataForm : checkDataCodeList) {
								mStringBuffer.append(sysDictionaryDataForm.getDicDataCode()+",");
							}
							return ServerResponse.createByErrorMessage("导入数据失败,数据字典代码:"
							+mStringBuffer.substring(0, mStringBuffer.length()-1)+"不能重复。");
						}
						
						String creator = (String) SecurityUtils.getSubject().getSession()
								.getAttribute(Const.CURRENT_USER);
						//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						//String currTime = simpleDateFormat.format(new Date());
						//Date currentDate = DateUtil.format(new Date());
						
						SysDictionary sysDictionary = new SysDictionary();
						List<SysDictionaryData> insertList = new ArrayList<>();
						for (int i=0;i<dictionaryDataFormList.size();i++) {
							sysDictionary.setDicCode(dictionaryDataFormList.get(i).getDicCode());
							List<SysDictionary> checkList = sysDictionaryMapper.selectSysDictionaryByCode(sysDictionary);
							if (checkList!=null&&checkList.size() > 0) {
								SysDictionaryData sysDictionaryData = new SysDictionaryData();
								String uuId = UUIDTool.getUUID();
								sysDictionaryData.setDicDataUid("dic_data"+uuId);
								sysDictionaryData.setDicUid(checkList.get(0).getDicUid());
								sysDictionaryData.setDicDataName(dictionaryDataFormList.get(i).getDicDataName());
								//sysDictionaryData.setDicDataDescription(dictionaryDataFormList.get(i).getDicDataDescription());
								sysDictionaryData.setDicDataSort(dictionaryDataFormList.get(i).getDicDataSort());
								sysDictionaryData.setDicDataCode(dictionaryDataFormList.get(i).getDicDataCode());
								sysDictionaryData.setCreateBy(creator);
								//sysDictionaryData.setCreateDate(currentDate);
								sysDictionaryData.setDicDataStatus("on");
								insertList.add(sysDictionaryData);
							}else {
								throw new PlatformException("导入数据失败,第"+(i+1)+"行数据字典类型不存在");
							}
						}
						sysDictionaryMapper.insertByBatch(insertList);
						return ServerResponse.createBySuccessMessage("导入数据成功！共"+insertList.size()+"成功");
					
				} else {
					return ServerResponse.createByErrorMessage("导入数据失败,文件不存在");
				}
			} else {
				return ServerResponse.createByErrorMessage("导入数据失败," + serverResponse.getMsg());
			}

		} else {
			return ServerResponse.createByErrorMessage("导入数据失败,数据读取异常");
		}
	}

}
