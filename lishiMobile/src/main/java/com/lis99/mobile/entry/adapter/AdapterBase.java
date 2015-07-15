package com.lis99.mobile.entry.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
  *
  * ��Ŀ��ƣ�GreenArk
  * ����ƣ�AdapterBase
  * ������Adapter����
  * �����ˣ�������
  * ����ʱ�䣺2014-4-14 ����11:39:13
  * @version
  *
 */
public abstract class AdapterBase<T> extends BaseAdapter {
	public List<T> list;

	public List<T> getList() {
		return list;
	}
	// ��4���벼��
	private LayoutInflater inflater = null;

/**
 * 
 * @param act 
 * @param list ��Ӧ��list
 */
	public AdapterBase(Activity act, List<T> list) {
		this.list = list == null ? new ArrayList<T>() : new ArrayList<T>(list);
		inflater = LayoutInflater.from(act);
	}
/**
 * ��Ӳ���
 * @param list
 */
	public void setData(List<T> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}
/**
 * ��ӵ������
 * @param elem
 */
	public void add(T elem) {
		list.add(elem);
		notifyDataSetChanged();
	}
/**
 * ���ȫ������
 * @param elem
 */
	public void addAll(List<T> elem) {
		list.addAll(elem);
		notifyDataSetChanged();
	}
/**
 * ��ָ���Ķ����滻Ϊָ���Ķ���
 * @param oldElem 
 * @param newElem
 */
	public void set(T oldElem, T newElem) {
		set(list.indexOf(oldElem), newElem);
	}
/**
 * ����ָ��
 * @param index
 * @param elem
 */
	public void set(int index, T elem) {
		list.set(index, elem);
		notifyDataSetChanged();
	}
/**
 * ȥ��ָ���Ķ���
 * @param elem
 */
	public void remove(T elem) {
		list.remove(elem);
		notifyDataSetChanged();
	}
/**
 * ȥ��ָ������һ�����
 * @param index
 */
	public void remove(int index) {
		list.remove(index);
		notifyDataSetChanged();
	}
/**
 * �����Ƿ�����б��ָ���Ķ���
 * @param elem
 * @return
 */
	public boolean contains(T elem) {
		return list.contains(elem);
	}
/**
 * ���adapter���
 */
	public void clearData() {
		list.clear();
		notifyDataSetChanged();
	}
/**
 * ��ȡadapter����
 */
	@Override
	public int getCount() {
		return list.size();
	}
/**
 * ��ȡ�ƶ���Item��type
 */
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return super.getViewTypeCount();
	}
   /**
    * ��ȡָ����adapter
    */
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
   /**
    * ��ȡitemid
    */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 * ��badapter
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getExView(position, convertView, parent, inflater);
	}
    /**
     * ����
     * @param position
     * @param convertView  
     * @param parent
     * @param inflater
     * @return
     */
	protected abstract View getExView(int position, View convertView,
			ViewGroup parent, LayoutInflater inflater);
}
