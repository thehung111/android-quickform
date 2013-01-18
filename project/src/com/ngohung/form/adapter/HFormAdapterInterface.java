package com.ngohung.form.adapter;

import android.view.View;
import android.view.ViewGroup;

// custom adapter must implement this interface to provide custom header UI
public interface HFormAdapterInterface {
	public View getHeaderView(int position, View convertView, ViewGroup parent) ;
	public long getHeaderId(int position) ;
	public View getView(int position, View convertView, ViewGroup parent);
}
