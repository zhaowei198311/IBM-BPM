package com.desmart.desmartsystem.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Dom4j {
	
	public static Object parse(Element root) {
		List<?> elements = root.elements();
		if (elements.size() == 0) {
			// 没有子元素
			return root.getTextTrim();
		} else {
			// 有子元素
			String prev = null;
			boolean guess = true; // 默认按照数组处理

			Iterator<?> iterator = elements.iterator();
			while (iterator.hasNext()) {
				Element elem = (Element) iterator.next();
				String name = elem.getName();
				if (prev == null) {
					prev = name;
				} else {
					guess = name.equals(prev);
					break;
				}
			}
			iterator = elements.iterator();
			if (guess) {
				List<Object> data = new ArrayList<Object>();
				while (iterator.hasNext()) {
					Element elem = (Element) iterator.next();
					((List<Object>) data).add(parse(elem));
				}
				return data;
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				while (iterator.hasNext()) {
					Element elem = (Element) iterator.next();
					((Map<String, Object>) data).put(elem.getName(), parse(elem));
				}
				return data;
			}
		}
	}

	public static void main(String[] args) throws Throwable {
	}
}
