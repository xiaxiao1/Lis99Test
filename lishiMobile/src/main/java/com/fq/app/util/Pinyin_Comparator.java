package com.fq.app.util;

import java.util.Comparator;

public class Pinyin_Comparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		 String str1 = PingYinUtil.getPingYin((String) o1);
	     String str2 = PingYinUtil.getPingYin((String) o2);
	     return str1.compareTo(str2);
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

}
