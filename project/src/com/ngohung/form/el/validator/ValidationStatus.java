package com.ngohung.form.el.validator;

import com.ngohung.form.constant.HConstants;

public class ValidationStatus {

	// store the result of validation and error/success message if there is any
	protected String msg;
	protected boolean isValid;
	
	public ValidationStatus(boolean isValid) {
		this.msg = HConstants.BLANK;
		this.isValid = isValid;
	}
	
	public ValidationStatus(boolean isValid, String msg) {
		this.msg = msg;
		this.isValid = isValid;
	}
	
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isInvalid() {
		return (!isValid);
	}
	
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
}
