package com.ngohung.form.el;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.ngohung.form.constant.HConstants;
import com.ngohung.form.el.store.HDataStore;
import com.ngohung.form.el.validator.ValidationStatus;

public class HDatePickerElement extends HElement implements OnClickListener{
	
	protected int elType = HElementType.DATE_ENTRY_EL;
	private Date dateValue; 					// the date value for this element
	private SimpleDateFormat displayDateFmt; 	// date format to display in the picker button
	
	public final static String DEFAULT_REQUIRED_MSG = "Please select date" ;
	public final static SimpleDateFormat DEFAULT_DISPLAY_DATE_FMT = new SimpleDateFormat("d MMM, yyyy");
	public final static SimpleDateFormat STORE_VALUE_DATE_FMT = new SimpleDateFormat("yyyyMMdd"); // storing the datevalue into the value as this string format
	

	public HDatePickerElement(String key, String label, String hint, boolean required)
	{
		
		this.key = key;
		this.label = label;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		setDateValue(null);  // default to current date
		displayDateFmt = DEFAULT_DISPLAY_DATE_FMT;
		
	}
	
	public HDatePickerElement(String key, String label, String hint, boolean required, HDataStore store)
	{
		
		this.key = key;
		this.label = label;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		displayDateFmt = DEFAULT_DISPLAY_DATE_FMT;
		
		this.setDataStore(store);
		if(store!=null)
		{
			store.loadValueFromStore(this);
			Date storeDateVal = getDateFromValue();
			setDateValue(storeDateVal);
		}
		else
			setDateValue(null);
		
	}
	
	public HDatePickerElement(String key, String label, String hint, boolean required, Date initVal)
	{
		
		this.key = key;
		this.label = label;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		setDateValue(initVal);  // default to current date
		displayDateFmt = DEFAULT_DISPLAY_DATE_FMT;
	}
	
	public HDatePickerElement(String key, String label, String hint, boolean required, Date initVal, SimpleDateFormat displayDateFormat)
	{
		
		this.key = key;
		this.label = label;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		setDateValue(initVal);  // default to current date
		displayDateFmt = displayDateFormat;
		
	}

	
	@Override
	public void loadValueForUI(View v) {
		if(dateValue == null || displayDateFmt == null)
		{
			// set hint here 
			if(v instanceof Button){
				Button btn = (Button) v;
				btn.setText(this.getHint());
			}
			
			return;
		}
		
		if(v instanceof Button){
			Button btn = (Button) v;
			
			btn.setText(displayDateFmt.format(dateValue)); // display selected date
		}
		
		
	}

	@Override
	public void saveValueFromUI(View v) {
		// nothing to do here as the view is the picker button, only used for display
		// which is a button and cannot accept input
		
	}

	@Override
	public ValidationStatus doValidationForUI(View v) {
		if( !(v instanceof Button) )
			return null;
		
		Button btn = (Button) v;
		// do validation here
		ValidationStatus vStatus = doValidation();
		
		// display error validation
		if(vStatus!=null && (!vStatus.isValid())  ){
			// display error here
			btn.setError(vStatus.getMsg() );
			
			// set text color to red
			btn.setText(vStatus.getMsg() );
			btn.setTextColor(Color.RED);
			
		}
		else{
			if(required || (vStatus!=null && vStatus.isValid() ) ) // only do this for required field , if optional field, only when condition is satisfied
			{
				btn.setError(null);
				btn.setTextColor(Color.BLACK);	
			}
		}
		
		return vStatus;
	}
	
	
	@Override
	public void onClick(View btn) {
		// show the dialog here
		if(btn instanceof Button)
		{
			final Button pickerBtn = (Button) btn;
			Calendar cal = Calendar.getInstance(); // default to current date
			
			Date curDateValue = this.getDateValue();
			
			if(curDateValue != null){
				cal.setTime(curDateValue);
			}
			
			int year 	= cal.get(Calendar.YEAR);
            int month 	= cal.get(Calendar.MONTH);
            int day 	= cal.get(Calendar.DAY_OF_MONTH) ;

			DatePickerDialog dateDialog = new DatePickerDialog(pickerBtn.getContext(), new OnDateSetListener(){

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					
					setDateValue(year, monthOfYear, dayOfMonth);
					
					loadValueForUI(pickerBtn);
					
					doValidationForUI(pickerBtn);
					
				}
				
			},  year, month, day  );
			
			dateDialog.show();
		}
		
	}


	public Date getDateValue() {
		return dateValue;
	}

	public Date getDateFromValue() { // manually parse the string
		
		Date resultDate = null;
		
		String val = getValue();
		if(val.length() > 0)
		{
			try {
				resultDate = STORE_VALUE_DATE_FMT.parse(val);
			} catch (ParseException e) {
			}
		}
		
		return resultDate;
		
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		
		if(dateValue == null){
			this.setValue(HConstants.BLANK);
		}
		else{
			this.setValue(STORE_VALUE_DATE_FMT.format(dateValue)); // store value in this format
		}
	}
	
	// helper method to get the date from the datepicker dialog
	public void setDateValue(int year, int monthOfYear, int dayOfMonth)
	{
		Calendar cal = Calendar.getInstance(); 
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		
		Date curDateVal = cal.getTime();
		
		// store this
		this.setDateValue(curDateVal);
	}


	public SimpleDateFormat getDisplayDateFmt() {
		return displayDateFmt;
	}


	public void setDisplayDateFmt(SimpleDateFormat displayDateFmt) {
		this.displayDateFmt = displayDateFmt;
	}

	
	public int getElType() {
		return HElementType.DATE_ENTRY_EL;
	}
}
