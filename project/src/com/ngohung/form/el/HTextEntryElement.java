package com.ngohung.form.el;

import com.ngohung.form.constant.HConstants;
import com.ngohung.form.el.store.HDataStore;
import com.ngohung.form.el.validator.ValidationStatus;

import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class HTextEntryElement extends HElement implements OnFocusChangeListener{ // when focus change we will do validation

	protected int elType = HElementType.TEXT_ENTRY_EL; 	
	
	
	public HTextEntryElement(String key, String label, String hint, boolean required)
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
	}
	
	public HTextEntryElement(String key, String label, String initVal, String hint, boolean required)
	{
		this.key = key;
		this.label = label;
		this.value = initVal;
		this.hint = hint;
		this.required = required;
	}
	
	public HTextEntryElement(String key, String label, String hint, boolean required, HDataStore store)
	{
		this.key = key;
		this.label = label;
		this.hint = hint;
		this.required = required;
		
		if(store!=null)
		{
			store.loadValueFromStore(this); // load value from store
			this.setDataStore(store);
		}
		else
			this.value = HConstants.BLANK;
		
		
		
	}
	
	@Override
	public void loadValueForUI(View v) {
		
		if(v instanceof EditText) // assumption.. TextEntryElement will be represented by TextView and EditText in UI
		{
			EditText editText = (EditText) v;
			if(value != null)
				editText.setText(value);
			editText.setHint(hint);
		}
		
	}

	@Override
	public void saveValueFromUI(View v) {
		if( !(v instanceof EditText) ) // assumption.. TextEntryElement will be represented by TextView and EditText in UI
			return;
		
		EditText editText = (EditText) v;
		this.setValue(editText.getText().toString().trim() );
		
	}

	@Override
	public ValidationStatus doValidationForUI(View v) {
		if( !(v instanceof EditText) )
			return null;
		
		EditText editText = (EditText) v;
		
		// do validation here
		ValidationStatus vStatus = doValidation();
		
		// display error validation
		if(vStatus!=null && (!vStatus.isValid())  ){
			
			editText.setError( vStatus.getMsg() );
		}
		else{
			if(required || (vStatus!=null && vStatus.isValid() ) ) // only do this for required field , if optional field, only when condition is satisfied
			{
				editText.setError(null);	
			}
		}
		
		return vStatus;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// do validation when EditText lose focus
		if(v instanceof EditText){
			
			if(!hasFocus){
				
				// save data first
				saveValueFromUI(v);
				
				// validation if necessary
				doValidationForUI(v);
				
			}
			
		}
		
	}
	
	public int getElType() {
		return HElementType.TEXT_ENTRY_EL;
	}

}
