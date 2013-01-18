package com.ngohung.form.el;

import com.ngohung.form.constant.HConstants;
import com.ngohung.form.el.store.HDataStore;
import com.ngohung.form.el.validator.ValidationStatus;
import com.ngohung.form.util.HStringUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * This element will display a single choice dialog popup
 * The displaying options refer to the choices in the popup
 * The 'options' should contain unique String array
 * The 'value' is refering to the selection after user has clicked on an item in the popup 
 *  
 */
public class HPickerElement extends HElement implements OnClickListener{

	protected int elType = HElementType.PICKER_EL;
	
	// the separator is so that we can pass the options String in the constructor
	// Example: Option 1|Option 2|Option 3 will generate the options array with 3 entry
	public final static String SEPARATOR_REGEX = "\\|" ;
	public final static String SEPARATOR = "|" ;
	public final static String DEFAULT_REQUIRED_MSG = "Please select" ;
	
	
	private String[] options;  									// list of option for display
	private int selectedIndex = HConstants.NOT_SPECIFIED; 		// which option user has selected
	
	// constructors
	// optionsStr is of format: Option 1|Option 2|Option 3
	public HPickerElement(String key, String label, String hint, boolean required, String optionsStr)
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		
		this.options = optionsStr.split(SEPARATOR_REGEX);
		selectedIndex = HConstants.NOT_SPECIFIED;
		
	}
	
	// constructors
	// optionsStr is of format: Option 1|Option 2|Option 3
	public HPickerElement(String key, String label, String hint, boolean required, String optionsStr, HDataStore store)
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		
		this.options = optionsStr.split(SEPARATOR_REGEX);
		selectedIndex = HConstants.NOT_SPECIFIED;
		
		this.setDataStore(store);
		if(store!=null){
			store.loadValueFromStore(this);
			selectedIndex = HStringUtil.getSelectedIndex(options, value);
		}
		
	}
	
	public HPickerElement(String key, String label, String hint, boolean required, String optionsStr, int selectedIndex)
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		
		this.options = optionsStr.split(SEPARATOR_REGEX);
		this.selectedIndex = selectedIndex;
		
	}
	
	public HPickerElement(String key, String label, String hint, boolean required, String optionsStr, int selectedIndex, HDataStore store)
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		
		this.options = optionsStr.split(SEPARATOR_REGEX);
		this.selectedIndex = selectedIndex;
		
		this.setDataStore(store);
		if(store!=null){
			store.loadValueFromStore(this);
			selectedIndex = HStringUtil.getSelectedIndex(options, value);
		}
		
	}
	
	public HPickerElement(String key, String label, String hint, boolean required, String options[])
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		
		this.options = options;
		selectedIndex = HConstants.NOT_SPECIFIED;
		
	}
	
	public HPickerElement(String key, String label, String hint, boolean required, String options[], int selectedIndex)
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		
		this.options = options;
		this.selectedIndex = selectedIndex;
		
	}
	
	public HPickerElement(String key, String label, String hint, boolean required, String options[], int selectedIndex, HDataStore store)
	{
		this.key = key;
		this.label = label;
		this.value = HConstants.BLANK;
		this.hint = hint;
		this.required = required;
		this.requireMsg = DEFAULT_REQUIRED_MSG ;
		
		this.options = options;
		this.selectedIndex = selectedIndex;
		
		this.setDataStore(store);
		if(store!=null){
			store.loadValueFromStore(this);
			selectedIndex = HStringUtil.getSelectedIndex(options, value);
		}
		
	}
	
	
	// the view here is refering to the button that this element represent
	@Override
	public void loadValueForUI(View v) {
		if(!(v instanceof Button) )
			return;
		Button btn = (Button) v;
		
		if(value == null || value.length() == 0 || (selectedIndex == HConstants.NOT_SPECIFIED) )
		{
			btn.setText(this.getHint());
			return;
		}
		
		btn.setText(value);
		
		
	}

	@Override
	public void saveValueFromUI(View v) {
		// nothing to do here as the view is a button which only display the value
		
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
	
	
	// trigger the dialog upon clicking of the picker btn
	@Override
	public void onClick(View view) {
		if(view instanceof Button)
		{
			final Button pickerBtn = (Button) view;
			
			String title = this.getHint();
			
			AlertDialog.Builder builder= new AlertDialog.Builder(pickerBtn.getContext());
	        builder.setTitle(title);
	        builder.setCancelable(true);
	        
	        builder.setSingleChoiceItems(options, selectedIndex , new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	// update button text
	            	pickerBtn.setText( options[which] ); 
	            	setValue(options[which]); // store value
	            	selectedIndex = which;
	            	
	            	//new category selected
	                dialog.dismiss();
	                
	                // display error if fail validation
	                doValidationForUI(pickerBtn);
	            }
	        });
	        
	        
	        AlertDialog alert= builder.create();
	        alert.show();
		}
		
		
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	
	public int getElType() {
		return HElementType.PICKER_EL;
	}
}
