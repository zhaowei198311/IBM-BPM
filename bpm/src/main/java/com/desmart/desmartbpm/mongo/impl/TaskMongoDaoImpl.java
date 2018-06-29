package com.desmart.desmartbpm.mongo.impl;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.LockedTask;
import com.desmart.desmartbpm.entity.OpenedTask;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.mongo.TaskMongoDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class TaskMongoDaoImpl implements TaskMongoDao {
	
	@Resource
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

	@Override
	public LockedTask queryLockTaskByTaskId(int taskId) {
		return mongoTemplate.findById(taskId, LockedTask.class, LockedTask.LOCKED_TASK_COLLECTION_NAME);
	}


    @Override
    public int removeLockedTask(int taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(taskId));
        WriteResult result = mongoTemplate.remove(query, LockedTask.LOCKED_TASK_COLLECTION_NAME);
        return result.getN();
    }


    @Override
    public void saveLockedTask(LockedTask lockedTask) {
        mongoTemplate.insert(lockedTask, LockedTask.LOCKED_TASK_COLLECTION_NAME);
    }

    @Override
    public void saveOpenedTask(OpenedTask openedTask) {
        mongoTemplate.insert(openedTask, OpenedTask.OPENED_TASK_COLLECTION_NAME);
    }

    @Override
    public List<LockedTask> getAllLockedTasks() {
        List<LockedTask> lockedTasks = mongoTemplate.findAll(LockedTask.class, LockedTask.LOCKED_TASK_COLLECTION_NAME);
        return lockedTasks;
    }

    @Override
    public void batchSaveLockedTasks(List<LockedTask> list) {
        if (list != null && list.size() > 0) {
            mongoTemplate.insert(list, LockedTask.LOCKED_TASK_COLLECTION_NAME);
        }

    }

    @Override
    public boolean hasTaskBeenOpened(int taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("taskId").is(taskId));
        long count = mongoTemplate.count(query, OpenedTask.OPENED_TASK_COLLECTION_NAME);
        return count > 0;
    }

    @Override
    public boolean hasTaskBeenOpened(String taskUid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(taskUid));
        long count = mongoTemplate.count(query, OpenedTask.OPENED_TASK_COLLECTION_NAME);
        return count > 0;
    }

    @Override
    public void batchlockTasks(List<Integer> taskIdList, String reason) {
        if (taskIdList == null || taskIdList.isEmpty()) {
            return;
        }
        List<LockedTask> lockedTasks = new ArrayList<>();
        for (Integer taskId : taskIdList) {
            lockedTasks.add(new LockedTask(taskId, new Date(), reason));
        }
        batchSaveLockedTasks(lockedTasks);
    }

}
