package com.desmart.desmartbpm.mongo;

import com.desmart.desmartbpm.entity.engine.LswBpd;

import java.util.List;
import java.util.Map;

/**
 * 保存VisualModel的持久层
 */
public interface ModelMongoDao {

    /**
     * 保存ProcessModel
     * @param proAppId
     * @param bpdId
     * @param snapshotId
     * @param content  HTTP请求的响应正文
     */
    void saveProcessModel(String proAppId, String bpdId, String snapshotId, String content);

    String getProcessModel(String proAppId, String bpdId, String snapshotId);

    void saveVisualModel(String proAppId, String bpdId, String snapshotId, String content);

    String getVisualModel(String proAppId, String bpdId, String snapshotId);

    void saveLswBpdData(String bpdId, String versionId, String content);

    String getLswBpdData(String bpdId, String versionId);

    /**
     * 批量以Map的形式保存 lswBpd中的data数据（转成String）
     * @param lswBpds
     */
    void batchSaveLswBpdData(List<LswBpd> lswBpds);
}