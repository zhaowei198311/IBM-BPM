package com.desmart.desmartbpm.entity;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class DhFieldPermissionData {
    Map<String, Map<String, String>> fieldJsonStr = new HashMap<>();
    Map<String, Map<String, String>> titleJsonStr = new HashMap<>();
    Map<String, Map<String, String>> fieldSkipJsonStr = new HashMap<>();
    Map<String, Map<String, String>> titleSkipJsonStr = new HashMap<>();
    Map<String, Map<String, String>> fieldPrintJsonStr = new HashMap<>();
    Map<String, Map<String, String>> titlePrintJsonStr = new HashMap<>();

    /**
     * 添加字段只读
     * @param name
     */
    public void addFieldReadonlyPermission(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("edit", "no");
        fieldJsonStr.put(name, map);
    }

    /**
     * 添加字段隐藏
     * @param name
     */
    public void addFieldHiddenPermission(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("display", "none");
        fieldJsonStr.put(name, map);
    }

    /**
     * 添加标题只读
     * @param name
     */
    public void addTitleReadonlyPermission(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("edit", "no");
        titleJsonStr.put(name, map);
    }

    /**
     * 添加标题隐藏
     * @param name
     */
    public void addTitleHiddenPermission(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("display", "none");
        titleJsonStr.put(name, map);
    }

    /**
     * 添加字段跳过必填
     * @param name
     */
    public void addFieldSkipPermissionYes(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("skip", "yes");
        fieldSkipJsonStr.put(name, map);
    }

    /**
     * 添加标题跳过必填
     * @param name
     */
    public void addTitleSkipPermissionYes(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("skip", "yes");
        titleSkipJsonStr.put(name, map);
    }

    /**
     * 添加字段打印权限 yes
     * @param name
     */
    public void addFieldPrintPermissionYes(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("print", "yes");
        fieldPrintJsonStr.put(name, map);
    }

    /**
     * 添加字段打印权限 no
     * @param name
     */
    public void addFieldPrintPermissionNo(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("print", "no");
        fieldPrintJsonStr.put(name, map);
    }

    /**
     * 添加标题打印权限 yes
     * @param name
     */
    public void addTitlePrintPermissionYes(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("print", "yes");
        titlePrintJsonStr.put(name, map);
    }

    /**
     * 添加标题打印权限 no
     * @param name
     */
    public void addTitlePrintPermissionNo(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("print", "no");
        titlePrintJsonStr.put(name, map);
    }

    public Map<String, Map<String, String>> getFieldJsonStr() {
        return fieldJsonStr;
    }

    public void setFieldJsonStr(Map<String, Map<String, String>> fieldJsonStr) {
        this.fieldJsonStr = fieldJsonStr;
    }

    public Map<String, Map<String, String>> getTitleJsonStr() {
        return titleJsonStr;
    }

    public void setTitleJsonStr(Map<String, Map<String, String>> titleJsonStr) {
        this.titleJsonStr = titleJsonStr;
    }

    public Map<String, Map<String, String>> getFieldSkipJsonStr() {
        return fieldSkipJsonStr;
    }

    public void setFieldSkipJsonStr(Map<String, Map<String, String>> fieldSkipJsonStr) {
        this.fieldSkipJsonStr = fieldSkipJsonStr;
    }

    public Map<String, Map<String, String>> getTitleSkipJsonStr() {
        return titleSkipJsonStr;
    }

    public void setTitleSkipJsonStr(Map<String, Map<String, String>> titleSkipJsonStr) {
        this.titleSkipJsonStr = titleSkipJsonStr;
    }

    public Map<String, Map<String, String>> getFieldPrintJsonStr() {
        return fieldPrintJsonStr;
    }

    public void setFieldPrintJsonStr(Map<String, Map<String, String>> fieldPrintJsonStr) {
        this.fieldPrintJsonStr = fieldPrintJsonStr;
    }

    public Map<String, Map<String, String>> getTitlePrintJsonStr() {
        return titlePrintJsonStr;
    }

    public void setTitlePrintJsonStr(Map<String, Map<String, String>> titlePrintJsonStr) {
        this.titlePrintJsonStr = titlePrintJsonStr;
    }
}