/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhProcessCategoryMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartportal.controller.UserController;
import com.desmart.desmartportal.service.ProcessFormService;
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
	
	@Autowired
	private DhProcessCategoryMapper dhProcessCategoryDao;
	
	@Autowired
	private ProcessFormService processFormService;

	private Logger log = Logger.getLogger(UserController.class);

	@Override
	public List<Map<String, Object>> selectByMenusProcess() {
		log.info("判断---当前用户权限菜单 开始。。。");
		// 集合创建
		List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
		try {
			String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
			log.info("当前用户为" + user);
			// 根据用户id 去 查询 角色id,角色组id
			SysRoleUser sysRoleUser = new SysRoleUser();
			sysRoleUser.setUserUid(user);
			// 如果用户没有角色  根据用户查询
			// 根据用户id查询 能发起的流程
			DhObjectPermission dhObjectPermission2 = new DhObjectPermission();
			dhObjectPermission2.setOpParticipateUid(user); // UserUid
			List<DhObjectPermission> result3 = dhObjectPermissionService
					.getDhObjectPermissionInfo(dhObjectPermission2);
			for (DhObjectPermission dhObjectPermission3 : result3) {
				String proAppId = dhObjectPermission3.getProAppId();
				String proUid = dhObjectPermission3.getProUid();
				DhProcessMeta dhProcessMeta2 = dhProcessMetaDao.queryByProAppIdAndProUid(proAppId, proUid);
				log.info("该用户获取的流程:" + dhProcessMeta2.getProName());
				// 通过分类id查询流程分类
				DhProcessCategory dhProcessCategory2 = dhProcessCategoryDao.queryByCategoryUid(dhProcessMeta2.getCategoryUid());
				log.info("该流程分类:" + dhProcessCategory2.getCategoryName());
				Map<String, Object> map2 = new HashMap<>();
				map2.put("proAppId", dhProcessMeta2.getProAppId());
				map2.put("verUid", dhObjectPermission3.getProVerUid());
				map2.put("proUid", dhProcessMeta2.getProUid());
				map2.put("proName", dhProcessMeta2.getProName());
				map2.put("categoryUid", dhProcessCategory2.getCategoryUid());
				map2.put("categoryName", dhProcessCategory2.getCategoryName());
				map2.put("userId", user);
				infoList.add(map2);
			}
			
			List<SysRoleUser> result = sysRoleUserService.selectAll(sysRoleUser);
			for (SysRoleUser sysRoleUser2 : result) {
				log.info("用户id~~~" + user);
				log.info("角色id~~~" + sysRoleUser2.getRoleUid());
				// 定义角色组id
				String sysTeamMemberid = "";
				// 根据用户id 去找到 用户所在的角色组织
				SysTeamMember sysTeamMember = new SysTeamMember();
				sysTeamMember.setUserUid(sysRoleUser2.getUserUid());
				List<SysTeamMember> sysTeamList = sysTeamMemberService.selectAll(sysTeamMember);
				for (SysTeamMember sysTeamMember2 : sysTeamList) {
					// 获得角色组id  为了方便后续 根据角色组进行查询 发起流程的权限
					sysTeamMemberid = sysTeamMember2.getTeamUid();
					log.info("角色组织id~~~" + sysTeamMember2.getTeamUid());
				}			
				// 根据用户的角色id查询 能发起的流程 (DhObjectPermission 为关联表数据信息)
				DhObjectPermission dhObjectPermission = new DhObjectPermission();
				dhObjectPermission.setOpParticipateUid(sysRoleUser2.getRoleUid()); // RoleUid
				List<DhObjectPermission> result2 = dhObjectPermissionService
						.getDhObjectPermissionInfo(dhObjectPermission);
				// 获取该流程的  三个 必传id (流程库id，流程id，版本id)
				for (DhObjectPermission dhObjectPermission1 : result2) {
					String proAppId = dhObjectPermission1.getProAppId();
					String proUid = dhObjectPermission1.getProUid();
					// 通过id  去 查询流程元数据表里的 数据信息
					DhProcessMeta dhProcessMeta = dhProcessMetaDao.queryByProAppIdAndProUid(proAppId, proUid);
					log.info("角色获取的流程:" + dhProcessMeta.getProName());
					// 通过 查询元数据 获得的getCategoryUid 流程分类id  去查询流程分类
					DhProcessCategory dhProcessCategory = dhProcessCategoryDao.queryByCategoryUid(dhProcessMeta.getCategoryUid());
					log.info("该流程分类:" + dhProcessCategory.getCategoryName());
					// 把所有信息 归纳到 一个map里进行保存
					Map<String, Object> map1 = new HashMap<>();
					map1.put("proAppId", dhProcessMeta.getProAppId());
					map1.put("verUid", dhObjectPermission1.getProVerUid());
					map1.put("proUid", dhProcessMeta.getProUid());
					map1.put("proName", dhProcessMeta.getProName());
					map1.put("categoryUid", dhProcessCategory.getCategoryUid());
					map1.put("categoryName", dhProcessCategory.getCategoryName());
					infoList.add(map1);
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
					// 通过分类id查询流程分类
					DhProcessCategory dhProcessCategory3 = dhProcessCategoryDao.queryByCategoryUid(dhProcessMeta3.getCategoryUid());
					log.info("该流程分类:" + dhProcessCategory3.getCategoryName());
					Map<String, Object> map3 = new HashMap<>();
					map3.put("proAppId", dhProcessMeta3.getProAppId());
					map3.put("verUid", dhObjectPermission4.getProVerUid());
					map3.put("proUid", dhProcessMeta3.getProUid());
					map3.put("proName", dhProcessMeta3.getProName());
					map3.put("categoryUid", dhProcessCategory3.getCategoryUid());
					map3.put("categoryName", dhProcessCategory3.getCategoryName());
					infoList.add(map3);
				}
			}
			// 去掉重复的流程
			for (int i = 0; i < infoList.size() - 1; i++) {
				for (int j = infoList.size() - 1; j > i; j--) {
					if (infoList.get(j).equals(infoList.get(i))) {
						infoList.remove(j);
					}
				}
			}
			
			for (Map<String, Object> map : infoList) {
				for (Map.Entry<String, Object> m : map.entrySet()) {
					if(m.getValue().equals(m.getValue())) {
						
					}
	            }
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("判断---当前用户权限菜单 结束。。。");
		}
		return infoList;
	}

}
