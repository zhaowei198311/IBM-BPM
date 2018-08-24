package com.desmart.desmartsystem.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.dao.SysRoleMapper;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserDepartmentMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysRole;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.desmart.desmartsystem.service.SysUserService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
public class SysUserServiceImpl implements SysUserService {
	private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
	
	@Autowired
	SysUserMapper sysUserMapper;
	@Autowired
	SysUserDepartmentService sysUserDepartmentService;
	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;
	@Autowired
	private SysTeamMemberMapper sysTeamMemberMapper;
	@Autowired
	private SysUserDepartmentMapper sysUserDepartmentMapper;
	@Autowired
	private SysDepartmentMapper sysDepartmentMapper;
	@Autowired
    private SysRoleMapper sysRoleMapper;

	
	@Override
	public int insert(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserMapper.insert(entity);
	}

	@Override
	public int update(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserMapper.update(entity);
	} 

	@Override
	public int delete(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		SysUserDepartment userDepartment=new SysUserDepartment();
		userDepartment.setUserUid(entity.getUserUid());
		sysUserDepartmentService.delete(userDepartment);
		
		return sysUserMapper.delete(entity);
	}

	@Override 
	public SysUser select(SysUser entity) {
		// TODO Auto-generated method stub
		return sysUserMapper.select(entity);
	}

	@Override
	public SysUser findById(SysUser id) {
		// TODO Auto-generated method stub
		return sysUserMapper.findById(id);
	}

	@Override
	public PagedResult<SysUser> queryByPage(SysUser entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysUserMapper.selectAll(entity));
	}

	@Override
	public List<SysUser> selectAll(SysUser entity) {
		// TODO Auto-generated method stub
		return sysUserMapper.selectAll(entity);
	}

	@Override
	public List<SysUser> login(String username, String password) {
		List<SysUser> userList = sysUserMapper.login(username, password);
		return userList;
	}

	@Override
	public ServerResponse querySysUserByConditionPC(String condition) {
		List<SysUser> userList = sysUserMapper.querySysUserByConditionPC(condition);
		return ServerResponse.createBySuccess(userList);
	}
	
	@Override
	public ServerResponse allSysUserMove(String userUidArrStr, Integer pageNo, Integer pageSize, String condition) {
		PageHelper.startPage(pageNo,pageSize);
		String[] userUidArr = userUidArrStr.split(";");
		if(userUidArr.length==1 && (null==userUidArr[0]||userUidArr[0].equals(""))) {
			userUidArr = null;
		}
		List<SysUser> userList = sysUserMapper.allSysUserMove(userUidArr,condition);
		PageInfo<List<SysUser>> info = new PageInfo(userList);
		return ServerResponse.createBySuccess(info);
	}

	@Override
	public List<SysUser> searchByUserUids(Collection<String> userUids) {
		if (CollectionUtils.isEmpty(userUids)) {
			return new ArrayList<>();
		}
		if (userUids instanceof Set) {
            return sysUserMapper.listByPrimaryKeyList(userUids);
        } else {
            Set<String> userUidSet = new HashSet<>();
            userUidSet.addAll(userUids);
            return sysUserMapper.listByPrimaryKeyList(userUids);
        }
	}

	@Override
	public List<SysUser> searchByRoleUidList(List<String> roleUidList) {
	    List<SysUser> result = new ArrayList<>();
		if(CollectionUtils.isEmpty(roleUidList)) {//查询条件集合为空，则直接返回空集合
			return result;
		}
        List<SysRoleUser> sysRoleUsers = sysRoleUserMapper.listByRoleUidsWithStation(roleUidList);
		List<String> roleUidsNeedSearch = null; // 需要从SYS_ROLE中查STATION的数据
        if (sysRoleUsers.isEmpty()) {
            roleUidsNeedSearch = roleUidList;
        } else {
            roleUidsNeedSearch = roleUidsNeedSearch(roleUidList, sysRoleUsers);
        }
        Set<String> userUidSet = extractUserUidsFromSysRoleUsers(sysRoleUsers);
        Set<String> stationSet = extractStationsFromSysRoleUsers(sysRoleUsers);
        if (!roleUidsNeedSearch.isEmpty()) {
            List<SysRole> sysRoles = sysRoleMapper.listByPrimaryKeyList(roleUidsNeedSearch);
            stationSet.addAll(extractStationsFromSysRoles(sysRoles));
        }

        if (CollectionUtils.isEmpty(userUidSet) && CollectionUtils.isEmpty(stationSet)) {
            return result;
        }
        result = sysUserMapper.listByUserUidsOrStations(userUidSet, stationSet);
        return result;
	}

	private List<String> roleUidsNeedSearch(List<String> roleUidList, List<SysRoleUser> sysRoleUsers) {
	    List<String> copyedRoleUids = new ArrayList<>();
        for (String roleUid : roleUidList) {
            copyedRoleUids.add(roleUid);
        }
        Set<String> containedRoleUids = new HashSet<>();
        for (SysRoleUser sysRoleUser : sysRoleUsers) {
            containedRoleUids.add(sysRoleUser.getRoleUid());
        }
        Iterator<String> iterator = copyedRoleUids.iterator();
        while (iterator.hasNext()) {
            String roleUid = iterator.next();
            if (containedRoleUids.contains(roleUid)) {
                iterator.remove();
            }
        }
        return copyedRoleUids;
    }

    private Set<String> extractUserUidsFromSysRoleUsers(List<SysRoleUser> sysRoleUsers) {
        Set<String> result = new HashSet<>();
        for (SysRoleUser sysRoleUser : sysRoleUsers) {
            result.add(sysRoleUser.getUserUid());
        }
        return result;
    }

	private Set<String> extractStationsFromSysRoleUsers(List<SysRoleUser> sysRoleUsers) {
        Set<String> result = new HashSet<>();
	    if (CollectionUtils.isEmpty(sysRoleUsers)) {
            return result;
        }
        StringBuilder builder = new StringBuilder();
        for (SysRoleUser sysRoleUser : sysRoleUsers) {
            if (StringUtils.isNotBlank(sysRoleUser.getStation())) {
                builder.append(sysRoleUser.getStation());
                if (!sysRoleUser.getStation().endsWith(";")) {
                    builder.append(";");
                }
            }
        }
        if (builder.length() == 0) {
            return result;
        }

        String[] stationArr = builder.toString().split(";");
        for (String station : stationArr) {
            if (StringUtils.isNotBlank(station)) {
                result.add(station);
            }
        }
        return result;
    }

    private Set<String> extractStationsFromSysRoles(List<SysRole> sysRoles) {
        Set<String> result = new HashSet<>();
	    if (CollectionUtils.isEmpty(sysRoles)) {
            return result;
        }
        StringBuilder builder = new StringBuilder();
        for (SysRole sysRole : sysRoles) {
            if (StringUtils.isNotBlank(sysRole.getStation())) {
                builder.append(sysRole.getStation());
                if (!sysRole.getStation().endsWith(";")) {
                    builder.append(";");
                }
            }
        }
        if (builder.length() == 0) {
            return result;
        }
        String[] stationArr = builder.toString().split(";");
        for (String station : stationArr) {
            if (StringUtils.isNotBlank(station)) {
                result.add(station);
            }
        }
        return result;
    }

	@Override
	public List<SysUser> searchByRoleUidListAndDepartment(List<String> roleUidList, String departNo) {
        List<SysUser> sysUsers = searchByRoleUidList(roleUidList);
        return filterUserByDepartNo(sysUsers, departNo);
	}

	private List<SysUser> filterUserByDepartNo(List<SysUser> sysUsers, String departNo) {
        if (StringUtils.isBlank(departNo) || CollectionUtils.isEmpty(sysUsers)) {
            return new ArrayList<>();
        }
        List<SysUserDepartment> sysUserDepartments = sysUserDepartmentMapper.listByUserUids(extractUserUidsFromSysUsers(sysUsers));
        assembleDepartNumberListToUserList(sysUsers, sysUserDepartments);
        List<String> relationDepartNumbers = relationDepartNumbers(departNo);
        Iterator<SysUser> iterator = sysUsers.iterator();
        while (iterator.hasNext()) {
            SysUser sysUser = iterator.next();
            if (!sysUser.isMemberOfRelationDepartments(relationDepartNumbers)) {
                iterator.remove();
            }
        }
        return sysUsers;
    }

    private List<String> relationDepartNumbers(String departNo) {
	    List<String> result = new ArrayList<>();
	    if (StringUtils.isBlank(departNo)) {
	        return result;
        }
	    SysDepartment sysDepartment = new SysDepartment();
	    sysDepartment.setDepartUid(departNo);
        List<SysDepartment> sysDepartments = sysDepartmentMapper.queryByConditionToParentTree(sysDepartment);
        if (CollectionUtils.isEmpty(sysDepartments)) {
            return result;
        }
        for (SysDepartment department : sysDepartments) {
            result.add(department.getDepartUid());
        }
        return result;
    }

    private void assembleDepartNumberListToUserList(List<SysUser> sysUsers, List<SysUserDepartment> sysUserDepartments) {
        Map<String, SysUser> userUidAndUserMap = new HashMap<>();
        for (SysUser sysUser : sysUsers) {
            userUidAndUserMap.put(sysUser.getUserUid(), sysUser);
        }
        for (SysUserDepartment data : sysUserDepartments) {
            if (userUidAndUserMap.get(data.getUserUid()) != null) {
                userUidAndUserMap.get(data.getUserUid()).addToDepartNumberList(data.getDepartUid());
            }
        }
    }

	@Override
	public List<SysUser> searchByRoleUidListAndCompany(List<String> roleUidList, String companyNum) {
        List<SysUser> sysUsers = searchByRoleUidList(roleUidList);
        return filterUserByCompanyNum(sysUsers, companyNum);
	}

	private List<SysUser> filterUserByCompanyNum(List<SysUser> sysUsers, String companyNum) {
	    if (StringUtils.isBlank(companyNum) || CollectionUtils.isEmpty(sysUsers)) {
            return new ArrayList<>();
        }
        List<SysUserDepartment> sysUserDepartments = sysUserDepartmentMapper.listByUserUids(extractUserUidsFromSysUsers(sysUsers));

        assembleCompayCodeListToUserList(sysUsers, sysUserDepartments);
        Iterator<SysUser> iterator = sysUsers.iterator();
        while (iterator.hasNext()) {
            SysUser sysUser = iterator.next();
            if (!sysUser.isMemberOfCompany(companyNum)) {
                iterator.remove();
            }
        }
        return sysUsers;
    }

    private void assembleCompayCodeListToUserList(List<SysUser> sysUsers, List<SysUserDepartment> sysUserDepartments) {
        Map<String, SysUser> userUidAndUserMap = new HashMap<>();
        for (SysUser sysUser : sysUsers) {
            userUidAndUserMap.put(sysUser.getUserUid(), sysUser);
        }
        for (SysUserDepartment data : sysUserDepartments) {
            if (userUidAndUserMap.get(data.getUserUid()) != null) {
                userUidAndUserMap.get(data.getUserUid()).addToCompanyNumberList(data.getCompanyCode());
            }
        }
    }

	private Set<String> extractUserUidsFromSysUsers(List<SysUser> sysUsers) {
        Set<String> result = new HashSet<>();
        if (CollectionUtils.isEmpty(sysUsers)) {
            return result;
        }
        for (SysUser sysUser : sysUsers) {
            result.add(sysUser.getUserUid());
        }
        return result;
    }

	@Override
	public List<SysUser> searchByTeamUidList(List<String> teamUidList) {
		if(CollectionUtils.isEmpty(teamUidList)) {//查询条件集合为空，则直接返回空集合
			return new ArrayList<>();
		}
        List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.listByTeamUids(teamUidList);
        if (CollectionUtils.isEmpty(sysTeamMembers)) {
            return new ArrayList<>();
        }
        List<String> roleUids = extractRoleUidsFromSysTeamMembers(sysTeamMembers);
        List<String> userUids = extractUserUidsFromSysTeamMembers(sysTeamMembers);
        List<SysUser> sysUsersFromRoleUids = null;
        List<SysUser> sysUsersFromUserUids = null;
        if (!CollectionUtils.isEmpty(roleUids)) {
            sysUsersFromRoleUids = searchByRoleUidList(roleUids);
        } else {
            sysUsersFromRoleUids = new ArrayList<>();
        }
        if (!CollectionUtils.isEmpty(userUids)) {
            sysUsersFromUserUids = searchByUserUids(userUids);
        } else {
            sysUsersFromUserUids = new ArrayList<>();
        }
        return combineUsers(sysUsersFromRoleUids, sysUsersFromUserUids);
	}

    /**
     * 合并两个集合中的人员，去重
     * @param sysUsersFromRoleUids
     * @param sysUsersFromUserUids
     * @return
     */
	private List<SysUser> combineUsers(List<SysUser> sysUsersFromRoleUids, List<SysUser> sysUsersFromUserUids) {
		Set<String> userUids = new HashSet<>();
		List<SysUser> result = new ArrayList<>();
        for (SysUser sysUser : sysUsersFromRoleUids) {
            if (userUids.add(sysUser.getUserUid())) {
                result.add(sysUser);
            }
        }
        for (SysUser sysUser : sysUsersFromUserUids) {
            if (userUids.add(sysUser.getUserUid())) {
                result.add(sysUser);
            }
        }
        return result;
    }

    private List<String> extractRoleUidsFromSysTeamMembers(List<SysTeamMember> sysTeamMembers) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(sysTeamMembers)) {
            return result;
        }
        Set<String> tempRoleUids = new HashSet<>();
        for (SysTeamMember sysTeamMember : sysTeamMembers) {
            if (SysTeamMember.TYPE_ROLE.equals(sysTeamMember.getMemberType())
                    && StringUtils.isNotBlank(sysTeamMember.getUserUid())) {
                tempRoleUids.add(sysTeamMember.getUserUid());
            }
        }
        result.addAll(tempRoleUids);
        return result;
    }

    private List<String> extractUserUidsFromSysTeamMembers(List<SysTeamMember> sysTeamMembers) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(sysTeamMembers)) {
            return result;
        }
        Set<String> tempUserUids = new HashSet<>();
        for (SysTeamMember sysTeamMember : sysTeamMembers) {
            if (SysTeamMember.TYPE_USER.equals(sysTeamMember.getMemberType())
                    && StringUtils.isNotBlank(sysTeamMember.getUserUid())) {
                tempUserUids.add(sysTeamMember.getUserUid());
            }
        }
        result.addAll(tempUserUids);
        return result;
    }

	@Override
	public List<SysUser> searchbyTeamUidListAndDepartment(List<String> teamUidList, String departNo) {
        List<SysUser> sysUsers = searchByTeamUidList(teamUidList);
        return filterUserByDepartNo(sysUsers, departNo);
	}

	@Override
	public List<SysUser> searchByTeamUidListAndCompany(List<String> teamUidList, String companyNum) {
        List<SysUser> sysUsers = searchByTeamUidList(teamUidList);
        return filterUserByCompanyNum(sysUsers, companyNum);
	}


	@Override
	public List<SysUser> searchByLeaderOfPreActivityUser(SysUser sysUser) {
		String departNo = sysUser.getDepartUid();
		SysUserDepartment selective = new SysUserDepartment();
		selective.setDepartUid(departNo);
		selective.setIsManager(SysUserDepartment.IS_MANAGER_TRUE);
		List<SysUserDepartment> departmentByManagerList = sysUserDepartmentMapper.selectAll(selective);//根据用户部门查询部门管理者集合
		List<String> userUidList = new ArrayList<>();//用来保存部门管理者的主键
		for (Iterator<SysUserDepartment> iterator = departmentByManagerList.iterator(); iterator.hasNext();) {
			SysUserDepartment sysUserDepartment = iterator.next();
			if(StringUtils.isBlank(sysUserDepartment.getUserUid())) {
				iterator.remove();//如果当前数据的用户的userUid不存在，则从列表中移除
			}else if(sysUser.getUserUid().equals(sysUserDepartment.getUserUid())){
				iterator.remove();//判断管理者Uid是不是当前用户，如果是则移除。
			}else {
				userUidList.add(sysUserDepartment.getUserUid());
			}
		}
		if (CollectionUtils.isEmpty(userUidList)) {
			//根据部门id查询其父部门id
			SysDepartment parentDepartment = sysDepartmentMapper.queryParentDepartByDepartId(departNo);
			sysUser.setDepartUid(parentDepartment.getDepartUid());
			return searchByLeaderOfPreActivityUser(sysUser);//继续递归查询上级部门的用户
		}
		List<SysUser> managerList = sysUserMapper.listByPrimaryKeyList(userUidList);//获取到根据userUid查询到的人员集合
		if(managerList != null && managerList.size() > 0) {//上级用户存在人，则直接返回
			return managerList;
		}else {
			//根据部门id查询其父部门id
			SysDepartment parentDepartment = sysDepartmentMapper.queryParentDepartByDepartId(departNo);
			sysUser.setDepartUid(parentDepartment.getDepartUid());
			return searchByLeaderOfPreActivityUser(sysUser);//继续递归查询上级部门的用户
		}
	}



	@Override
	public List<SysUser> searchByField(JSONObject mergedFormData, String field) {
		List<SysUser> sysUserList = new ArrayList<>();
		if (StringUtils.isBlank(field) || mergedFormData == null) {
			return sysUserList;
		}
		JSONObject fieldJson = mergedFormData.getJSONObject(field);
		if (fieldJson == null) {
			return sysUserList;
		}
		String idValue = fieldJson.getString("value");
		if (StringUtils.isBlank(idValue)) {
			return sysUserList;
		}
		String[] strArr = idValue.split(";");
		Set<String> tempValues = new HashSet<>();
		for (String str : strArr) {
			if (StringUtils.isNotBlank(str)) {
				tempValues.add(str.trim());
			}
		}
		if (tempValues.isEmpty()) {
			return sysUserList;
		} else {
			return sysUserMapper.listByPrimaryKeyList(tempValues);
		}
	}



	/**
	 * 将包含重复id的字符串转换为用户列表，并去除重复<br/>
	 * @param tempIdStr  uid1;uid2uid3;
	 * @return
	 */
	private List<SysUser> transformTempIdStrToUserList(String tempIdStr) {
		List<SysUser> resultList = null;
		if (StringUtils.isNotBlank(tempIdStr)) {
			// 去重
			List<String> tempList = new ArrayList<>();
			String[] temArr = tempIdStr.split(";");
			for (String str : temArr) {
				if (StringUtils.isNotBlank(str) && !tempList.contains(str)) {
					// 对id去重后添加
					tempList.add(str);
				}
			}
			if (!tempList.isEmpty()) {
				resultList = sysUserMapper.listByPrimaryKeyList(tempList);

			}
		}
		return resultList == null ? new ArrayList<>() : resultList;
	}
	
}
