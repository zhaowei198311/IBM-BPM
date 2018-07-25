package com.desmart.desmartportal.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.DateTimeUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.dao.DhFormNoCounterMapper;
import com.desmart.desmartportal.entity.DhFormNoCounter;
import com.desmart.desmartportal.service.DhFormNoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public JSONArray updateFormNoListJsonObject(DhStep formStep, JSONArray oldFormNoJSONArray) {
        JSONArray result = null;
        String formUid = formStep.getStepObjectUid();
        if (StringUtils.isBlank(formUid)) {
            throw new PlatformException("生成变单号失败，表单不存在");
        }
        BpmForm bpmForm = bpmFormManageMapper.queryFormByFormUid(formUid);
        if (bpmForm == null) {
            throw new PlatformException("生成变单号失败，表单不存在");
        }
        String formNo = null;
        boolean exist = false;
        if (oldFormNoJSONArray == null || oldFormNoJSONArray.size() == 0) {
            // 创建新的formNoList并返回
            formNo = generateFormNo(bpmForm.getFormNoExpression());
            JSONArray newJsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("formUid", formUid);
            jsonObject.put("formNo", formNo);
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

/*

生成规则中可填入任意合法字符，其中的变量请用“{”，“}”
包起来，变量暂时支持:{yyyy-MM-dd}、{num数字}，
num后面的数字代表位数，注意区分大小写
规则：LYF-HT-{yyyy-MM-dd}-{num5} --> 编号：LYF-HT-20180808-00001(00001自增长)

 */


    /**
     * 根据表单号生成规则生成一个表单号
     * @param formNoExpression 表单号生成表达式
     * @return
     */
    private String generateFormNo(String formNoExpression) {
        int affectRow = 0;
        DhFormNoCounter newCount = new DhFormNoCounter();

        if (StringUtils.isBlank(formNoExpression)) {
            throw new PlatformException("表单号生成规则为空");
        }
        if (formNoExpression.contains("{yyyy-MM-dd}")) {
            formNoExpression = formNoExpression.replace("{yyyy-MM-dd}", DateTimeUtil.dateToStr(new Date(), "yyyy-MM-dd"));
            newCount.setIsDaily(Const.Boolean.TRUE);
        }
        newCount.setFormNoExpression(formNoExpression);
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
    private int getNextNumber(String formNoExpression, DhFormNoCounter counter) {
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