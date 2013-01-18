package com.ngohung.form.el.listener;

import com.ngohung.form.el.HElement;
import com.ngohung.form.el.validator.ValidationStatus;

public interface HValidatorListener {
	ValidationStatus isValid(HElement el);
}
