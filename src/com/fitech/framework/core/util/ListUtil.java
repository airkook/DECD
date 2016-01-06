package com.fitech.framework.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author 石昊东 2011-4-29
 * 
 */
public class ListUtil {

	/**
	 * 判断list是否为空
	 * 
	 * @param list
	 * @return list为空返回true,list不为空返回false
	 */
	public static boolean isEmpty(List list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}
	/**
	 * 去重list<String>
	 * 
	 * 
	 * 
	 */
	public static List distinctList(List<String> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		 //List listSrc = new ArrayList();  
	     Set set = new TreeSet();  
	      for (int i = 0; i < list.size(); i++) {
	            set.add(list.get(i));  
	        }  
	      List listDesc = new ArrayList(); 
	      Iterator it= set.iterator();
	      while (it.hasNext()) {   
	    	  String str = (String) it.next(); 
	    	  listDesc.add(str);
	    	}   

		return listDesc;
	}
	public static void main(String[] args) {
		List listSrc = new ArrayList();  
		listSrc.add("a");
		listSrc.add("b");
		listSrc.add("c");
		listSrc.add("a");
		listSrc.add("b");
		listSrc.add("a");
		 List listDesc =distinctList(listSrc);
		 for (int i = 0; i < listDesc.size(); i++) {
			System.out.println(listDesc.get(i));
		}
	}
}
