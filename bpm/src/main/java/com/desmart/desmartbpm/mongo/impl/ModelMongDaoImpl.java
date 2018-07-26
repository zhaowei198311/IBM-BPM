package com.desmart.desmartbpm.mongo.impl;

import com.desmart.desmartbpm.mongo.ModelMongoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelMongDaoImpl implements ModelMongoDao {
    private static final Logger logger = LoggerFactory.getLogger(ModelMongDaoImpl.class);
    public static final String PROCESS_MODEL_COLLECTION = "ProcessModel";
    public static final String VISUAL_MODEL_COLLECTION = "VisualModel";


    @Resource
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    public void saveProcessModel(String proAppId, String bpdId, String snapshotId, String content) {
        saveModel(proAppId, bpdId, snapshotId, content, PROCESS_MODEL_COLLECTION);
    }

    public String getProcessModel(String proAppId, String bpdId, String snapshotId) {
        return getModelById(proAppId, bpdId, snapshotId, PROCESS_MODEL_COLLECTION);
    }

    public void saveVisualModel(String proAppId, String bpdId, String snapshotId, String content) {
        saveModel(proAppId, bpdId, snapshotId, content, VISUAL_MODEL_COLLECTION);
    }

    public String getVisualModel(String proAppId, String bpdId, String snapshotId) {
        return getModelById(proAppId, bpdId, snapshotId, VISUAL_MODEL_COLLECTION);
    }

    private void saveModel(String proAppId, String bpdId, String snapshotId, String content, String collectionName) {
        String id = proAppId + bpdId + snapshotId;
        Map<String, String> map = new HashMap<>();
        map.put("_id", id);
        map.put("content", content);
        mongoTemplate.insert(map, collectionName);
    }

    private  String getModelById(String proAppId, String bpdId, String snapshotId, String collectionName) {
        String id = proAppId + bpdId + snapshotId;
        Map map = mongoTemplate.findById(id, Map.class, collectionName);
        return map == null ? null : (String)map.get("content");
    }

    private void saveBatch(List<Map<String, String>> list, String collectionName) {
        mongoTemplate.insert(list, collectionName);
    }

}