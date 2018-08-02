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

    public void check(JSONObject jsonObject, String taskId) {
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
                    stack.pop(); // 将自己弹出
                    Node parentNode = stack.pop(); // 弹出父级node
                    if (parentNode.flowObjectId == null) {
                        parentNode = stack.pop(); // 弹出父级的父级node
                        this.parentTokenId = parentNode.tokenId;
                    } else if (StringUtils.equals(flowObjectId, parentNode.flowObjectId)) {
                        // 父节点的flowObjectId与子节点相同, 再网上找一级
                        parentNode = stack.pop();
                        this.parentTokenId = parentNode.tokenId;
                    } else {
                        //父节点的flowObjectId与子节点不同
                        this.parentTokenId = parentNode.tokenId;
                    }
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
        // 遍历完成 弹出一个Node
        stack.pop();
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getParentTokenId() {
        return parentTokenId;
    }

    public void setParentTokenId(String parentTokenId) {
        this.parentTokenId = parentTokenId;
    }

    public static void main(String[] args){
        String str = FileToStringUtil.getStringFromLocalFile("E:/demo.txt");
        JSONObject dataJson = JSON.parseObject(str);
        JSONObject root = dataJson.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");

        TaskTokenInfoUtil test = new TaskTokenInfoUtil();
        test.check(root, "102");
        System.out.println(test.tokenId);
        System.out.println(test.parentTokenId);
    }


}