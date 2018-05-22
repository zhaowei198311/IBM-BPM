package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.enums.DhActivityAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartportal.service.MenusService;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.util.ArrayUtil;

@Service
public class MenusServiceImpl implements MenusService {

	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;

	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysTeamMemberMapper sysTeamMemberMapper;

	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;

	@Autowired
	private DhActivityAssignMapper dhActivityAssignMapper;
	
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;

	
	@Override
	public List<BpmActivityMeta> activityHandler(String proUid, String proAppId, String verUid) {
		// TODO Auto-generated method stub
		
		//获得流程定义的第一个人工活动环节
		ServerResponse<BpmActivityMeta> bpmActivityMeta=dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId, proUid, verUid);
		
		//获取下一个环节
		Map<String, Object> activtyMap = bpmActivityMetaService.getNextToActivity(bpmActivityMeta.getData(), "");
		List<BpmActivityMeta> activityMetaList = new ArrayList<BpmActivityMeta>();
		List<BpmActivityMeta> normal = (List<BpmActivityMeta>) activtyMap.get("normal");
		List<BpmActivityMeta> gateAnd = (List<BpmActivityMeta>) activtyMap.get("gateAnd");
		activityMetaList.addAll(gateAnd);
		activityMetaList.addAll(normal);
		// 环节配置获取
		for (BpmActivityMeta activityMeta : activityMetaList) {
			DhActivityConf dhActivityConf = activityMeta.getDhActivityConf();
			String actcAssignType = dhActivityConf.getActcAssignType();
			DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
			String activityId = dhActivityConf.getActivityId();
			if (assignTypeEnum == null) {
				System.out.println("处理人类型不符合要求");
			}
			if (assignTypeEnum == DhActivityConfAssignType.NONE) {
				// return ServerResponse.createBySuccess();
			}
			DhActivityAssign selective = new DhActivityAssign();
			selective.setActivityId(activityId);
			selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
			List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
			if (assignList.size() == 0) {
				// return ServerResponse.createByErrorMessage("缺少处理人信息");
				System.out.println("缺少处理人信息");
			}
			List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList); // 角色或部门

			String userUid = "";
			String userName = "";

			switch (assignTypeEnum) {
			case ROLE:
			case ROLE_AND_DEPARTMENT:
			case ROLE_AND_COMPANY:
				SysRoleUser roleUser = new SysRoleUser();
				roleUser.setRoleUid(ArrayUtil.toArrayString(idList));
				List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByRoleUser(roleUser);
				for (SysRoleUser sysRoleUser : roleUsers) {
					userUid += sysRoleUser.getUserUid() + ";";
					userName += sysRoleUser.getUserName() + ";";
				}
				break;
			case TEAM:
			case TEAM_AND_DEPARTMENT:
			case TEAM_AND_COMPANY:
				SysTeamMember sysTeamMember = new SysTeamMember();
				sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));

				List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
				for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
					userUid += sysTeamMember2.getUserUid() + ";";
					userName += sysTeamMember2.getUserName() + ";";
				}

				break;
			case LEADER_OF_PRE_ACTIVITY_USER:

				break;
			case USERS:
				List<SysUser> users = sysUserMapper.listByPrimaryKeyList(idList);
				for (SysUser sysUser : users) {
					userUid += sysUser.getUserUid() + ";";
					userName += sysUser.getUserName() + ";";
				}
				break;
			case PROCESS_CREATOR:

				break;
			case BY_FIELD:
				if (assignList.size() > 0) {
					String field = assignList.get(0).getActaAssignId();
					dhActivityConf.setHandleField(field);
				}
				break;
			default:
				break;
			}
			activityMeta.setUserUid(userUid);
			activityMeta.setUserName(userName);
		}
		return activityMetaList;
	}


	
	@Override
	public List<SysUser> choosableHandler(String activityUid) {
		List<SysUser> userList=new ArrayList<SysUser>();
		BpmActivityMeta bpmActivityMeta=new BpmActivityMeta();
		bpmActivityMeta.setActivityId(activityUid);
		List<BpmActivityMeta> bpmActivityMetas = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
		
		DhActivityConf dhActivityConf=new DhActivityConf();
		if(bpmActivityMetas!=null){
			dhActivityConf=bpmActivityMetas.get(0).getDhActivityConf();
		}
		
		  String actcAssignType = dhActivityConf.getActcAssignType();
          DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
          String activityId = dhActivityConf.getActivityId();
          if (assignTypeEnum == null) {
        	  System.out.println("处理人类型不符合要求");
        	  return userList;
          }
          if (assignTypeEnum == DhActivityConfAssignType.NONE) {
              //return ServerResponse.createBySuccess();
        	  return userList;
          }
          DhActivityAssign selective = new DhActivityAssign();
          selective.setActivityId(activityId);
          System.err.println(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
          selective.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode()); 
          List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
          if (assignList.size() == 0) {
              //return ServerResponse.createByErrorMessage("缺少处理人信息");
        	  System.out.println("缺少处理人信息");
        	  return userList;
          }
          List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList); //角色或部门
          
          String userUid="";
          String userName="";
          
          switch (assignTypeEnum) {
          case ROLE:
          case ROLE_AND_DEPARTMENT:
          case ROLE_AND_COMPANY:
        	  SysRoleUser roleUser= new SysRoleUser();
        	  roleUser.setRoleUid(ArrayUtil.toArrayString(idList));
        	  List<SysRoleUser> roleUsers=sysRoleUserMapper.selectByRoleUser(roleUser);
        	  for (SysRoleUser sysRoleUser : roleUsers) {
        		  userUid+=sysRoleUser.getUserUid()+";";
        		  userName+=sysRoleUser.getUserName()+";";
        	  }
              break;
          case TEAM:
          case TEAM_AND_DEPARTMENT:
          case TEAM_AND_COMPANY:
        	  SysTeamMember sysTeamMember=new SysTeamMember();
        	  sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));
        	  List<SysTeamMember>  sysTeamMembers=sysTeamMemberMapper.selectTeamUser(sysTeamMember);
        	  for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
        		  userUid+=sysTeamMember2.getUserUid()+";";
        		  userName+=sysTeamMember2.getUserName()+";";
			  }
              break;
          case LEADER_OF_PRE_ACTIVITY_USER:
              
              break;
          case USERS:
        	  List<SysUser> users=sysUserMapper.listByPrimaryKeyList(idList);
        	  for (SysUser sysUser : users) {
        		  userUid+=sysUser.getUserUid();
        		  userName+=sysUser.getUserName();
        	  }
              break;
          case PROCESS_CREATOR:
              
              break;
          case BY_FIELD:
              if (assignList.size() > 0) {
                  String field = assignList.get(0).getActaAssignId();
                  dhActivityConf.setHandleField(field);
              }
              break;
          default:
              break; 
          }
          
          if(StringUtils.isNotBlank(userUid)) {
        	  String [] userUids =	userUid.split(";");
        	  String [] userNames =	userName.split(";");
        	  for (int i = 0; i < userUids.length; i++) {
        		  SysUser sysUser=new SysUser();
        		  sysUser.setUserUid(userUids[i]);
        		  sysUser.setUserName(userNames[i]);
        		  userList.add(sysUser);
			  }
          }
		return userList;
	}



	@Override
	public List<BpmActivityMeta> backlogActivityHandler(BpmActivityMeta bpmActivityMeta) {
		//获取下一个环节
		Map<String, Object> activtyMap = bpmActivityMetaService.getNextToActivity(bpmActivityMeta, "");
		List<BpmActivityMeta> activityMetaList = new ArrayList<BpmActivityMeta>();
		List<BpmActivityMeta> normal = (List<BpmActivityMeta>) activtyMap.get("normal");
		List<BpmActivityMeta> gateAnd = (List<BpmActivityMeta>) activtyMap.get("gateAnd");
		activityMetaList.addAll(gateAnd);
		activityMetaList.addAll(normal);
		// 环节配置获取
		for (BpmActivityMeta activityMeta : activityMetaList) {
			DhActivityConf dhActivityConf = activityMeta.getDhActivityConf();
			String actcAssignType = dhActivityConf.getActcAssignType();
			DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
			String activityId = dhActivityConf.getActivityId();
			if (assignTypeEnum == null) {
				System.out.println("处理人类型不符合要求");
			}
			if (assignTypeEnum == DhActivityConfAssignType.NONE) {
				// return ServerResponse.createBySuccess();
			}
			DhActivityAssign selective = new DhActivityAssign();
			selective.setActivityId(activityId);
			selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
			List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
			if (assignList.size() == 0) {
				// return ServerResponse.createByErrorMessage("缺少处理人信息");
				System.out.println("缺少处理人信息");
			}
			List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList); // 角色或部门

			String userUid = "";
			String userName = "";

			switch (assignTypeEnum) {
			case ROLE:
			case ROLE_AND_DEPARTMENT:
			case ROLE_AND_COMPANY:
				SysRoleUser roleUser = new SysRoleUser();
				roleUser.setRoleUid(ArrayUtil.toArrayString(idList));
				List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByRoleUser(roleUser);
				for (SysRoleUser sysRoleUser : roleUsers) {
					userUid += sysRoleUser.getUserUid() + ";";
					userName += sysRoleUser.getUserName() + ";";
				}
				break;
			case TEAM:
			case TEAM_AND_DEPARTMENT:
			case TEAM_AND_COMPANY:
				SysTeamMember sysTeamMember = new SysTeamMember();
				sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));

				List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
				for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
					userUid += sysTeamMember2.getUserUid() + ";";
					userName += sysTeamMember2.getUserName() + ";";
				}

				break;
			case LEADER_OF_PRE_ACTIVITY_USER:

				break;
			case USERS:
				List<SysUser> users = sysUserMapper.listByPrimaryKeyList(idList);
				for (SysUser sysUser : users) {
					userUid += sysUser.getUserUid() + ";";
					userName += sysUser.getUserName() + ";";
				}
				break;
			case PROCESS_CREATOR:

				break;
			case BY_FIELD:
				if (assignList.size() > 0) {
					String field = assignList.get(0).getActaAssignId();
					dhActivityConf.setHandleField(field);
				}
				break;
			default:
				break;
			}
			activityMeta.setUserUid(userUid);
			activityMeta.setUserName(userName);
		}
		return activityMetaList;
	}

}
