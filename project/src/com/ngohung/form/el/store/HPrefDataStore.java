package com.ngohung.form.el.store;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ngohung.form.constant.HConstants;
import com.ngohung.form.el.HElement;

// store element values to SharedPreferences data store
public class HPrefDataStore implements HDataStore{

	private SharedPreferences pref;
	
	public HPrefDataStore(SharedPreferences pref)
	{
		this.pref = pref;
	}
	
	
	@Override
	public void saveValueToStore(HElement el) {
		if(pref!=null && el!=null && el.getKey()!=null){
			// store to sharepreferences
			Editor editor = pref.edit();
			editor.putString(el.getKey(), el.getValue() );
			editor.commit();
			
//			Log.i("HElement", "save value to store - key: " + el.getKey() + ", val : " + el.getValue() );
		}
		
	}

	@Override
	public void loadValueFromStore(HElement el) {
		if(el!=null && el.getKey()!=null)
		{
			String key = el.getKey();
			String storeValue = pref.getString(key, HConstants.BLANK);
//			Log.i("HElement", "load value from store - key: " + el.getKey() + ", val : " + storeValue);
			el.setValueFromStore(storeValue);
		}
	}

	public SharedPreferences getPref() {
		return pref;
	}

	public void setPref(SharedPreferences pref) {
		this.pref = pref;
	}

	
}
