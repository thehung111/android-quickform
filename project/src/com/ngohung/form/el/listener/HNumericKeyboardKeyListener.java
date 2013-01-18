package com.ngohung.form.el.listener;

import android.text.InputType;
import android.text.method.DigitsKeyListener;

public class HNumericKeyboardKeyListener extends DigitsKeyListener{

	public HNumericKeyboardKeyListener() {
        super(false, false);
    }

	public HNumericKeyboardKeyListener(boolean sign, boolean decimal) {
        super(sign, decimal);
    }
	
	// force input type to phone
	public int getInputType() {
        return InputType.TYPE_CLASS_PHONE;
    }
	
}
