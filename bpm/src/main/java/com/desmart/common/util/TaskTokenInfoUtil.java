package com.desmart.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Stack;

public class TaskTokenInfoUtil {

    private Stack<Node> stack = new Stack<>();
    private String tokenId;
    private String parentTokenId;

    private class Node {
        String nodeId;
        String tokenId;
        String flowObjectId;
        public Node(String nodeId, String tokenId, String flowObjectId) {
            this.nodeId = nodeId;
            this.tokenId = tokenId;
            this.flowObjectId = flowObjectId;
        }
    }

    /**
     *
     * @param taskId  需要确定父流程tokenId的任务id
     * @param processMsgJson  调用RESTFul API获得的流程实例信息直接转换的JSON对象
     */
    public TaskTokenInfoUtil(int taskId, JSONObject processMsgJson) {
        JSONObject rootJson = processMsgJson.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
        check(rootJson, String.valueOf(taskId));
    }

    private void check(JSONObject jsonObject, String taskId) {
        if (this.tokenId != null) { // 说明已经得到了结果
            return;
        }
        String nodeId = jsonObject.getString("nodeId");
        String tokenId = jsonObject.getString("tokenId");
        String flowObjectId = jsonObject.getString("flowObjectId");
        stack.push(new Node(nodeId, tokenId, flowObjectId));

        JSONArray createdTaskIDs = jsonObject.getJSONArray("createdTaskIDs");
        if (createdTaskIDs != null) {
            for (int i = 0; i < createdTaskIDs.size(); i++) {
                if (createdTaskIDs.getString(i).equals(taskId)) {
                    this.tokenId = tokenId;
                    getParentProcessTokenId(flowObjectId);
                    return;
                }
            }
        }

        JSONArray children = jsonObject.getJSONArray("children");
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                check(children.getJSONObject(i), taskId);
            }
        }
        if (this.tokenId != null) {
            return;
        }
        // 遍历完成 弹出当前Node
        stack.pop();
    }

    /**
     * 获得当前节点的父流程tokenId
     * @param currentFlowObjectId 当前节点的flowObjectId
     * @return
     */
    private void getParentProcessTokenId(String currentFlowObjectId) {
        stack.pop(); // 先将当前节点自身弹出
        Node node = stack.pop(); // 上级节点
        if (currentFlowObjectId.equals(node.flowObjectId)) {
            node = stack.pop();
        }
        if ("1".equals(node.nodeId) || stack.isEmpty()) {
            return;
        }
        node = stack.pop();
        parentTokenId = node.tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getParentTokenId() {
        return parentTokenId;
    }




}