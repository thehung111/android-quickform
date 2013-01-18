package com.ngohung.form.el.validator;


import com.ngohung.form.el.HElement;
import com.ngohung.form.el.listener.HValidatorListener;
import com.ngohung.form.util.HStringUtil;

public class HEmailValidatorListener implements HValidatorListener{

	@Override
	public ValidationStatus isValid(HElement el) {
		if(el!=null){
			String value = el.getValue();
			
			// we only validate if there is a value
			// assumption: required validation is already triggered
			// if this field is optional, we only validate if there is a value
			if(value!=null && value.length() > 0){
				boolean isEmail = HStringUtil.isEmail(value);
				return new ValidationStatus(isEmail, "Please enter a valid email");
				
			}
			
			
		}
		return null;
	}

}
