package com.desmart.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartportal.entity.CommonBusinessObject;

public class CommonBusinessObjectUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CommonBusinessObjectUtils.class);

    public CommonBusinessObjectUtils() {
    }

    /**
     * 在指定nextOwner_?变量上设置
     * @param assignVarName
     * @param pubBo
     * @param owners
     */
    public static void setNextOwners(String assignVarName, CommonBusinessObject pubBo, List<String> owners) {
        boolean var3 = false;

        try {
            int pos = assignVarName.lastIndexOf("_");
            pos = pos < 0 ? 0 : pos;
            int assignNum = Integer.parseInt(assignVarName.substring(pos + 1));
            setNextOwners(assignNum, pubBo, owners);
        } catch (NumberFormatException var5) {
            LOG.error("下一处理人的名称序号转换失败！变量名称：" + assignVarName, var5);
        }

    }
    
    /**
     * 在指定signCount_?变量上设置
     * @param signCountVarName
     * @param pubBo
     * @param signCount
     */
    public static void setOwnerSignCount(String signCountVarName, CommonBusinessObject pubBo, int signCount) {
        boolean var3 = false;

        try {
            int pos = signCountVarName.lastIndexOf("_");
            pos = pos < 0 ? 0 : pos;
            int assignNum = Integer.parseInt(signCountVarName.substring(pos + 1));
            setOwnerSignCount(assignNum, pubBo, signCount);
        } catch (NumberFormatException var5) {
            LOG.error("下一处理会签次数的名称序号转换失败！变量名称：" + signCountVarName, var5);
        }

    }
    
    
    public static void setNextOwners(int ownerNum, CommonBusinessObject pubBo, List<String> owners) {
        int num = ownerNum < 0 ? 0 : (ownerNum > 20 ? 20 : ownerNum);

        try {
            Method md = pubBo.getClass().getMethod("setNextOwners_" + num, List.class);
            md.invoke(pubBo, owners);
        } catch (Exception var6) {
            LOG.error("程序在设置下一环节处理人的时候发生异常！", var6);
        }

    }

    public static void setOwnerSignCount(int ownerNum, CommonBusinessObject pubBo, int signCount) {
        int num = ownerNum < 0 ? 0 : (ownerNum > 20 ? 20 : ownerNum);

        try {
            Method md = pubBo.getClass().getMethod("setSignCount_" + num, Integer.TYPE);
            md.invoke(pubBo, signCount);
        } catch (Exception var6) {
            LOG.error("程序在设置下一环节处理人会签数量的时候发生异常！", var6);
        }

    }



    public static List<String> getNextOwners(int ownerNum, CommonBusinessObject pubBo) {
        int num = ownerNum < 0 ? 0 : (ownerNum > 20 ? 20 : ownerNum);
        Object owners = new ArrayList();

        try {
            Method md = pubBo.getClass().getMethod("getNextOwners_" + num);
            owners = (List)md.invoke(pubBo);
        } catch (Exception var6) {
            LOG.error("程序在获取下一环节处理人列表的时候发生异常！ownerNum=" + ownerNum, var6);
        }

        return (List)owners;
    }

    public static List<String> getNextOwners(String assignVarName, CommonBusinessObject pubBo) {
        List<String> owners = new ArrayList();
        boolean var3 = false;

        try {
            int pos = assignVarName.lastIndexOf("_");
            pos = pos < 0 ? 0 : pos;
            int assignNum = Integer.parseInt(assignVarName.substring(pos + 1));
            owners = getNextOwners(assignNum, pubBo);
        } catch (NumberFormatException var5) {
            LOG.error("程序在获取下一环节处理人列表的时候发生异常！变量名称：" + assignVarName, var5);
        }

        return (List)owners;
    }




}
