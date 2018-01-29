package com.summer.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Bean2MapUtil {

	/**
	 * map转sortedMap
	 * @param map
	 * @return
	 */
	public static SortedMap<String, Object> map2sortedMap(Map<String, Object> map) {
		SortedMap<String, Object> sortedMap = new TreeMap<>();
		for (Object key : map.keySet()) {
			sortedMap.put(key.toString(), map.get(key));
		}
		return sortedMap;
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * @param clazz
	 *        要转化的类型
	 * @param map
	 *        包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *         如果分析类属性失败
	 * @throws IllegalAccessException
	 *         如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *         如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *         如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T map2Bean(Class<T> clazz, Map map) {
		T obj = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			obj = clazz.newInstance(); // 创建 JavaBean 对象
			// 给 JavaBean 对象的属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
					Object value = map.get(propertyName);
					if ("".equals(value)) {
						value = null;
					}
					Object[] args = new Object[1];
					args[0] = value;
					try {
						descriptor.getWriteMethod().invoke(obj, args);
					} catch (InvocationTargetException e) {
						e.printStackTrace();// 字段映射失败
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();// "实例化 JavaBean 失败"
		} catch (IntrospectionException e) {
			e.printStackTrace();// "分析类属性失败"
		} catch (IllegalArgumentException e) {
			e.printStackTrace();// "映射错误"
		} catch (InstantiationException e) {
			e.printStackTrace();// "实例化 JavaBean 失败"
		}
		return (T) obj;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * @param bean
	 *        要转化的JavaBean 对象
	 * @param kickOutNull
	 *        是否踢出为空的数据
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *         如果分析类属性失败
	 * @throws IllegalAccessException
	 *         如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *         如果调用属性的 setter 方法失败
	 */
	public static Map<String, Object> bean2Map(Object bean, boolean kickOutNull) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Bean2MapUtil.getMap(bean, kickOutNull, returnMap);
		return returnMap;
	}

	public static SortedMap<String, Object> bean2SortedMap(Object bean, boolean kickOutNull) {
		SortedMap<String, Object> returnMap = new TreeMap<String, Object>();
		Bean2MapUtil.getMap(bean, kickOutNull, returnMap);
		return returnMap;
	}

	private static void getMap(Object bean, boolean kickOutNull, Map<String, Object> returnMap) {
		Class<? extends Object> clazz = bean.getClass();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = null;
					result = readMethod.invoke(bean, new Object[0]);
					if (null != propertyName) {
						propertyName = propertyName.toString();
					}
					if (null != result) {
						result = result.toString();
					}
					if (kickOutNull && null == result) continue;
					returnMap.put(propertyName, result);
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();// "分析类属性失败"
		} catch (IllegalAccessException e) {
			e.printStackTrace();// "实例化 JavaBean 失败"
		} catch (IllegalArgumentException e) {
			e.printStackTrace();// "映射错误"
		} catch (InvocationTargetException e) {
			e.printStackTrace();// "调用属性的 setter 方法失败"
		}
	}

}
