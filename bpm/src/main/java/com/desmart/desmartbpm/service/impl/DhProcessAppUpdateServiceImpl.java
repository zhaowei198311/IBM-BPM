package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.DateTimeUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.*;
import com.desmart.desmartbpm.enginedao.LswBpdMapper;
import com.desmart.desmartbpm.enginedao.LswProjectMapper;
import com.desmart.desmartbpm.enginedao.LswSnapshotMapper;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartbpm.entity.engine.LswBpd;
import com.desmart.desmartbpm.entity.engine.LswProject;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.mongo.CommonMongoDao;
import com.desmart.desmartbpm.mongo.ModelMongoDao;
import com.desmart.desmartbpm.service.*;
import com.desmart.desmartsystem.dao.DhInterfaceMapper;
import com.desmart.desmartsystem.dao.DhInterfaceParameterMapper;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import com.github.pagehelper.PageHelper;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class DhProcessAppUpdateServiceImpl implements DhProcessAppUpdateService {
    private static final Logger logger = LoggerFactory.getLogger(DhProcessAppUpdateServiceImpl.class);

    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private DhGatewayLineService dhGatewayLineService;
    @Autowired
    private BpmFormManageService bpmFormManageService;
    @Autowired
    private BpmFormRelePublicFormMapper bpmFormRelePublicFormMapper;
    @Autowired
    private DhActivityRejectMapper dhActivityRejectMapper;
    @Autowired
    private DhProcessDefinitionMapper dhProcessDefinitionMapper;
    @Autowired
    private DhGatewayLineMapper dhGatewayLineMapper;
    @Autowired
    private DatRuleMapper datRuleMapper;
    @Autowired
    private DatRuleConditionMapper datRuleConditionMapper;
    @Autowired
    private DhObjectPermissionMapper dhObjectPermissionMapper;
    @Autowired
    private BpmFormManageMapper bpmFormManageMapper;
    @Autowired
    private BpmFormFieldMapper bpmFormFieldMapper;
    @Autowired
    private DhTriggerMapper dhTriggerMapper;
    @Autowired
    private DhTriggerInterfaceMapper dhTriggerInterfaceMapper;
    @Autowired
    private ModelMongoDao modelMongoDao;
    @Autowired
    private LswBpdMapper lswBpdMapper;
    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    @Autowired
    private DhActivityAssignMapper dhActivityAssignMapper;
    @Autowired
    private DhStepMapper dhStepMapper;
    @Autowired
    private BpmExposedItemMapper bpmExposedItemMapper;
    @Autowired
    private LswProjectMapper lswProjectMapper;
    @Autowired
    private LswSnapshotMapper lswSnapshotMapper;
    @Autowired
    private CommonMongoDao commonMongoDao;


    @Override
    public ServerResponse<List<Map<String, String>>> getAllProcessApp() {
        List<Map<String, String>> result = new ArrayList<>();
        List<String> proAppIds = bpmExposedItemMapper.listDistinctProAppId();
        if (proAppIds.isEmpty()) {
            return ServerResponse.createBySuccess(result);
        }
        for (int i = 0; i < proAppIds.size(); i++) {
            String proAppId = proAppIds.get(i);
            proAppIds.set(i, proAppId.substring(proAppId.indexOf(".") + 1));
        }
        List<LswProject> lswProjects = lswProjectMapper.listByProjectIdList(proAppIds);
        for (LswProject lswProject : lswProjects) {
            Map<String, String> proApp = new HashMap<>();
            proApp.put("proAppId", "2066." + lswProject.getProjectId());
            proApp.put("proAppName", lswProject.getName());
            result.add(proApp);
        }
        return ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse<List<Map<String, String>>> findSynchronizedSnapshotByProAppId(String proAppId) {
        List<Map<String, String>> result = new ArrayList<>();
        if (StringUtils.isBlank(proAppId)) {
            return ServerResponse.createByErrorMessage("缺少应用库id");
        }
        List<String> proVerUids = dhProcessDefinitionMapper.listDistinctProVerUidByProAppId(proAppId);
        if (proVerUids.isEmpty()) {
            return ServerResponse.createBySuccess(result);
        }
        for (int i = 0; i < proVerUids.size(); i++) {
            String proVerUid = proVerUids.get(i);
            proVerUids.set(i, proVerUid.substring(proVerUid.indexOf(".") + 1));
        }

        List<LswSnapshot> snapshotList = lswSnapshotMapper.listBySnapshotIdList(proVerUids);
        for (LswSnapshot snapshot : snapshotList) {
            Map<String, String> map = new HashMap<>();
            map.put("snapshotId", "2064." + snapshot.getSnapshotId());
            map.put("snapshotName", snapshot.getName());
            map.put("createTime", DateTimeUtil.dateToStr(snapshot.getCreatedOn()));
            result.add(map);
        }

        return ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse<List<Map<String, String>>> findUnsynchronizedSnapshotByProAppId(String proAppId) {
        List<Map<String, String>> result = new ArrayList<>();
        if (StringUtils.isBlank(proAppId)) {
            return ServerResponse.createByErrorMessage("缺少应用库id");
        }
        List<LswSnapshot> snapshotList = bpmExposedItemMapper.listUnsynchronizedSnapshotByProAppId(proAppId);
        for (LswSnapshot snapshot : snapshotList) {
            Map<String, String> map = new HashMap<>();
            map.put("snapshotId", snapshot.getSnapshotId());
            map.put("snapshotName", snapshot.getName());
            map.put("createTime", DateTimeUtil.dateToStr(snapshot.getCreatedOn()));
            result.add(map);
        }
        return ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse<Queue<DhProcessDefinitionBo>> prepareData(String proAppId, String newProVerUid) {
        // 检查此应用库是否已经被同步过
        String updateIdentity = proAppId + "|" + newProVerUid; // 查看是否有更新记录
        String updateTime = commonMongoDao.getStringValue(updateIdentity, CommonMongoDao.APP_UPDATE_RECORD);
        if (updateTime != null) {
            return ServerResponse.createByErrorMessage("应用库此版本已经同步过，不能再次同步，同步时间：" + updateTime);
        }
        // 从BpmExposedItem中获得需要拉取的流程
        List<DhProcessDefinitionBo> exposedDefinitionList = dhProcessDefinitionService.getExposedProcessDefinitionByProAppIdAndSnapshotId(proAppId, newProVerUid);
        if (CollectionUtils.isEmpty(exposedDefinitionList)) {
            return ServerResponse.createByErrorMessage("应用库此版本没有公开的流程数据，请先同步公开的流程后再试");
        }
        DhProcessDefinitionBo bo = null;
        Queue<DhProcessDefinitionBo> boToPullQueue = new LinkedList<>();
        List<String> sortedBpdIdsEachTime = new ArrayList<>(); // 记录每次遍历后 被排序的bpdId
        List<JSONObject> allVisualModelList = new ArrayList<>();

        // 对所有的流程定义排序拉取
        // 获得所有流程定义的ProcessModel
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfigService.getFirstActConfig());
        Iterator<DhProcessDefinitionBo> iterator = exposedDefinitionList.iterator();
        List<JSONObject> visualModelList = null;
        // 遍历处理每个bo对象，分析有没有外链流程，如果没有外链流程，加入队列
        while (iterator.hasNext()) {
            bo = iterator.next();
            String processModelStr = modelMongoDao.getProcessModel(bo.getProAppId(), bo.getProUid(), bo.getProVerUid());
            if (processModelStr == null) {
                HttpReturnStatus returnStatus = bpmProcessUtil.getProcessModel(bo.getProAppId(),
                        bo.getProUid(), bo.getProVerUid());
                if (HttpReturnStatusUtil.isErrorResult(returnStatus)) {
                    throw new PlatformException("获得ProcessModel失败");
                }
                processModelStr = returnStatus.getMsg();
                modelMongoDao.saveProcessModel(bo.getProAppId(), bo.getProUid(), bo.getProVerUid(), processModelStr);
            }
            // 分析外链的流程
            Set<String> externalIdList = getExternalIdList(processModelStr);
            // 获得所有的VisualModel数据
            visualModelList = new ArrayList<>();
            dhGatewayLineService.getVisualModel(visualModelList, bo.getProAppId(), bo.getProUid(), bo.getProVerUid());
            allVisualModelList.addAll(visualModelList);

            if (externalIdList.isEmpty()) {
                // 没有外链流程，入队
                boToPullQueue.offer(bo);
                sortedBpdIdsEachTime.add(bo.getProUid());
                iterator.remove();
            }
            bo.setExternalIdList(externalIdList);
        } // 至此第一次分析结束
        // 获得每个VisualModel在引擎中对应的 data
        List<Map<String, String>> queryParams = new ArrayList<>();
        Map<String, String> queryParam = null;
        for (JSONObject visualModel : allVisualModelList) {
            String poId = visualModel.getString("poId");
            String versionId = visualModel.getString("poVersionId");
            poId = poId.substring(poId.indexOf(".") + 1);
            queryParam = new HashMap<>();
            queryParam.put("bpdId", poId);
            queryParam.put("versionId", versionId);
            queryParams.add(queryParam);
        }
        if (!CollectionUtils.isEmpty(queryParams)) {
            List<LswBpd> lswBpds = lswBpdMapper.queryByBpdIdAndVersionIdList(queryParams);
            modelMongoDao.batchSaveLswBpdData(lswBpds);
        }

        // 反复处理知道所有公开的流程被排序
        List<String> sortedBpdIdsTemp = null;
        while (!exposedDefinitionList.isEmpty()) {
            sortedBpdIdsTemp = new ArrayList<>();
            iterator = exposedDefinitionList.iterator();
            while (iterator.hasNext()) {
                bo = iterator.next();
                bo.removeFromExternalIdList(sortedBpdIdsEachTime); // 去除上次被排序的外链流程
                if (bo.canSynchronize()) {
                    boToPullQueue.offer(bo); // 加入已排序队列
                    sortedBpdIdsTemp.add(bo.getProUid());
                    iterator.remove(); // 从未排序集合中移除
                }
            }
            sortedBpdIdsEachTime = sortedBpdIdsTemp;
            if (sortedBpdIdsEachTime.isEmpty()) {
                // 如果经过一次循环，没有任何流程被排序成功，则说明陷入了死循环
                throw new PlatformException("排序失败，陷入死循环");
            }
        }// 至此所有定义排序结束

        return ServerResponse.createBySuccess(boToPullQueue);
    }

    @Transactional
    @Override
    public ServerResponse updateProcessApp(String proAppId, String oldProVerUid, String newProVerUid, Queue<DhProcessDefinitionBo> definitionBoQueue) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(newProVerUid) || StringUtils.isBlank(oldProVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        // 检查老版本是否存在流程定义
        List<DhProcessDefinition> oldDefinitions = dhProcessDefinitionService.listProcessDefinitionByProAppIdAndProVerUid(proAppId, oldProVerUid);
        if (CollectionUtils.isEmpty(oldDefinitions)) {
            return ServerResponse.createByErrorMessage("版本升级失败，老版本不存在流程定义");
        }
        // 检查是否重复操作
        String updateIdentity = proAppId + "|" + newProVerUid; // 查看是否有更新记录
        String updateTime = commonMongoDao.getStringValue(updateIdentity, CommonMongoDao.APP_UPDATE_RECORD);
        if (updateTime != null) {
            return ServerResponse.createByErrorMessage("应用库此版本已经同步过，不能再次同步，同步时间：" + updateTime);
        }
        commonMongoDao.set(updateIdentity, DateTimeUtil.dateToStr(new Date()), CommonMongoDao.APP_UPDATE_RECORD);
        // 拉取新版本的环节并生成流程定义
        ServerResponse<List<DhProcessDefinition>> newDefinitionResponse = pullAllProcessActivityMeta(definitionBoQueue);
        if (!newDefinitionResponse.isSuccess()) {
            return newDefinitionResponse;
        }
        List<DhProcessDefinition> newDefintions = newDefinitionResponse.getData();
        // 对比得到新老版本都存在的流程定义
        List<DhDefinitionCopyData> copyDataList = findDefinitionNeedCopyConfig(oldDefinitions, newDefintions);
        // 遍历拷贝流程
        Iterator<DhDefinitionCopyData> iterator = copyDataList.iterator();
        while (iterator.hasNext()) {
            DhDefinitionCopyData copyData = iterator.next();
            ServerResponse response = copyConfigFromOldVersion(copyData);
            if (!response.isSuccess()) {
                logger.error("升级应用库失败，复制老版本流程定义时失败：" + copyData.toString());
                throw new PlatformException("升级版本失败，复制配置失败:" + copyData.toString());
            }
            iterator.remove(); // 拷贝完成释放引用
        }
        //if (1 == 1) throw new PlatformException("主动抛出");
        return ServerResponse.createBySuccess();
    }

    // 拉取流程定义
    @Transactional
    @Override
    public ServerResponse<List<DhProcessDefinition>> pullAllProcessActivityMeta(Queue<DhProcessDefinitionBo> boToPullQueue) {
        DhProcessDefinitionBo bo = null;
        List<DhProcessDefinition> newDefintions = new ArrayList<>();
        while (!boToPullQueue.isEmpty()) {
            bo = boToPullQueue.poll();
            ServerResponse<DhProcessDefinition> response = null;
            try {
                response = dhProcessDefinitionService.createDhProcessDefinition(bo.getProAppId(),
                        bo.getProUid(), bo.getProVerUid());
            } catch (Exception e) {
                logger.error("创建环节失败，解析环节失败，路程名：" + bo.getProName(), e);
                throw new PlatformException("创建环节失败，解析环节失败，路程名：" + bo.getProName() + "。" + e.getMessage());
            }
            if (!response.isSuccess()) {
                throw new PlatformException("创建环节失败，解析环节失败，路程名：" + bo.getProName() + response);
            }
            newDefintions.add(response.getData());
        }
        return ServerResponse.createBySuccess(newDefintions);
    }

    // 复制配置
    public ServerResponse copyConfigFromOldVersion(DhDefinitionCopyData copyData) {
        // 1. 拷贝流程定义
        copyDefinition(copyData);
        // 2. 拷贝流程权限
        copyProcessPermission(copyData);
        // 3. 拷贝表单
        copyBpmForm(copyData);
        // 4. 拷贝环节配置
        copyActivityConf(copyData);
        // 5. 拷贝网关相关配置
        copyGatewayAndRule(copyData);
        // 如果新老版本有匹配的人员环节
        if (!copyData.getOldMatchedUserNodeActIds().isEmpty()) {
            // 6. 拷贝可回退环节
            copyActivityReject(copyData);
            // 7. 拷贝可选处理人
            copyActivityAssign(copyData);
            // 8. 拷贝步骤和字段权限
            copyStepAndStepPermission(copyData);
            // 9. 拷贝接口触发器配置
            copyTriggerInterface(copyData);
        }
        return ServerResponse.createBySuccess();
    }

    /**
     * 根据ProcessModel信息获得所有的外链环节
     * @param processModelStr
     * @return
     */
    private Set<String> getExternalIdList(String processModelStr) {
        JSONObject obj = JSON.parseObject(processModelStr);
        JSONArray stepArr = obj.getJSONObject("data").getJSONObject("Diagram").getJSONArray("step");
        Set<String> externalIdList = new HashSet<>();
        findExternalIdFromStepJsonArr(externalIdList, stepArr);
        return externalIdList;
    }

    /**
     * 在ProcessModel的step数组中找到外链流程
     * @param externalList
     * @param stepArr
     */
    private void findExternalIdFromStepJsonArr(Set<String> externalList, JSONArray stepArr) {
        if (stepArr == null || stepArr.size() == 0) {
            return;
        }
        for (int i = 0; i < stepArr.size(); i++) {
            JSONObject step = stepArr.getJSONObject(i);
            if ("subBpd".equals(step.getString("activityType")) && step.getString("externalID") != null) {
                externalList.add(step.getString("externalID"));
            } else if ("subProcess".equals(step.getString("activityType"))) {
                JSONObject diagram = step.getJSONObject("diagram");
                if (diagram != null) {
                    JSONArray steps = diagram.getJSONArray("step");
                    if (steps.size() > 0) {
                        findExternalIdFromStepJsonArr(externalList, steps);
                    }
                }
            }
        }
    }

    /**
     * 找到新老版本交替中需要拷贝配置的流程定义
     * @param oldProcessDefinitions 老版本流程定义集合
     * @param newProcessDefinitions 新版本流程定义集合
     * @return  需要拷贝配置的 新老流程对应关系
     */
    private List<DhDefinitionCopyData> findDefinitionNeedCopyConfig(List<DhProcessDefinition> oldProcessDefinitions, List<DhProcessDefinition> newProcessDefinitions) {
        List<DhDefinitionCopyData> copyDataList = new ArrayList<>();
        Map<String, DhProcessDefinition> map = new HashMap<>();
        for (DhProcessDefinition newProcessDefinition : newProcessDefinitions) {
            map.put(newProcessDefinition.getProUid(), newProcessDefinition);
        }
        List<String> interfaceTriggerUids = dhTriggerMapper.getAllTriUidOfInterfaceTrigger(); // 接口所有接口类型的触发器主键
        DhProcessDefinition newDefinition = null;
        String currentUserUid = getCurrentUserUid();
        for (DhProcessDefinition oldProcessDefinition : oldProcessDefinitions) {
            // 如果老版本的流程在新版本中还存在，视为需要拷贝配置
            if ((newDefinition = map.get(oldProcessDefinition.getProUid())) != null) {
                DhDefinitionCopyData copyData = new DhDefinitionCopyData(newDefinition.getProAppId(), newDefinition.getProUid(),
                        oldProcessDefinition.getProVerUid(), newDefinition.getProVerUid());
                copyData.setOldDefintion(oldProcessDefinition);
                copyData.setNewDefinition(newDefinition);
                copyData.setCurrUserUid(currentUserUid);
                copyData.setInterfaceTriggerUids(interfaceTriggerUids);
                copyDataList.add(copyData);
            }
        }
        return copyDataList;
    }

    private String getCurrentUserUid() {
        return (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
    }

    /**
     * 复制流程定义上的配置
     * @param copyData
     */
    private void copyDefinition(DhDefinitionCopyData copyData){
        DhProcessDefinition oldDefinition = copyData.getOldDefintion();
        DhProcessDefinition newDefinition = copyData.getNewDefinition();
        BeanUtils.copyProperties(oldDefinition, newDefinition, new String[]{"proVerUid", "proStatus", "lastModifiedDate", "createDate", "createUser"});
        newDefinition.setLastModifiedUser(getCurrentUserUid());
        // 更新DH_PROCESS_DEFINITION表
        dhProcessDefinitionMapper.updateByProAppIdAndProUidAndProVerUid(newDefinition);
    }

    /**
     * 拷贝流程范围的权限
     * @param copyData
     */
    private void copyProcessPermission(DhDefinitionCopyData copyData){
        // 老流程权限信息
        List<DhObjectPermission> dhObjectPermissionList = dhObjectPermissionMapper.listProcessDefinitionScopePermission(
                copyData.getProAppId(), copyData.getProUid(), copyData.getOldProVerUid());
        if (!CollectionUtils.isEmpty(dhObjectPermissionList)) {
            for (DhObjectPermission permission : dhObjectPermissionList) {
                permission.setOpUid(EntityIdPrefix.DH_OBJECT_PERMISSION + UUID.randomUUID().toString());
                permission.setProVerUid(copyData.getNewProVerUid());
            }
            dhObjectPermissionMapper.saveBatch(dhObjectPermissionList);
        }
    }

    private void copyBpmForm(DhDefinitionCopyData updateData) {
        // 找到此流程定义所有的表单
        List<BpmForm> oldForms = bpmFormManageService.listAllFormsOfProcessDefinition(updateData.getProUid(), updateData.getOldProVerUid());
        if (CollectionUtils.isEmpty(oldForms)) {
            return;
        }
        Map<String, String> oldNewFormUidMap = new HashMap<>();
        Map<String, String> oldNewFieldUidMap = new HashMap<>();

        List<String> oldFormUids = getIdentityListOfObjectList(oldForms);
        String oldUid, newUid;
        List<BpmForm> newForms = new ArrayList<>();
        // 复制BPM_FORM
        for (BpmForm oldForm : oldForms) {
            BpmForm newForm = new BpmForm();
            BeanUtils.copyProperties(oldForm, newForm);
            oldUid = oldForm.getDynUid();
            newUid = EntityIdPrefix.BPM_FORM + UUID.randomUUID().toString();
            newForm.setDynUid(newUid); // 设置新主键
            newForm.setCreator(updateData.getCurrUserUid());
            newForm.setProVersion(updateData.getNewProVerUid());
            oldNewFormUidMap.put(oldUid, newUid); // 记录新老主键映射关系
            newForms.add(newForm);
        }
        bpmFormManageMapper.insertFormBatch(newForms);
        // 复制BPM_FORM_FIELD
        List<BpmFormField> oldFormFields = bpmFormFieldMapper.listByFormUidList(oldFormUids);
        List<BpmFormField> newFormFields = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldFormFields)) {
            for (BpmFormField oldFormField : oldFormFields) {
                BpmFormField newFormField = new BpmFormField();
                BeanUtils.copyProperties(oldFormField, newFormField);
                oldUid = oldFormField.getFldUid();
                newUid = EntityIdPrefix.BPM_FORM_FIELD + UUID.randomUUID().toString();
                newFormField.setFldUid(newUid); // 设置新主键
                newFormField.setFormUid(oldNewFormUidMap.get(oldFormField.getFormUid())); // 设置表单号
                oldNewFieldUidMap.put(oldUid, newUid); // 记录映射关系
                newFormFields.add(newFormField);
            }
            bpmFormFieldMapper.insertBatch(newFormFields);
        }
        // 为新的表单关联子表单
        List<BpmFormRelePublicForm> oldRelations = bpmFormRelePublicFormMapper.listByFormUidList(oldFormUids);
        List<BpmFormRelePublicForm> newRelations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldRelations)) {
            for (BpmFormRelePublicForm oldRelation : oldRelations) {
                BpmFormRelePublicForm newRelation = new BpmFormRelePublicForm();
                newRelation.setFormUid(oldNewFormUidMap.get(oldRelation.getFormUid()));
                newRelation.setPublicFormUid(oldRelation.getPublicFormUid());
                newRelations.add(newRelation);
            }
            bpmFormRelePublicFormMapper.insertBatch(newRelations);
        }
        updateData.setOldNewFormUidMap(oldNewFormUidMap);
        updateData.setOldNewFieldUidMap(oldNewFieldUidMap);

    }

    /**
     * 获得对象集合的标识符id 集合(去重)
     * @param objList  对象集合
     * @param <T>
     * @return
     */
    private <T> List<String>  getIdentityListOfObjectList(List<T> objList) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(objList)) {
            return result;
        }
        for (T item : objList) {
            String identity = null;
            if (item instanceof BpmForm) {
                identity = ((BpmForm) item).getDynUid();
            } else if (item instanceof DhStep) {
                identity = ((DhStep) item).getStepUid();
            } else if (item instanceof BpmActivityMeta) {
                identity = ((BpmActivityMeta)item).getActivityId();
            } else if (item instanceof DhTrigger) {
                identity = ((DhTrigger)item).getTriUid();
            } else if (item instanceof DhProcessCategory) {
                identity = ((DhProcessCategory)item).getCategoryUid();
            } else if (item instanceof DhGatewayLine) {
                identity = ((DhGatewayLine)item).getGatewayLineUid();
            } else if (item instanceof DhNotifyTemplate) {
                identity = ((DhNotifyTemplate)item).getTemplateUid();
            } else if (item instanceof DhActivityConf) {
                identity = ((DhActivityConf)item).getActcUid();
            } else if (item instanceof DhInterfaceParameter) {
                identity = ((DhInterfaceParameter)item).getParaUid();
            }
            if (StringUtils.isNotBlank(identity) && !result.contains(identity)) {
                result.add(identity);
            }
        }
        return result;
    }

    /**
     * 拷贝人工环节的配置
     * @param copyData
     */
    private void copyActivityConf(DhDefinitionCopyData copyData) {
        List<BpmActivityMeta> oldActivityMetas = bpmActivityMetaService.listAllBpmActivityMeta(copyData.getProAppId(),
                copyData.getProUid(), copyData.getOldProVerUid());
        List<BpmActivityMeta> newActivityMetas = bpmActivityMetaService.listAllBpmActivityMeta(copyData.getProAppId(),
                copyData.getProUid(), copyData.getNewProVerUid());
        copyData.setOldActivityMetas(oldActivityMetas);
        copyData.setNewActivityMetas(newActivityMetas);
        copyData.checkRelationShipAboutActivity();


        // 复制环节配置
        // 1. 将有对应关系的新环节配置删除，用老的
        // 2. 获得作为拷贝对象的配置
        Map<String, String> oldNewConfUidMap = copyData.getOldNewConfUidMap();
        if (oldNewConfUidMap.isEmpty()) {
            // 如果新老版本的配置主键的对应关系是空的，不需要复制配置
            return;
        }
        List<String> actcUidSource = copyData.getActcUidSource();
        List<String> actcUidBeReplace = copyData.getActcUidBeReplace();
        // 批量删除要被覆盖的环节配置
        dhActivityConfMapper.deleteByPrimaryKeyList(actcUidBeReplace);
        // 查询出老版的配置
        List<DhActivityConf> oldConfs = dhActivityConfMapper.listByPrimayKeyList(actcUidSource);
        List<DhActivityConf> newConfs = new ArrayList<>();
        String newConfUid, newActivityId, oldActivityId, oldConfUid;
        for (DhActivityConf oldConf : oldConfs) {
            DhActivityConf newConf = new DhActivityConf();
            BeanUtils.copyProperties(oldConf, newConf);
            oldConfUid = oldConf.getActcUid();
            oldActivityId = oldConf.getActivityId();
            newConfUid = oldNewConfUidMap.get(oldConfUid);
            newActivityId = copyData.getNewActIdByOldActId(oldActivityId);
            newConf.setActcUid(newConfUid); // 将老配置换上新的主键
            newConf.setActivityId(newActivityId); // 将老配置关联的环节换为新环节
            newConfs.add(newConf);
        }
        dhActivityConfMapper.insertBatch(newConfs); // 批量插入
    }

    /**
     * 拷贝网关环节配置
     * @param copyData
     */
    private void copyGatewayAndRule(DhDefinitionCopyData copyData) {
        Map<String, String> oldNewGatewayNodeActIdMap = copyData.getOldNewGatewayNodeActIdMap();
        if (oldNewGatewayNodeActIdMap.isEmpty()) {
            // 如果新老网关没有映射关系，则跳过此步骤
            return;
        }
        Set<String> oldGatewayActIds = oldNewGatewayNodeActIdMap.keySet();
        Collection<String> newGatewayActIds = oldNewGatewayNodeActIdMap.values();
        List<String> gatewayActIds = new ArrayList<>(oldGatewayActIds);
        gatewayActIds.addAll(newGatewayActIds);

        // 找到老版本网关的所有连接线和新版本对应网关的连接线
        List<DhGatewayLine> gatewayLines = dhGatewayLineMapper.listByGatewayActivityIdList(gatewayActIds);

        Map<String, String> oldNewRuleIdMap = new HashMap<>();
        // 建立连接线的对应关系
        // 记录线的标识和规则的关系 标识为起始网关activityBpdId、连接点activityBpdId、routeResult
        Map<String, String> oldIdentityRuleIdMap = new HashMap<>();
        Map<String, String> newIdentityRuleIdMap = new HashMap<>();
        for (DhGatewayLine line : gatewayLines) {
            if ("TRUE".equals(line.getIsDefault()) || StringUtils.isBlank(line.getRuleId())) {
                continue;
            }
            String identity = "";
            if (oldGatewayActIds.contains(line.getActivityId())) {
                // 是老版本的连接线
                identity += copyData.getOldActivityMetaByActId(line.getActivityId()).getActivityBpdId(); // 老网关的activityBpdId
                identity += copyData.getOldActivityMetaByActId(line.getToActivityId()).getActivityBpdId(); // 老版连接点的activityBpdId
                identity += line.getRouteResult();
                oldIdentityRuleIdMap.put(identity, line.getRuleId());
            } else {
                // 是新版本的连接线
                identity += copyData.getNewActivityMetaByActId(line.getActivityId()).getActivityBpdId(); // 老网关的activityBpdId
                identity += copyData.getNewActivityMetaByActId(line.getToActivityId()).getActivityBpdId(); // 老版连接点的activityBpdId
                identity += line.getRouteResult();
                newIdentityRuleIdMap.put(identity, line.getRuleId());
            }
        }
        Set<String> oldIdentityKeySet = oldIdentityRuleIdMap.keySet();
        for (String oldIdentity : oldIdentityKeySet) {
            String newRuleId = newIdentityRuleIdMap.get(oldIdentity);
            if (newRuleId != null) {
                oldNewRuleIdMap.put(oldIdentityRuleIdMap.get(oldIdentity), newRuleId); // 记录新老规则对应关系
            }
        }
        if (oldNewRuleIdMap.isEmpty()) {
            // 如果新老版本的规则没有对应关系，则跳过后续步骤
            return;
        }
        // 查询出老规则对应的数据
        List<String> oldRuleIds = new ArrayList<>(oldNewRuleIdMap.keySet());
        List<DatRule> oldRules = datRuleMapper.listByRuleIds(oldRuleIds);
        List<DatRule> updateRules = new ArrayList<>();
        for (DatRule oldRule : oldRules) {
            if (StringUtils.isNotBlank(oldRule.getRuleProcess())) {
                DatRule updateRule = new DatRule();
                // 找到老规则对应的新规则id
                updateRule.setRuleId(oldNewRuleIdMap.get(oldRule.getRuleId()));
                updateRule.setRuleProcess(oldRule.getRuleProcess());
                updateRules.add(updateRule);
            }
        }
        if (!CollectionUtils.isEmpty(updateRules)) {
            datRuleMapper.batchUpdateRuleProcessByPrimaryKey(updateRules);
        }

        // 更新规则的条件
        // 查询老规则的条件, 修改后批量插入
        List<DatRuleCondition> oldConditions = datRuleConditionMapper.listByRuleIds(oldRuleIds);
        List<DatRuleCondition> newConditions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldConditions)) {
            for (DatRuleCondition oldCondition : oldConditions) {
                DatRuleCondition newCondition = new DatRuleCondition();
                BeanUtils.copyProperties(oldCondition, newCondition);
                newCondition.setConditionId(EntityIdPrefix.DAT_RULE_CONDITION + UUID.randomUUID().toString());
                newCondition.setRuleId(oldNewRuleIdMap.get(oldCondition.getRuleId()));
                newCondition.setCreator(copyData.getCurrUserUid());
                newCondition.setRuleVersion(0);
                newConditions.add(newCondition);
            }
            datRuleConditionMapper.inserToDatRuleCondition(newConditions);
        }

    }

    /**
     * 拷贝可驳回的环节
     * @param copyData
     */
    private void copyActivityReject(DhDefinitionCopyData copyData) {
        // 获得新老版本对应的人工环节映射关系
        Map<String, String> oldNewUserNodeActIdMap = copyData.getOldNewUserNodeActIdMap();
        if (oldNewUserNodeActIdMap.isEmpty()) {
            return;
        }
        // 获得老版本的环节的可回退信息
        List<DhActivityReject> oldRejects = dhActivityRejectMapper.listByActivityIdList(copyData.getOldMatchedUserNodeActIds());
        List<DhActivityReject> newRejects = new ArrayList<>();
        for (DhActivityReject oldReject : oldRejects) {
            // 通过activity_bpd_id找到新版本中此环节
            BpmActivityMeta newBeRejectNode = copyData.getNewActivityMetaByActBpdId(oldReject.getActrRejectActivity());
            if (newBeRejectNode != null && BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(newBeRejectNode.getBpmTaskType())) {
                // 如果此环节存在且是人工环节
                DhActivityReject newReject = new DhActivityReject();
                newReject.setActrUid(EntityIdPrefix.DH_ACTIVITY_REJECT + UUID.randomUUID().toString());
                newReject.setActivityId(oldNewUserNodeActIdMap.get(oldReject.getActivityId()));  // 设置执行驳回的环节
                newReject.setActrRejectActivity(oldReject.getActrRejectActivity()); // 设置驳回到哪个环节
                newRejects.add(newReject);
            }
        }
        if (!CollectionUtils.isEmpty(newRejects)) {
            dhActivityRejectMapper.insertBatch(newRejects);
        }
    }

    /**
     * 拷贝ActivityAssign表（默认处理人、可选处理人、超时通知人、邮件接收人）
     * @param copyData
     */
    private void copyActivityAssign(DhDefinitionCopyData copyData) {
        // 获得新老版本对应的人工环节映射关系
        Map<String, String> oldNewUserNodeActIdMap = copyData.getOldNewUserNodeActIdMap();
        if (oldNewUserNodeActIdMap.isEmpty()) {
            return;
        }
        // 获得分配信息
        List<DhActivityAssign> oldAssigns = dhActivityAssignMapper.listByActivityIdList(copyData.getOldMatchedUserNodeActIds());
        List<DhActivityAssign> newAssigns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldAssigns)) {
            for (DhActivityAssign oldAssign : oldAssigns) {
                DhActivityAssign newAssign = new DhActivityAssign();
                BeanUtils.copyProperties(oldAssign, newAssign);
                newAssign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
                newAssign.setActivityId(oldNewUserNodeActIdMap.get(oldAssign.getActivityId())); // 设置为新环节activity_id
                newAssigns.add(newAssign);
            }
            dhActivityAssignMapper.insertBatch(newAssigns);
        }
    }

    /**
     * 拷贝步骤和字段权限
     * @param copyData
     */
    private void copyStepAndStepPermission(DhDefinitionCopyData copyData) {
        // 获得新老版本对应的人工环节映射关系
        Map<String, String> oldNewUserNodeActIdMap = copyData.getOldNewUserNodeActIdMap();
        if (oldNewUserNodeActIdMap.isEmpty()) {
            return;
        }
        // 得到老版本所有步骤
        List<DhStep> oldSteps = dhStepMapper.listStepsOfProcessDefinition(copyData.getProAppId(),
                copyData.getProUid(), copyData.getOldProVerUid());
        List<String> matchedUserNodeActBpdIds = copyData.getMatchedUserNodeActBpdIds();
        List<DhStep> newSteps = new ArrayList<>();
        Map<String, String> oldNewStepUidMap = new HashMap<>();
        List<String> interfaceTriggerUids = copyData.getInterfaceTriggerUids(); // 数据库中所有的接口类型触发器主键集合
        List<String> oldTriggerInterfaceStepUids = new ArrayList<>(); // 老版本中是接口触发器类型的步骤
        for (DhStep oldStep : oldSteps) {
            if (matchedUserNodeActBpdIds.contains(oldStep.getActivityBpdId())) {
                // 如果匹配的人工环节中包含此 activity_bpd_id ， 说明要拷贝这个配置
                DhStep newStep = new DhStep();
                BeanUtils.copyProperties(oldStep, newStep);
                String newStepUid = EntityIdPrefix.DH_STEP + UUID.randomUUID().toString();
                oldNewStepUidMap.put(oldStep.getStepUid(), newStepUid);
                newStep.setStepUid(newStepUid);
                if (DhStep.TYPE_FORM.equals(oldStep.getStepType())) {
                    // 如果是表单步骤，需要替换为新的表单编号
                    newStep.setStepObjectUid(copyData.getNewFormUidByOldFormUid(oldStep.getStepObjectUid()));
                } else if (DhStep.TYPE_TRIGGER.equals(oldStep.getStepType())
                        && interfaceTriggerUids.contains(oldStep.getStepObjectUid())) {
                    // 如果是触发器步骤，且是接口类型的触发器
                    oldTriggerInterfaceStepUids.add(oldStep.getStepUid());
                }
                newStep.setProVerUid(copyData.getNewProVerUid());
                newSteps.add(newStep);
            }
        }
        copyData.setOldNewStepUidMap(oldNewStepUidMap);
        copyData.setOldTriggerInterfaceStepUids(oldTriggerInterfaceStepUids);
        if (oldNewStepUidMap.isEmpty()) {
            return;
        }
        dhStepMapper.insertBatchDhStep(newSteps);

        // 查找老的字段权限
        List<DhObjectPermission> oldFieldPermissions = dhObjectPermissionMapper.listByStepUidList(new ArrayList<>(oldNewStepUidMap.keySet()));
        List<DhObjectPermission> newFieldPermissions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldFieldPermissions)) {
            for (DhObjectPermission oldFieldPermission : oldFieldPermissions) {
                DhObjectPermission newFieldPermission = new DhObjectPermission();
                BeanUtils.copyProperties(oldFieldPermission, newFieldPermission);
                newFieldPermission.setOpUid(EntityIdPrefix.DH_OBJECT_PERMISSION + UUID.randomUUID().toString()); // 新主键
                newFieldPermission.setStepUid(oldNewStepUidMap.get(oldFieldPermission.getStepUid())); // 修改步骤id
                newFieldPermission.setProVerUid(copyData.getNewProVerUid()); // 修改版本
                // 根据老的字段权限上的字段主键找到新的对应的字段主键
                String newFieldUid = copyData.getNewFieldUidByOldFieldUid(oldFieldPermission.getOpObjUid());
                // 如果新版本表单字段里没有对应的，说明该字段是公共表单的字段，维持原样即可
                newFieldPermission.setOpObjUid(newFieldUid == null ? oldFieldPermission.getOpObjUid() : newFieldUid);
                newFieldPermissions.add(newFieldPermission);
            }
            dhObjectPermissionMapper.saveBatch(newFieldPermissions);
        }

    }

    /**
     * 拷贝接口类型的触发器需要的配置
     * @param copyData
     */
    private void copyTriggerInterface(DhDefinitionCopyData copyData) {
        List<String> oldTriggerInterfaceStepUids = copyData.getOldTriggerInterfaceStepUids();
        if (oldTriggerInterfaceStepUids.isEmpty()) {
            return;
        }
        // 根据步骤获得老版本的相关tirggerInterface数据
        List<DhTriggerInterface> oldTriggerInterfaces = dhTriggerInterfaceMapper.listByStepUidList(oldTriggerInterfaceStepUids);
        List<DhTriggerInterface> newTriggerInterfaces = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldTriggerInterfaces)) {
            for (DhTriggerInterface oldTriggerInterface : oldTriggerInterfaces) {
                DhTriggerInterface newTriggerInterface = new DhTriggerInterface();
                BeanUtils.copyProperties(oldTriggerInterface, newTriggerInterface);
                newTriggerInterface.setTinUid(EntityIdPrefix.DH_TRIGGER_INTERFACE + UUID.randomUUID().toString()); // 设置主键
                newTriggerInterface.setCreator(copyData.getCurrUserUid()); // 设置创建人
                newTriggerInterface.setDynUid(copyData.getNewFormUidByOldFormUid(oldTriggerInterface.getDynUid())); // 设置表单号
                newTriggerInterface.setStepUid(copyData.getNewStepUidByOldStepUid(oldTriggerInterface.getStepUid())); // 设置步骤号
                newTriggerInterface.setActivityId(copyData.getNewActIdByOldActId(oldTriggerInterface.getActivityId())); // 设置环节id
                newTriggerInterfaces.add(newTriggerInterface);
            }
            dhTriggerInterfaceMapper.insertBatch(newTriggerInterfaces);
        }

    }

    public static void main(String[] args){
        Map<String, String> map = new HashMap<>();
        Set<String> set = map.keySet();
        System.out.println(set.isEmpty());
    }


}