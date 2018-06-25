package com.desmart.desmartsystem.util;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 遍历xml所有节点（包括子节点下还有子节点多层嵌套）
 */
public class TestXML {

	/**
	 * 获取文件的xml对象，然后获取对应的根节点root
	 */
	public static void testGetRoot(String xml,List<String> stringList) throws Exception {
		final Document document = DocumentHelper.parseText(xml); 
		final Element root = document.getRootElement();// 获取根节点
		getResponseNodes(root,stringList);
	}
	
	public static void getResponseNodes(final Element node,List<String> stringList) {
		stringList.add(node.getName());
		final List<Element> listElement = node.elements();
		for (final Element e : listElement) {
			getResponseNodes(e,stringList);
		}
	}
}
