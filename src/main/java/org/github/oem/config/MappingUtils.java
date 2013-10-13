package org.github.oem.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class MappingUtils {
	public static String path=ConfigUtils.rb.getString("propertyFilePath");
	public static Map configMap=new HashMap();
	static{
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void init() throws Exception{
		File file=new File(path);
		File[] files=file.listFiles();
		SAXReader saxReader = new SAXReader();
		
		for(int i=0;i<files.length;i++){
			if(files[i].getName().endsWith(".xml")){
				Document document = saxReader.read(files[i]);
				String className=document.getRootElement().element("class").attribute("name").getText();
				OemObject oem=getOemObjects(document);
				configMap.put(className, oem);	
			}
		}
	}
	/**
	 * �õ�Oem����
	 * @param document ÿһ��ӳ���ļ���Ӧ���ĵ�����
	 * @return Oem����
	 */
	public static OemObject getOemObjects(Document document){
//		List oemList=new ArrayList();
		OemObject oem=new OemObject();
		String className=document.getRootElement().element("class").attribute("name").getText();
		oem.setClassName(className);
		//�ж��Ƿ�����ת�е����
		Element element=(Element) document.getRootElement().element("class").elements().get(0);
		if(!"one-to-many".equals(element.getName())){
			List propertys=getPropertys(document.getRootElement().element("class"));
			oem.setPropertys(propertys);
		}else{
			List oneToManyObjects=new ArrayList();
			List oneToManyElemList=document.getRootElement().element("class").elements();
			for(int i=0;i<oneToManyElemList.size();i++){
				Element ele=(Element)oneToManyElemList.get(i);
				OneToManyObject oneToManyObject=getPropertysForOneToMany(ele);
				oneToManyObjects.add(oneToManyObject);
			}
			oem.setOneToManyObjects(oneToManyObjects);
		}
		return oem;
	}
	/**
	 * �õ����Զ���,�����򵥵����
	 * @param element class�ڵ�
	 * @return ���Զ��󼯺�
	 */
	public static List getPropertys(Element element){
		List elemList=element.elements();
		
		List propertyList=new ArrayList();
		for(int i=0;i<elemList.size();i++){
			Element elem=(Element) elemList.get(i);
			PropertyObject property=new PropertyObject();
			String name=elem.attribute("name").getText();
			String type=elem.attribute("type").getText();
			property.setName(name);
			property.setType(type);
			propertyList.add(property);
		}
		return propertyList;
	}
	/**
	 * �õ����Լ���,�����ת�е����
	 * @param element class�ڵ�
	 * @return ���Զ��󼯺�
	 */
	public static OneToManyObject getPropertysForOneToMany(Element element){
		List elemList=element.elements();
		OneToManyObject oneToManyObject=new OneToManyObject();
		List propertyList=new ArrayList();
		for(int i=0;i<elemList.size();i++){
			Element elem=(Element) elemList.get(i);
			PropertyObject property=new PropertyObject();
			String name=elem.attribute("name").getText();
			String type=elem.attribute("type").getText();
			int dataIndex=-1;//����������Ĭ��ֵ
			if(elem.attribute("dataIndex")!=null){
				dataIndex=Integer.parseInt(elem.attribute("dataIndex").getText());
			}
			
			String value=null;
			if(elem.attribute("value")!=null){
				value=elem.attribute("value").getText();
			}
			
			property.setName(name);
			property.setType(type);
			property.setDataIndex(dataIndex);
			property.setValue(value);
			propertyList.add(property);
		}
		oneToManyObject.setPropertys(propertyList);
		return oneToManyObject;
	}
}
