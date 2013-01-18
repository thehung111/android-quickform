package com.ngohung.form.el;

import com.ngohung.form.el.store.HDataStore;

import android.text.InputType;

public class HNumericElement  extends HTextEntryElement{

	public HNumericElement(String key, String label, String initVal,
			String hint, boolean required) {
		super(key, label, initVal, hint, required);
		
		elType = HElementType.NUMERIC_ENTRY_EL;
		
		// set keyboard to be numeric keyboard
		this.keyboardType = InputType.TYPE_CLASS_PHONE;
		
	}
	
	public HNumericElement(String key, String label, String hint, boolean required) {
		super(key, label, hint, required);
		
		elType = HElementType.NUMERIC_ENTRY_EL;
		
		// set keyboard to be numeric keyboard
		this.keyboardType = InputType.TYPE_CLASS_PHONE;
		
	}
	
	public HNumericElement(String key, String label, String hint, boolean required, HDataStore store) {
		super(key, label, hint, required, store);
		
		elType = HElementType.NUMERIC_ENTRY_EL;
		
		// set keyboard to be numeric keyboard
		this.keyboardType = InputType.TYPE_CLASS_PHONE;
		
	}
	
	
	
	public int getElType() {
		return HElementType.NUMERIC_ENTRY_EL;
	}

}
