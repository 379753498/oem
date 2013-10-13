package org.github.oem.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.github.oem.config.ConfigUtils;
import org.github.oem.config.MappingUtils;
import org.github.oem.config.OemObject;
import org.github.oem.config.OneToManyObject;
import org.github.oem.config.PropertyObject;


public class ExcelToObjectByPoi implements ExcelToObject {
	public static int dataStartRow=ConfigUtils.dataStartRow;
	public static boolean isMultiSheet=ConfigUtils.isMultiSheet;
	/**
	 * ��ȡ����Sheet
	 * @param sheet �������е�sheet
	 * @param clazz Ҫת������
	 * @return ���󼯺�
	 * @throws Exception �쳣
	 */
	public <T> List<T> readSheet(Sheet sheet,Class<T> clazz){
		try {
			List<T> list=new ArrayList<T>();
			for(int i=dataStartRow;i<=sheet.getLastRowNum();i++){
				
				OemObject oem= (OemObject) MappingUtils.configMap.get(clazz.getName());
				if(oem==null){
					oem=CommonUtils.buildOemByClass(clazz);
				}
				Row row=sheet.getRow(i);
				if(oem.getOneToManyObjects()==null){
					T obj=clazz.newInstance();
					List<T> propertys=oem.getPropertys();
					for(int j=0;j<propertys.size();j++){
						PropertyObject property=(PropertyObject) propertys.get(j);
						Cell cell=row.getCell((short) j);
						Object value=PoiUtils.getCellValue(cell, CommonUtils.getType(property.getType()));
						CommonUtils.setProperty(obj, property.getName(), value);
					}
					list.add(obj);
				}else{
					List<T> oneToManyList=oem.getOneToManyObjects();
					for(int j=0;j<oneToManyList.size();j++){
						T obj=clazz.newInstance();
						OneToManyObject oneToManyObject=(OneToManyObject) oneToManyList.get(j);
						for(int k=0;k<oneToManyObject.getPropertys().size();k++){
							PropertyObject property=(PropertyObject) oneToManyObject.getPropertys().get(k);
							if(property.getDataIndex()!=-1){
								Cell cell=row.getCell((short) property.getDataIndex());
								Object value=PoiUtils.getCellValue(cell, CommonUtils.getType(property.getType()));
								CommonUtils.setProperty(obj, property.getName(), value);
							}else{
								Object value=property.getValue();
								CommonUtils.setProperty(obj, property.getName(), value);
							}
							
							
							
						}
						list.add(obj);
					}
				}
				
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * ���������еļ�¼ת���ɶ��󼯺ϵ�����,����������ȷ�������еĸ���,���isMultiSheetΪfalse
	 * �Ļ���ֻ����һ��list��list����,��Ϊtrue��ʱ�����sheet�ĸ���������
	 * @param workbook Ҫת��Ϊ����Ĺ�����
	 * @param clazz Ҫת������
	 * @return ���鼯��
	 * @throws Exception �쳣
	 */
	public <T> List<T>[] readWorkbook(Workbook workbook,Class<T>[] clazzs) throws Exception{
		if(isMultiSheet){
			List<T>[] lists=new List[workbook.getNumberOfSheets()];
			for(int i=0;i<lists.length;i++){
				Sheet sheet=workbook.getSheetAt(i);
				lists[i]=this.readSheet(sheet, clazzs[i]);
			}
			return lists;
		}else{
			List<T>[] lists=new List[1];
			lists[0]=this.readSheet(workbook.getSheetAt(0), clazzs[0]);
			return lists;
		}
	}
	/**
	 * ��ȡsheet,��֮ת��Ϊָ�����󼯺�,�÷�����Ҫ���һ�����Ӧ���ű��ʱ��ʹ��
	 * @param sheet �������е�sheet
	 * @param clazz ָ������
	 * @param className �����ļ��е�����
	 * @return ���󼯺�
	 * @throws Exception �쳣
	 */
	public List readSheet(HSSFSheet sheet,Class clazz,String className) throws Exception{
		List list=new ArrayList();
		for(int i=dataStartRow;i<=sheet.getLastRowNum();i++){
			OemObject oem= (OemObject) MappingUtils.configMap.get(className);
			HSSFRow row=sheet.getRow(i);
			if(oem.getOneToManyObjects()==null){
				Object obj=CommonUtils.getNewObject(clazz);
				List propertys=oem.getPropertys();
				for(int j=0;j<propertys.size();j++){
					PropertyObject property=(PropertyObject) propertys.get(j);
					HSSFCell cell=row.getCell((short) j);
					Object value=PoiUtils.getCellValue(cell, CommonUtils.getType(property.getType()));
					CommonUtils.setProperty(obj, property.getName(), value);
				}
				list.add(obj);
			}else{
				List oneToManyList=oem.getOneToManyObjects();
				for(int j=0;j<oneToManyList.size();j++){
					Object obj=CommonUtils.getNewObject(clazz);
					OneToManyObject oneToManyObject=(OneToManyObject) oneToManyList.get(j);
					for(int k=0;k<oneToManyObject.getPropertys().size();k++){
						PropertyObject property=(PropertyObject) oneToManyObject.getPropertys().get(k);
						if(property.getDataIndex()!=-1){
							HSSFCell cell=row.getCell((short) property.getDataIndex());
							Object value=PoiUtils.getCellValue(cell, CommonUtils.getType(property.getType()));
							CommonUtils.setProperty(obj, property.getName(), value);
						}else{
							Object value=property.getValue();
							CommonUtils.setProperty(obj, property.getName(), value);
						}
					}
					list.add(obj);
				}
			}
			
		}
		return list;
	}
	public List[] readExcel(String filePath,Class[] classes){
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(filePath);
			XSSFWorkbook workbook=new XSSFWorkbook(fis);
			ExcelToObjectByPoi reader=new ExcelToObjectByPoi();
			List[] lists=reader.readWorkbook(workbook, classes);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
