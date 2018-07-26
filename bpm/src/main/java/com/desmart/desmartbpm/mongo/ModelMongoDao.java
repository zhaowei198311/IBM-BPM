package com.desmart.desmartbpm.mongo;

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

}