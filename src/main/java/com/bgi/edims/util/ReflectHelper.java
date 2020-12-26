package com.bgi.edims.util;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

public class ReflectHelper {
	/**
	 * 获取属性值
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		if (obj == null) {
			return null;
		}
		Field targetField = getTargetField(obj.getClass(), fieldName);
		try {
			return FieldUtils.readField(targetField, obj, true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取属性字段
	 * @param targetClass
	 * @param fieldName
	 * @return
	 */
	public static Field getTargetField(Class<?> targetClass, String fieldName) {
		Field field = null;
		try {
			if (targetClass == null) {
				return field;
			}
			if (Object.class.equals(targetClass)) {
				return field;
			}
			field = FieldUtils.getDeclaredField(targetClass, fieldName, true);
			if (field == null) {
				field = getTargetField(targetClass.getSuperclass(), fieldName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}
	/**
	 * 设置属性字段值
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) {
		if (null == obj) {
			return;
		}
		Field targetField = getTargetField(obj.getClass(), fieldName);
		try {
			FieldUtils.writeField(targetField, obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}