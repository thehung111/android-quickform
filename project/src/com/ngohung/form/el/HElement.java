package com.ngohung.form.el;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;

import com.ngohung.form.constant.HConstants;
import com.ngohung.form.el.listener.*;
import com.ngohung.form.el.store.HDataStore;
import com.ngohung.form.el.validator.*;

public abstract class HElement { // base element for all elements
	
	private HDataStore dataStore = null; // defined the data store use to save/load value
	protected String key; 			// unique key for this element if we need to store to SharePreferences
	protected String label; 		// label for the element
	protected String value; 		// value for this element e.g value for EditText
	protected String hint = HConstants.BLANK; 			// placeholder for edittext if necessary
	protected int elType; 			// whether is text field, multi-line, etc
	protected boolean hidden = false; // whether this element is hidden in UI
	
	// validation fields
	protected boolean required = false; 	// is this field required
	protected String requireMsg; 	// if this field is required, use this message to display
	protected int maxLength = HConstants.NOT_SPECIFIED;     // max length for elements, do maxlength
	protected int keyboardType = HConstants.NOT_SPECIFIED;  // for numeric fields field, set this to numeric keyboard. Only applicable to elements that need keyboard
	
	protected List<HValidatorListener> validators = new ArrayList<HValidatorListener>();
	protected List<HValueChangedListener> valuesChangedListener = new ArrayList<HValueChangedListener>();
	
	
	private final static String DEFAULT_REQUIRED_MSG = "Please enter %s" ;
	
	// for subclass to implement
	public abstract void loadValueForUI(View v);  		// load value of this element into its UI e.g.  load value into EditText textbox 
	public abstract void saveValueFromUI(View v);  		// when user has entered text in the UI, we need to store the changes back to this element
	public abstract ValidationStatus doValidationForUI(View v); // do validation and display errors/success in the UI if necessary. null mean that no validation needed
	
	
	// whether keyboard type has been specified in this element
	public boolean hasSpecifyKeyboardType(){
		return (keyboardType != HConstants.NOT_SPECIFIED) ;
	}
	
	// whether maxlength is specified
	// if it is need to limit max length in the UI e.g. max number of characters in EditText
	public boolean hasSpecifyMaxLength(){
		return (maxLength != HConstants.NOT_SPECIFIED) ;
	}
	
	// whether this element use the keyboard for input
	// in the form, the last element keyboard IME option will be done
	// if not last, keyboard will have next button
	public boolean isKeyboardInputRequired(){
		switch(elType){
			case HElementType.TEXT_ENTRY_EL:
			case HElementType.NUMERIC_ENTRY_EL:
			case HElementType.MULTI_LINES_TEXT_ENTRY_EL:
				return true;
				
			default:
				return false;
		}
	}
	
	
	public void addValueChangedListener(HValueChangedListener listener){
		if(listener == null)
			return;
			
		if(!valuesChangedListener.contains(listener))
			valuesChangedListener.add(listener);
	}
	
	public void removeValueChangedListener(HValueChangedListener listener){
		
		if(listener == null)
			return;
		
		if(valuesChangedListener.contains(listener))
			valuesChangedListener.remove(listener);
	}
	
	public void addValidator(HValidatorListener listener){
		
		if(listener == null)
			return;
		
		if(!validators.contains(listener))
			validators.add(listener);
	}
	
	public void removeValidator(HValidatorListener listener){
		
		if(listener == null)
			return;
		
		if(validators.contains(listener))
			validators.remove(listener);
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	
	// if we set the value from data store after loading
	// we should not trigger the events or re-saving again
	public void setValueFromStore(String newValue) {
		this.value = newValue;
	}
	
	public void setValue(String newValue) {
		
		if(value!=newValue){
			
			this.value = newValue;
			
			// notify change status to listeners
			for(HValueChangedListener listener: this.valuesChangedListener){
				listener.onValueChanged(this);
			}
		
			if(this.getDataStore()!=null){
				this.getDataStore().saveValueToStore(this);
			}
		}
	}
	
	// return validation status for various validators
	// return null if there is no validator or this field is option
	// if there is at least 1 validator or the field is required, must return a status (valid/invalid)
	// default only do required validation
	// will fail instantly for the first error
	public ValidationStatus doValidation()
	{
		ValidationStatus valStatus = new ValidationStatus(true);
		
		if(required){
			
			// do required validation here
			if( value== null || value.length() == 0)
			{
				valStatus.setValid(false);
				
				if(requireMsg!=null)
					valStatus.setMsg(requireMsg);
				else{
					
					valStatus.setMsg(String.format(DEFAULT_REQUIRED_MSG, this.label.toLowerCase()) );  // default message for required msg
				}
				return valStatus;
			}
		}
		else {
			// if user has not enter anything, we dun display the valid icon
			if(value== null || value.length() == 0)
				valStatus = null; // optional field, we will mark this validation as optional by passing null
		}
		
		// custom validator
		for(HValidatorListener v: validators)
		{
			ValidationStatus vStatus = v.isValid(this);
			if(vStatus!=null && vStatus.isInvalid() ) // fail validation
			{
				return vStatus;
			}
			
			valStatus = vStatus;
			
		}
		
		return valStatus;
	}
	
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public int getElType() {
		return elType;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getRequireMsg() {
		return requireMsg;
	}
	public void setRequireMsg(String requireMsg) {
		this.requireMsg = requireMsg;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public int getKeyboardType() {
		return keyboardType;
	}
	public void setKeyboardType(int keyboardType) {
		this.keyboardType = keyboardType;
	}
	public HDataStore getDataStore() {
		return dataStore;
	}
	public void setDataStore(HDataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	
	
	
	

	
	
}
