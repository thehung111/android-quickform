package com.ngohung.form.el.store;

import com.ngohung.form.el.HElement;

// define main methods to save data or load value from store
public interface HDataStore {
	public void saveValueToStore(HElement el); // save value of this element to the store
	public void loadValueFromStore(HElement el); // load value of this element from the store
}
