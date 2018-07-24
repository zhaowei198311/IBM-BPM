package com.desmart.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.desmart.common.annotation.ExcelBigDecimal;
import com.desmart.common.annotation.ExcelBoolean;
import com.desmart.common.annotation.ExcelDate;
import com.desmart.common.annotation.ExcelField;
import com.desmart.common.annotation.ExcelValid;
import com.desmart.common.constant.ExcelHelper;
import com.desmart.common.constant.ServerResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 动态反射及转换的实体类
 * @author lys
 *
 */
public class ExcelUtil {
	public static ExcelUtil excelUitl = null;


	public static ExcelUtil getInstance() {
	if (excelUitl == null) {
		excelUitl = new ExcelUtil();
	}
		return excelUitl;
	}

	/**
	* 读取注解值、字段名及字段的类型
	* 
	* @param className
	* @return
	* @throws Exception
	*/
	public Map<Integer, ExcelHelper> loadExcelAnnotationFieldVlaue(Class<?> className) throws Exception {
		Map<String, ExcelHelper> temp = new HashMap<String, ExcelHelper>();
		Field[] fields = className.getDeclaredFields();
		for (Field field : fields) {
			if (!field.getName().equals("serialVersionUID")) {
				if (!field.isAccessible())
					field.setAccessible(true);

				ExcelHelper helper = new ExcelHelper();
				Type type = field.getGenericType();
				if (type instanceof Class<?>) {
					Class<?> cls = (Class<?>) type;
					helper.setClazz(cls);
				}
				helper.setFieldName(field.getName());
				temp.put(field.getName(), helper);
				Annotation[] ans = field.getAnnotations();
				for (Annotation annotation : ans) {
					if (annotation.annotationType().equals(ExcelField.class)) {
						ExcelField fd = field.getAnnotation(ExcelField.class);
						temp.get(field.getName()).setSort(fd.sort());
						temp.get(field.getName()).setName(fd.name());
						temp.get(field.getName()).setNullable(fd.nullable());
					} else if (annotation.annotationType().equals(ExcelBoolean.class)) {
						ExcelBoolean fd = field.getAnnotation(ExcelBoolean.class);
						temp.get(field.getName()).setFalseName(fd.False().toString());
						temp.get(field.getName()).setTrueName(fd.True().toString());
					} else if (annotation.annotationType().equals(ExcelDate.class)) {
						ExcelDate fd = field.getAnnotation(ExcelDate.class);
						temp.get(field.getName()).setFormat(fd.format());
					} else if (annotation.annotationType().equals(ExcelValid.class)) {
						ExcelValid fd = field.getAnnotation(ExcelValid.class);
						temp.get(field.getName()).setRegexp(fd.regexp());
					} else if (annotation.annotationType().equals(ExcelBigDecimal.class)) {
						ExcelBigDecimal fd = field.getAnnotation(ExcelBigDecimal.class);
						temp.get(field.getName()).setScale(fd.scale());
					}
				}
			}
		}

		Map<Integer, ExcelHelper> map = new HashMap<>();
		for (Map.Entry<String, ExcelHelper> m : temp.entrySet()) {
			map.put(m.getValue().getSort(), m.getValue());
		}

		return map;
	}
	
	/**
	* 获取Excel显示的中文名及排列的顺序
	* 
	* @param className
	* @return
	*/
	public Map<Integer, String> getExcelFieldName(Class<?> className) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		Field[] fields = className.getDeclaredFields();
		for (Field field : fields) {
			if (!field.getName().equals("serialVersionUID")) {

				if (field.isAnnotationPresent(ExcelField.class)) {
					ExcelField fd = field.getAnnotation(ExcelField.class);
					if (!field.isAccessible())
						field.setAccessible(true);
					map.put(fd.sort(), fd.name());
				}
			}
		}
		return map;
	}

	/**
	* 比较Excel的头字段与实体类的showname数量、名称及顺序是否一致,headNum从0开始
	* @param sheet
	* @param map
	* @param headNum
	* @return
	*/
	public boolean equalsArrays(Sheet sheet, Map<Integer, String> map,Integer headNum) {
		boolean check = true;
		//for (int k = 0; k < sheet.getColumns(); k++) {
		for (int k = 0; k < map.keySet().size(); k++) {
			if (!sheet.getCell(k, headNum).getContents().equals(map.get(k))) {//目前写死，第5行是头部
				check = false;
				break;
			}
		}
		return check;
	}
	
	/**
	* 校验实体类与Excel的字段是否是相同类型
	* 
	* @param sheet
	* @param clazz
	* @param headNum 从0开始
	* @return
	* @throws Exception
	*/
	public String checkExcelContent(Sheet sheet, Class<?> clazz,Integer headNum) throws Exception {
		StringBuilder result = new StringBuilder();
		result.append("");
		int size = sheet.getRows();
		Cell[] heads = sheet.getRow(headNum);//目前写死，第5行为头部
		Map<Integer, ExcelHelper> map = loadExcelAnnotationFieldVlaue(clazz);
		for (int i = headNum+1; i < size; i++) {//目前写死，从第6行开始检验
			Cell[] cells = sheet.getRow(i);
			int len = cells.length;
			for (int j = 0; j < len; j++) {
				boolean warnning = false;
				ExcelHelper helper = map.get(j);
				// 判断字段内容是否为非空字段
				if (!helper.isNullable()) {
					if (!Validator.isEffective(cells[j].getContents())) {
						warnning = true;
					}
				}

				if (!warnning) {
					// 判断字段注解是否存在规则过滤
					if (Validator.isEffective(cells[j].getContents())) {

						if (Validator.isEffective(helper.getRegexp())) {
							if (!Validator.match(helper.getRegexp(), cells[j].getContents())) {
								warnning = true;
							}
						}
					}
				}

				if (!warnning) {
					if (Date.class.isAssignableFrom(helper.getClazz())) {
						if (Validator.isEffective(cells[j].getContents())) {
							if (!Validator.isValidDate(cells[j].getContents(), helper.getFormat())) {
								warnning = true;
							}
						}
					} else if (Boolean.class.isAssignableFrom(helper.getClazz())) {

						if (!(cells[j].getContents().equals(helper.getFalseName())
								|| cells[j].getContents().equals(helper.getTrueName()))) {
							warnning = true;
						}
					} else if (Integer.class.isAssignableFrom(helper.getClazz())) {
						if (!Validator.IsNumber(cells[j].getContents())) {
							warnning = true;
						}
					} else if (BigDecimal.class.isAssignableFrom(helper.getClazz())) {
						String regexp = "^[+-]?[0-9]+(.[0-9]{1," + (helper.getScale() != null ? helper.getScale() : 2)
								+ "})?$";
						if (!(Validator.match(regexp, cells[j].getContents()))) {
							warnning = true;
						}
					}

				}

				if (warnning) {
					if (result.toString().indexOf(heads[j].getContents()) == -1) {
						result.append("[" + heads[j].getContents()+":第"+(i+1)+"行第"+(j+1)+"列出现错误"+ "]").append(",");
					}
				}
			}
		}
		return result.toString();
	}

	/**
	* 将Excel的内容转换成实体类
	* 
	* @param sheet
	* @param clazz
	* @param headNum 头部行数 从0开始
	* @return
	* @throws Exception
	*/
	public <T> List<T> importExcelToEntity(Sheet sheet, Class<T> clazz,Integer headNum) throws Exception {
		List<T> list = new ArrayList<>();
		Map<Integer, ExcelHelper> map = loadExcelAnnotationFieldVlaue(clazz);
		int size = sheet.getRows();
		for (int i = headNum+1; i < size; i++) {
			Cell[] cells = sheet.getRow(i);
			int len = cells.length;
			T t = (T) clazz.newInstance();
			for (int j = 0; j < len; j++) {
				ExcelHelper helper = map.get(j);
				Field f = t.getClass().getDeclaredField(helper.getFieldName());
				if (!f.isAccessible())
					f.setAccessible(true);
				if (Date.class.isAssignableFrom(helper.getClazz())) {
					if (Validator.isEffective(cells[j].getContents())) {
						f.set(t, new SimpleDateFormat(helper.getFormat()).parse(cells[j].getContents().toString()));
					} else {
						f.set(t, null);
					}
				} else if (BigDecimal.class.isAssignableFrom(helper.getClazz())) {
					f.set(t, BigDecimal.valueOf(Double.valueOf(cells[j].getContents().toString()))
							.setScale(helper.getScale() != null ? helper.getScale() : 2, BigDecimal.ROUND_HALF_UP));
				} else if (Boolean.class.isAssignableFrom(helper.getClazz())) {
					f.set(t, cells[j].getContents().toString().equals(helper.getTrueName()) ? true : false);
				} else if (String.class.isAssignableFrom(helper.getClazz())) {
					f.set(t, cells[j].getContents().toString());
				} else if (Integer.class.isAssignableFrom(helper.getClazz())) {
					f.set(t, Integer.valueOf(cells[j].getContents().toString()));
				}
			}
			list.add(t);
		}
		return list;
	}

	/**
	 * 将实体类转换成List<Map>格式以便输出到Excel
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public <T> List<Map<Integer, String>> exportExcel(List<T> list) throws Exception {
		Class<?> cls = list.get(0).getClass();
		List<Map<Integer, String>> data = new ArrayList<>();
		Map<Integer, ExcelHelper> helper = loadExcelAnnotationFieldVlaue(cls);
		// 存放Excel文件头部的字段中文名
		Map<Integer, String> title = new HashMap<>();
		Set<Integer> set = helper.keySet();
		for (Integer key : set) {
			title.put(key, helper.get(key).getName());
		}
		data.add(title);

		// 存放Excel的内容
		for (T l : list) {
			Map<Integer, String> contentMap = new HashMap<>();
			for (int i = 0; i < helper.size(); i++) {
				ExcelHelper excelHelper = helper.get(i);
				Field f = cls.getDeclaredField(excelHelper.getFieldName());
				if (!f.isAccessible())
					f.setAccessible(true);
				if (String.class.isAssignableFrom(excelHelper.getClazz())) {
					contentMap.put(i, f.get(l) != null ? String.valueOf(f.get(l)) : "");
				} else if (Date.class.isAssignableFrom(excelHelper.getClazz())) {
					contentMap.put(i, new SimpleDateFormat(excelHelper.getFormat()).format(f.get(l)));
				} else if (BigDecimal.class.isAssignableFrom(excelHelper.getClazz())) {
					int scale = (excelHelper.getScale() != null && excelHelper.getScale().intValue() > 0)
							? excelHelper.getScale().intValue()
							: 2;
					contentMap.put(i, String.valueOf(BigDecimal.valueOf(Double.valueOf(String.valueOf(f.get(l))))
							.setScale(scale, BigDecimal.ROUND_HALF_UP)));
				} else if (Boolean.class.isAssignableFrom(excelHelper.getClazz())) {
					contentMap.put(i, Boolean.valueOf(f.get(l).toString()) ? excelHelper.getTrueName()
							: excelHelper.getFalseName());
				} else if (Integer.class.isAssignableFrom(excelHelper.getClazz())) {
					contentMap.put(i, String.valueOf(Integer.valueOf(f.get(l).toString())));
				}
			}
			data.add(contentMap);
		}

		return data;
	}
	
	/**
	 * 验证Excel文件首部的列名及排列顺序是否跟定义的类一致
	 * 
	 * @param file
	 * @param headNum 从0开始
	 * @return
	 */
	public static boolean checkExcelTitleAndSort(File file,Class claszz,Integer headNum) {
		InputStream stream = null;
		Workbook rwb = null;
		Boolean check = false;
		try {
			stream = new FileInputStream(file);
			// 获取Excel文件对象
			rwb = Workbook.getWorkbook(stream);
			// 获取文件的指定工作表 默认的第一个
			Sheet sheet = rwb.getSheet(0);
			Map<Integer, String> titleAndSortMap = ExcelUtil.getInstance().getExcelFieldName(claszz);
			check = ExcelUtil.getInstance().equalsArrays(sheet, titleAndSortMap,headNum);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (rwb != null) {
				rwb.close();
			}
		}
		return check;
	}

	/**
	 * 验证Excel文件的内容格式是否正确
	 * 
	 * @param file
	 * @param headNum 从0开始
	 * @return
	 */
	public static ServerResponse checkExcelContent(File file,Class claszz,Integer headNum) {
		InputStream stream = null;
		Workbook rwb = null;
		String result = "";
		//Boolean check = false;
		try {
			stream = new FileInputStream(file);
			// 获取Excel文件对象
			rwb = Workbook.getWorkbook(stream);
			// 获取文件的指定工作表 默认的第一个
			Sheet sheet = rwb.getSheet(0);
			// 如有验证失败，该方法会返回错字段的字段名称
			result = ExcelUtil.getInstance().checkExcelContent(sheet, claszz,headNum);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (rwb != null) {
				rwb.close();
			}
		}
		// 如果没有返回错误字段，表示验证通过
		if (!Validator.isEffective(result)) {
			return ServerResponse.createBySuccess();
		} else {
			return ServerResponse.createByErrorMessage(result);
		}
	}
	
	public static ServerResponse<Sheet> loadSheet(InputStream inputStream) {
		Workbook rwb = null;
		try {
			// 获取Excel文件对象
			rwb = Workbook.getWorkbook(inputStream);
			// 获取文件的指定工作表 默认的第一个
			Sheet sheet = rwb.getSheet(0);
			return ServerResponse.createBySuccess(sheet);
		}catch (Exception e) {
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}

}
