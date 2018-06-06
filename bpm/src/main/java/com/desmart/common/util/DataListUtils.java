package com.desmart.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
