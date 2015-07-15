/** 
 * 文件名：InitInterface.java
 * 版本信息�?
 * 日期�?013-11-4
 */

package com.lis99.mobile.engine.base;

/**
 * 
  *
  * ��Ŀ��ƣ�GreenArk
  * ����ƣ�InitInterface
  * �������������Ҫ���������
  * �����ˣ�������
  * ����ʱ�䣺2014-4-14 ����01:44:10
  * @version
  *
 */
public interface InitInterface {

	/**
	 * ��ȡ����android��layout
	 * @return
	 */
	int getLayoutId();
    
	/**
	 * �ؼ���ִ��
	 */
	void setListener();

    /**
     * �������.�������
    */
	void initData();
}
