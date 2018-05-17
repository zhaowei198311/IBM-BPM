package com.desmart.desmartportal.entity;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BpmRouteConditionResult implements Cloneable, Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(BpmRouteConditionResult.class);
    public static final String[] ROUTE_RESULT_VARNAME = new String[]{"result_0", "result_1", "result_2", "result_3", "result_4", "result_5", "result_6", "result_7", "result_8", "result_9", "result_10", "result_11", "result_12", "result_13", "result_14", "result_15", "result_16", "result_17", "result_18", "result_19", "result_20"};
    private String result_0 = "";
    private String result_1 = "";
    private String result_2 = "";
    private String result_3 = "";
    private String result_4 = "";
    private String result_5 = "";
    private String result_6 = "";
    private String result_7 = "";
    private String result_8 = "";
    private String result_9 = "";
    private String result_10 = "";
    private String result_11 = "";
    private String result_12 = "";
    private String result_13 = "";
    private String result_14 = "";
    private String result_15 = "";
    private String result_16 = "";
    private String result_17 = "";
    private String result_18 = "";
    private String result_19 = "";
    private String result_20 = "";
    private boolean used = false;

    public BpmRouteConditionResult() {
    }

    public BpmRouteConditionResult clone() {
        BpmRouteConditionResult result = null;

        try {
            result = (BpmRouteConditionResult)super.clone();
        } catch (CloneNotSupportedException var3) {
            result = null;
            LOG.error("克隆ConditionResult BO对象失败！", var3);
        }

        return result;
    }

    public String getResult_0() {
        return this.result_0;
    }

    public void setResult_0(String result_0) {
        this.result_0 = result_0;
    }

    public String getResult_1() {
        return this.result_1;
    }

    public void setResult_1(String result_1) {
        this.result_1 = result_1;
    }

    public String getResult_2() {
        return this.result_2;
    }

    public void setResult_2(String result_2) {
        this.result_2 = result_2;
    }

    public String getResult_3() {
        return this.result_3;
    }

    public void setResult_3(String result_3) {
        this.result_3 = result_3;
    }

    public String getResult_4() {
        return this.result_4;
    }

    public void setResult_4(String result_4) {
        this.result_4 = result_4;
    }

    public String getResult_5() {
        return this.result_5;
    }

    public void setResult_5(String result_5) {
        this.result_5 = result_5;
    }

    public String getResult_6() {
        return this.result_6;
    }

    public void setResult_6(String result_6) {
        this.result_6 = result_6;
    }

    public String getResult_7() {
        return this.result_7;
    }

    public void setResult_7(String result_7) {
        this.result_7 = result_7;
    }

    public String getResult_8() {
        return this.result_8;
    }

    public void setResult_8(String result_8) {
        this.result_8 = result_8;
    }

    public String getResult_9() {
        return this.result_9;
    }

    public void setResult_9(String result_9) {
        this.result_9 = result_9;
    }

    public String getResult_10() {
        return this.result_10;
    }

    public void setResult_10(String result_10) {
        this.result_10 = result_10;
    }

    public String getResult_11() {
        return this.result_11;
    }

    public void setResult_11(String result_11) {
        this.result_11 = result_11;
    }

    public String getResult_12() {
        return this.result_12;
    }

    public void setResult_12(String result_12) {
        this.result_12 = result_12;
    }

    public String getResult_13() {
        return this.result_13;
    }

    public void setResult_13(String result_13) {
        this.result_13 = result_13;
    }

    public String getResult_14() {
        return this.result_14;
    }

    public void setResult_14(String result_14) {
        this.result_14 = result_14;
    }

    public String getResult_15() {
        return this.result_15;
    }

    public void setResult_15(String result_15) {
        this.result_15 = result_15;
    }

    public String getResult_16() {
        return this.result_16;
    }

    public void setResult_16(String result_16) {
        this.result_16 = result_16;
    }

    public String getResult_17() {
        return this.result_17;
    }

    public void setResult_17(String result_17) {
        this.result_17 = result_17;
    }

    public String getResult_18() {
        return this.result_18;
    }

    public void setResult_18(String result_18) {
        this.result_18 = result_18;
    }

    public String getResult_19() {
        return this.result_19;
    }

    public void setResult_19(String result_19) {
        this.result_19 = result_19;
    }

    public String getResult_20() {
        return this.result_20;
    }

    public void setResult_20(String result_20) {
        this.result_20 = result_20;
    }

    public boolean isUsed() {
        return this.used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
