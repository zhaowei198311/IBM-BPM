package com.desmart.desmartbpm.mongo;

import com.desmart.desmartbpm.entity.LockedTask;
import com.desmart.desmartbpm.entity.OpenedTask;

import java.util.List;

/**
 * 用来保存键值类型的数据，数据结构：{"_id": "name", "value": "张三"}   {"_id": "age", "value": 12}
 */
public interface CommonMongoDao {
	public static final String EXPOSED_ITEM_RELOAD_TIME = "exposedItemReloadTime";


	/**
	 * 设置键值，如果这个键已经有值，覆盖掉原值, 保存到commonKeyValue集合
	 * @param key
	 * @param value  整数型或Stirng类型的值
	 * @return
	 */
	int set(String key, Object value);

	/**
	 * 根据key，获得Intger类型的值, 保存到commonKeyValue集合
	 * @param key
	 * @return
	 */
	Integer getIntValue(String key);

	/**
	 * 根据key获得String类型的值, 保存到commonKeyValue集合
	 * @param key
	 * @return
	 */
	String getStringValue(String key);

	/**
	 * 移除指定的键和对应的值, 保存到commonKeyValue集合
	 * @param key
	 * @return
	 */
	int remove(String key);


	/**
	 * 设置键值，如果这个键已经有值，覆盖掉原值
	 * @param key
	 * @param value  整数型或Stirng类型的值
	 * @param collectionName 集合名
	 * @return
	 */
	int set(String key, Object value, String collectionName);

	/**
	 * 根据key，获得Intger类型的值
	 * @param key
	 * @param collectionName 集合名
	 * @return
	 */
	Integer getIntValue(String key, String collectionName);

	/**
	 * 根据key获得String类型的值
	 * @param key
	 * @param collectionName 集合名
	 * @return
	 */
	String getStringValue(String key, String collectionName);

	/**
	 * 移除指定的键和对应的值
	 * @param key
	 * @param collectionName 集合名
	 * @return
	 */
	int remove(String key, String collectionName);

}
