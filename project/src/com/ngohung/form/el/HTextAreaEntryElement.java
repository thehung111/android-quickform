package com.ngohung.form.el;

import com.ngohung.form.el.store.HDataStore;

// same as textenetryelement but is displayed in multiple lines
public class HTextAreaEntryElement extends HTextEntryElement{

	private int numberOfLines = 3; // default to 3 lines
	
	public HTextAreaEntryElement(String key, String label, String initVal,
			String hint, boolean required) {
		super(key, label, initVal, hint, required);
		elType = HElementType.MULTI_LINES_TEXT_ENTRY_EL; 	
	}
	
	public HTextAreaEntryElement(String key, String label,
			String hint, boolean required) {
		super(key, label, hint, required);
		elType = HElementType.MULTI_LINES_TEXT_ENTRY_EL; 	
	}
	
	public HTextAreaEntryElement(String key, String label, String hint, boolean required, HDataStore store) {
		super(key, label, hint, required, store);
		elType = HElementType.MULTI_LINES_TEXT_ENTRY_EL; 	
	}
	
	public HTextAreaEntryElement(String key, String label, String hint, boolean required, int numberOfLines) {
		super(key, label, hint, required);
		elType = HElementType.MULTI_LINES_TEXT_ENTRY_EL; 	
		this.numberOfLines = numberOfLines;
	}
	
	public HTextAreaEntryElement(String key, String label, String hint, boolean required, int numberOfLines, HDataStore store) {
		super(key, label, hint, required, store);
		elType = HElementType.MULTI_LINES_TEXT_ENTRY_EL; 	
		this.numberOfLines = numberOfLines;
	}
	
	public HTextAreaEntryElement(String key, String label, String initVal,
			String hint, boolean required, int numberOfLines) {
		super(key, label, initVal, hint, required);
		elType = HElementType.MULTI_LINES_TEXT_ENTRY_EL; 	
		this.numberOfLines = numberOfLines;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	public int getElType() {
		return HElementType.MULTI_LINES_TEXT_ENTRY_EL;
	}
	
}
