package com.desmart.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Stack;

public class TokenInfoUtil {

    private Stack<Node> stack = new Stack<>();
    private String tokenId;
    private String parentTokenId;
    private Stack<Node> tempStak;

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
     * 调用此构造方法，自动触发查找指定任务的父流程tokenId<br/>
     * 实例化后调用getParentTokenId() 即可
     * @param taskId  需要确定父流程tokenId的任务id
     * @param processMsgJson  调用RESTFul API获得的流程实例信息直接转换的JSON对象
     */
    public TokenInfoUtil(int taskId, JSONObject processMsgJson) {
        JSONObject rootJson = processMsgJson.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
        findParentProcessToken(rootJson, String.valueOf(taskId));
    }

    /**
     * 调用此构造方法，自动触发找指定节点上的tokenId<br/>
     * 实例化后调用getTokenId() 即可
     * @param actBpdIdIdentitySubProcess  代表子流程的节点的元素id
     * @param processMsgJson  调用RESTFul API获得的流程实例信息直接转换的JSON对象
     */
    public TokenInfoUtil(String actBpdIdIdentitySubProcess, String parentFlowObjectId, JSONObject processMsgJson) {
        JSONObject rootJson = processMsgJson.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
        getTokenIdOfProcessNode(actBpdIdIdentitySubProcess, parentFlowObjectId, rootJson);

    }



    private void findParentProcessToken(JSONObject jsonObject, String taskId) {
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
                findParentProcessToken(children.getJSONObject(i), taskId);
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

    /**
     * 根据flowobjectId找到这个节点代表的流程的tokenId
     * @param actBpdIdIdentitySubProcess
     * @param parentFlowObjectId  父流程的 flowobjectId
     * @param jsonObject
     */
    private void getTokenIdOfProcessNode(String actBpdIdIdentitySubProcess, String parentFlowObjectId, JSONObject jsonObject) {
        if (this.tokenId != null) { // 说明已经得到了结果
            return;
        }
        String nodeId = jsonObject.getString("nodeId");
        String tokenId = jsonObject.getString("tokenId");
        String flowObjectId = jsonObject.getString("flowObjectId");
        stack.push(new Node(nodeId, tokenId, flowObjectId)); // 将当前对象放入栈中
        if (actBpdIdIdentitySubProcess.equals(flowObjectId)) {
            // 如果flowObjectId相等，校验上级流程是否相等
            if (checkSubProcessNode(parentFlowObjectId)) {
                this.tokenId = tokenId;
                return;
            }
        }
        JSONArray children = jsonObject.getJSONArray("children");
        if (children != null) {
            for (int i =0; i < children.size(); i++) {
                getTokenIdOfProcessNode(actBpdIdIdentitySubProcess, parentFlowObjectId, children.getJSONObject(i));
            }
        }
        if (this.tokenId != null) {
            return;
        }
        // 遍历完成 弹出当前Node
        stack.pop();
    }

    /**
     * 检查这个节点的父流程是否是给定的
     * @param parentFlowObjectId
     */
    private boolean checkSubProcessNode(String parentFlowObjectId) {
        tempStak = new Stack<>(); // 为校验失败做准备
        tempStak.push(stack.pop());
        if (stack.size() < 2) {
            // 说明当前这个节点位于主流程
            if (parentFlowObjectId == null) {
                return true;
            } else {
                returnNodeFromTempStackToStack();
                return false;
            }
        }
        tempStak.push(stack.pop());
        Node node = stack.pop();  // 命中节点上级的上级
        tempStak.push(node);
        if (parentFlowObjectId == null) {
            returnNodeFromTempStackToStack();
            return false;
        }
        if (parentFlowObjectId.equals(node.flowObjectId)) {
            return true;
        } else {
            returnNodeFromTempStackToStack();
            return false;
        }


    }

    private void returnNodeFromTempStackToStack() {
        while (!tempStak.isEmpty()) {
            stack.push(tempStak.pop());
        }
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getParentTokenId() {
        return parentTokenId;
    }




}