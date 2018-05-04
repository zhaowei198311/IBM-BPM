/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.desmart.desmartportal.controller.UserController;
import com.desmart.desmartportal.service.UserService;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.service.SysResourceService;
import com.desmart.desmartsystem.service.SysRoleUserService;
import com.desmart.desmartsystem.service.SysTeamMemberService;
import com.desmart.desmartsystem.service.SysUserService;

/**
 * <p>
 * Title: UserServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年5月3日
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SysRoleUserService sysRoleUserService;

	@Autowired
	private DhObjectPermissionService dhObjectPermissionService;

	@Autowired
	private SysTeamMemberService sysTeamMemberService;

	@Autowired
	private DhProcessMetaMapper dhProcessMetaDao;

	private Logger log = Logger.getLogger(UserController.class);

	@Override
	public List<Object> selectByMenusProcess() {
		log.info("判断---当前用户权限菜单 开始。。。");
		// 集合创建
		List<Object> menuList = new ArrayList<>();
		try {
			String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
			log.info("当前用户为" + user);
			// 根据用户id 去 查询 角色id
			SysRoleUser sysRoleUser = new SysRoleUser();
			sysRoleUser.setUserUid(user);
			List<SysRoleUser> result = sysRoleUserService.selectAll(sysRoleUser);
			for (SysRoleUser sysRoleUser2 : result) {
				log.info("用户id~~~" + sysRoleUser2.getUserUid());
				log.info("角色id~~~" + sysRoleUser2.getRoleUid());
				// 查询当前用户所在的角色组织
				String sysTeamMemberid = "";
				SysTeamMember sysTeamMember = new SysTeamMember();
				sysTeamMember.setUserUid(sysRoleUser2.getUserUid());
				List<SysTeamMember> sysTeamList = sysTeamMemberService.selectAll(sysTeamMember);
				for (SysTeamMember sysTeamMember2 : sysTeamList) {
					sysTeamMemberid = sysTeamMember2.getTeamUid();
					log.info("角色组织id~~~" + sysTeamMember2.getTeamUid());
				}
				// 根据用户的角色id查询 能发起的流程
				DhObjectPermission dhObjectPermission = new DhObjectPermission();
				dhObjectPermission.setOpParticipateUid(sysRoleUser2.getRoleUid()); // RoleUid
				List<DhObjectPermission> result2 = dhObjectPermissionService
						.getDhObjectPermissionInfo(dhObjectPermission);
				for (DhObjectPermission dhObjectPermission2 : result2) {
					String proAppId = dhObjectPermission2.getProAppId();
					String proUid = dhObjectPermission2.getProUid();
					DhProcessMeta dhProcessMeta = dhProcessMetaDao.queryByProAppIdAndProUid(proAppId, proUid);
					log.info("角色获取的流程:" + dhProcessMeta.getProName());
					menuList.add(dhProcessMeta.getProName());
				}
				// 根据用户id查询 能发起的流程
				DhObjectPermission dhObjectPermission2 = new DhObjectPermission();
				dhObjectPermission2.setOpParticipateUid(sysRoleUser2.getUserUid()); // UserUid
				List<DhObjectPermission> result3 = dhObjectPermissionService
						.getDhObjectPermissionInfo(dhObjectPermission2);
				for (DhObjectPermission dhObjectPermission3 : result3) {
					String proAppId = dhObjectPermission3.getProAppId();
					String proUid = dhObjectPermission3.getProUid();
					DhProcessMeta dhProcessMeta2 = dhProcessMetaDao.queryByProAppIdAndProUid(proAppId, proUid);
					log.info("该用户获取的流程:" + dhProcessMeta2.getProName());
					menuList.add(dhProcessMeta2.getProName());
				}
				// 根据用户角色组织查询 能发起的流程
				DhObjectPermission dhObjectPermission3 = new DhObjectPermission();
				dhObjectPermission3.setOpParticipateUid(sysTeamMemberid); // DepartUid
				List<DhObjectPermission> result4 = dhObjectPermissionService
						.getDhObjectPermissionInfo(dhObjectPermission3);
				for (DhObjectPermission dhObjectPermission4 : result4) {
					String proAppId = dhObjectPermission4.getProAppId();
					String proUid = dhObjectPermission4.getProUid();
					DhProcessMeta dhProcessMeta3 = dhProcessMetaDao.queryByProAppIdAndProUid(proAppId, proUid);
					log.info("用户所在的角色组获取的流程:" + dhProcessMeta3.getProName());
					menuList.add(dhProcessMeta3.getProName());
				}
			}
			// 去掉重复的流程
			for (int i = 0; i < menuList.size() - 1; i++) {
				for (int j = menuList.size() - 1; j > i; j--) {
					if (menuList.get(j).equals(menuList.get(i))) {
						menuList.remove(j);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("判断---当前用户权限菜单 结束。。。");
		}
		return menuList;
	}

}
