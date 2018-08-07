package com.desmart.desmartportal.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.DateTimeUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartportal.dao.DhFormNoCounterMapper;
import com.desmart.desmartportal.entity.DhFormNoCounter;
import com.desmart.desmartportal.service.DhFormNoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DhFormNoServiceImpl implements DhFormNoService {
    private Logger log = Logger.getLogger(DhFormNoServiceImpl.class);

    @Autowired
    private BpmFormManageMapper bpmFormManageMapper;
    @Autowired
    private DhFormNoCounterMapper dhFormNoCounterMapper;


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public JSONArray updateFormNoListJsonObject(BpmForm bpmForm, JSONArray oldFormNoJSONArray) {
        JSONArray result = null;
        String formNo = null;
        String formUid = bpmForm.getDynUid();
        boolean exist = false;
        if (oldFormNoJSONArray == null || oldFormNoJSONArray.size() == 0) {
            // 创建新的formNoList并返回
            formNo = generateFormNo(bpmForm.getFormNoExpression());
            JSONArray newJsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("formUid", formUid);
            jsonObject.put("formNo", formNo);
            jsonObject.put("formNoStatic", bpmForm.getFormNoStatic());
            newJsonArray.add(jsonObject);
            return newJsonArray;
        }
        // 检查并更新formNoList对象
        for (int i = 0; i < oldFormNoJSONArray.size(); i++) {
            JSONObject jsonObject = oldFormNoJSONArray.getJSONObject(i);
            if (formUid.equals(jsonObject.getString("formUid"))) {
                exist = true;
                break;
            }
        }
        if (exist) {
            return oldFormNoJSONArray;
        }
        // 不存在时，需要创建一个
        formNo = generateFormNo(bpmForm.getFormNoExpression());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formUid", formUid);
        jsonObject.put("formNo", formNo);
        oldFormNoJSONArray.add(jsonObject);
        return oldFormNoJSONArray;
    }

    @Override
    public String findFormNoByFormUid(String formUid, JSONArray formNoListJson) {
        if (formNoListJson == null) {
            return null;
        }
        for (int i = 0; i < formNoListJson.size(); i++) {
            JSONObject item = formNoListJson.getJSONObject(i);
            if (formUid.equals(item.getString("formUid"))) {
                return item.getString("formNo");
            }
        }
        return null;
    }

    /**
     * 根据表单号生成规则生成一个表单号
     * @param formNoExpression 表单号生成表达式
     * @return
     */
    public String generateFormNo(String formNoExpression) {
        int affectRow = 0;
        // 检查是否包含需要替换的字符，如果不包含就直接返回原字符
        Pattern pattern = Pattern.compile("(\\{yyyy-MM-dd\\})|(\\{yyyyMMdd\\})|(\\{num\\d\\})");
        Matcher matcher = pattern.matcher(formNoExpression);
        if (!matcher.find()) {
            return formNoExpression;
        }

        DhFormNoCounter newCount = new DhFormNoCounter();
        if (StringUtils.isBlank(formNoExpression)) {
            throw new PlatformException("表单号生成规则为空");
        }
        if (formNoExpression.contains("{yyyy-MM-dd}")) {
            formNoExpression = formNoExpression.replace("{yyyy-MM-dd}", DateTimeUtil.dateToStr(new Date(), "yyyy-MM-dd"));
            newCount.setIsDaily(Const.Boolean.TRUE);
        } else if (formNoExpression.contains("{yyyyMMdd}")) {
            formNoExpression = formNoExpression.replace("{yyyyMMdd}", DateTimeUtil.dateToStr(new Date(), "yyyyMMdd"));
            newCount.setIsDaily(Const.Boolean.TRUE);
        } else {
            newCount.setIsDaily(Const.Boolean.FALSE);
        }
        newCount.setFormNoExpression(formNoExpression);
        // 检查是否需要递增单号
        pattern = Pattern.compile("\\{num\\d\\}");
        matcher = pattern.matcher(formNoExpression);
        if (!matcher.find()) {
            return formNoExpression;
        }
        DhFormNoCounter dhFormNoCounter = dhFormNoCounterMapper.selectByPrimaryKey(formNoExpression);

        if (dhFormNoCounter == null) {
            // 此规则的表单号还没有
            newCount.setCurrentNo(1);
            try {
                affectRow = dhFormNoCounterMapper.insert(newCount);
            } catch (Exception e) {
                // 如果主键重复会报此异常
                e.printStackTrace();
            }
            if (affectRow == 1) {
                // 插入数据成功
                return mergeFormNoExpressWithCurrentNo(formNoExpression, 1);
            } else {
                // 获取最新的记录并更新
                int nowNumber = this.getNextNumber(formNoExpression, null);
                return mergeFormNoExpressWithCurrentNo(formNoExpression, nowNumber);
            }
        } else {
            // 此规则的表单号已存在
            int nowNumber = this.getNextNumber(formNoExpression, dhFormNoCounter);
            return mergeFormNoExpressWithCurrentNo(formNoExpression, nowNumber);
        }

    }

    /**
     * 获得指定表达式的下个值
     * @param formNoExpression 已经将日期替换后的表达式
     * @param counter  当前的计数情况， 可传 null
     * @return
     */
    public int getNextNumber(String formNoExpression, DhFormNoCounter counter) {
        int count = 0;
        while (count < 3) {
            if (!(count == 0 && counter != null)) {
                // 重新获得counter
                counter = dhFormNoCounterMapper.selectByPrimaryKey(formNoExpression);
            }
            int countRow = dhFormNoCounterMapper.updateByFormNoExpressionAndCurrentNo(formNoExpression, counter.getCurrentNo());
            if (countRow > 0) {
                return counter.getCurrentNo() + 1;
            }
            count++;
        }
        throw new PlatformException("获得表单号失败，尝试3次失败");
    }

    /**
     * 将当前号码与表达式融合
     * @param formNoExpression  已经将日期替换完成的表达式
     * @param currentNo  当前号码
     * @return
     */
    public String mergeFormNoExpressWithCurrentNo(String formNoExpression, int currentNo) {
        Pattern p = Pattern.compile("\\{num\\d\\}");
        Matcher m = p.matcher(formNoExpression);
        if (!m.find()) {
            return formNoExpression;
        } else {
            int number = Integer.parseInt(formNoExpression.substring(m.start() + 4, m.end() - 1));
            String formatStr = "%0" + number + "d";
            String finalNumber = String.format(formatStr, currentNo);
            return formNoExpression.replaceAll("\\{num\\d\\}", finalNumber);
        }
    }

    public static void main(String[] args){
        String express = "LYF-HT-{yyyy-MM-dd}-{num4}";
        Pattern p = Pattern.compile("\\{num\\d\\}");
        Matcher m = p.matcher(express);
        if (m.find()) {
            System.out.println(express.substring(m.start() + 4, m.end() - 1));
            // 补0后的位数
            int number = Integer.parseInt(express.substring(m.start() + 4, m.end() - 1));
            String formatStr = "%0" + number + "d";
            String finalNumber = String.format(formatStr, number);
            express = express.replaceAll("\\{num\\d\\}", finalNumber);
            System.out.println(express);
        }
    }
}