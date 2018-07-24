package com.desmart.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.desmart.desmartsystem.entity.SysUser;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.desmart.desmartbpm.entity.DhStep;

public class DataListUtils {
    private static Logger LOG = LoggerFactory.getLogger(DataListUtils.class);

    public DataListUtils() {
    }
    
    /**
     * 克隆集合，集合内是实体对象
     * @param srcList 源集合
     * @param cls 源集合内的对象Class
     * @return
     */
    public static <T> List<T> cloneList(List<T> srcList, Class<?> cls) {
        List<T> tmpMetaList = new ArrayList();
        if (!CollectionUtils.isEmpty(srcList)) {
            try {
                Iterator var4 = srcList.iterator();

                while(var4.hasNext()) {
                    T item = (T)var4.next();
                    T tmpObj = (T) cls.newInstance();
                    BeanUtils.copyProperties(item, tmpObj);
                    tmpMetaList.add(tmpObj);
                }
            } catch (Exception var6) {
                LOG.error("克隆数据列表时发生异常！", var6);
            }
        }

        return tmpMetaList;
    }

    /**
     * 将字符串集合转化为字符串，以";" 分隔，";" 结尾, 如果是空集合或者null返回 空串
     * @param stringList
     * @return
     */
    public static String transformToStringSplitWithSemicolon(List<String> stringList) {
        String result = "";
        if (stringList == null) {
            return result;
        }

        for (String str : stringList) {
            result += str + ";";
        }
        return result;
    }

    /**
     * 将用户列表转换成用户id列表
     * @param userList
     * @return
     */
    public static List<String> transformUserListToUserIdList(List<SysUser> userList) {
        List<String> result = new ArrayList<>();
        for (SysUser user : userList) {
            result.add(user.getUserUid());
        }
        return result;
    }

    /**
     * 将用户列表转换成用户姓名列表
     * @param userList
     * @return
     */
    public static List<String> transformUserListToUserNameList(List<SysUser> userList) {
        List<String> result = new ArrayList<>();
        for (SysUser user : userList) {
            result.add(user.getUserName());
        }
        return result;
    }

    /**
     * 用户列表转换成用户id字符串  uid1;uid2;
     * @param userList
     * @return
     */
    public static String transformUserListToUserIdStr(List<SysUser> userList) {
        StringBuilder sb = new StringBuilder();
        for (SysUser user : userList) {
            sb.append(user.getUserUid()).append(";");
        }
        return sb.toString();
    }

    /**
     * 用户列表转换成用户姓名字符串  name1;name2;
     * @param userList
     * @return
     */
    public static String transformUserListToUserNameStr(List<SysUser> userList) {
        StringBuilder sb = new StringBuilder();
        for (SysUser user : userList) {
            sb.append(user.getUserName()).append("(").append(user.getUserUid()).append(");");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<DhStep> list = new ArrayList<DhStep>();
        list.add(new DhStep("1","2", "3"));
        list.add(new DhStep("2","2", "3"));
        List<DhStep> list2 = DataListUtils.cloneList(list, DhStep.class );
        System.out.println(list2.size());
        for(DhStep s:list2) {
            System.out.println(s);
        }
    }
}
