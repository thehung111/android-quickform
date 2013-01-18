package com.ngohung.form;

import java.util.ArrayList;
import java.util.List;

import com.ngohung.form.adapter.HFormAdapter;
import com.ngohung.form.el.HElement;
import com.ngohung.form.el.HRootElement;
import com.ngohung.form.el.validator.ValidationStatus;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

// this form will serve as the base for all form
public abstract class HBaseFormActivity extends Activity {

	protected HRootElement rootElement; 						// the element represent the form to build
	protected HFormAdapter formAdapter;
	
	protected LinearLayout formContainerView;
	protected TextView instructionTextView; 					// our instruction if there is e.g. * denotes mandatory
	protected List<View> formViews = new ArrayList<View>() ;	// list of views that are used for validation e.g. EditText for text entry elements, Button for picker elements
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hbase_form);

		// get back the view
		formContainerView = (LinearLayout) this.findViewById(R.id.formContainer);
		instructionTextView = (TextView) formContainerView.findViewById(R.id.instructionTextView);
		
		rootElement = createRootElement();
		if(rootElement == null)
			return;
		
		// set title for the activity
		if(rootElement.getTitle()!=null)
			this.setTitle(rootElement.getTitle());
		
		formAdapter = new HFormAdapter(this, rootElement );
		
		// load various views into the container with the help of adapter
		buildForm(formAdapter, formContainerView, formViews);
		
		
	}
	
	// should save data whenever quit the form
	protected void onPause()
	{
		super.onPause();
		saveData();
	}

	// to be overrided by subclass
	protected abstract HRootElement createRootElement();
	
	protected String getFormTitle(){
		return rootElement.getTitle();
	}
	
	// when user left the form by closing or clicking on the Back button, we should save all the data
	// data can be save to preference or sqlite if necessary
	public void saveData(){
		
		int numOfItems = formAdapter.getCount();
		
		// must add the headers also
		for(int position = 0; position < numOfItems; position++){
			// validation
			View validationView = this.formViews.get(position);
			HElement el = (HElement) formAdapter.getItem(position);
			el.saveValueFromUI(validationView);
		}
	}
	
	
	
	
	protected void buildForm(HFormAdapter adapter, LinearLayout containerView,List<View> validationViews) {
		
		int numOfItems = adapter.getCount();
		
		long lastHeaderId = -1;
		
		for(int position = 0; position < numOfItems; position++){
			
			// for the first item in a section, we draw the header
			long headerId = adapter.getHeaderId(position);
			if(headerId!= lastHeaderId){
				// draw the header here
				View headerView = adapter.getHeaderView(position, null, containerView);
				containerView.addView(headerView);
				
				lastHeaderId = headerId;
			}
			
			// create the views and add it to container
			View convertView = adapter.getView(position, null, containerView);
			containerView.addView(convertView);
			
			// keep track of the views we want to use for validation
			View validationView = adapter.getViewForValidation(convertView, position);
			validationViews.add(validationView);
			
		}
		
		
	}
	
	protected void setInstructions(String msg){
		if(msg!=null && msg.length() > 0 && instructionTextView!=null ){
			
			// if message contains star, then we mark it with red color
			if(msg.indexOf("*") > -1)
			{
				msg = msg.replace("*", "<font color='red'>*</font>");
				
				instructionTextView.setText( Html.fromHtml(msg) );
				
			}
			else
				instructionTextView.setText(msg);
			
			
			instructionTextView.setVisibility(View.VISIBLE);
		}
	}
	
	// validation
	// use for cases when we come back from listelement selection
	public void refreshAndValidateViews(){
		int numOfItems = formAdapter.getCount();
		
		// must add the headers also
		for(int position = 0; position < numOfItems; position++){
			// validation
			View validationView = this.formViews.get(position);
			HElement el = (HElement) formAdapter.getItem(position);
			el.loadValueForUI(validationView); 		// refresh data
	
			el.doValidationForUI(validationView); 	// validate
		}
	}
	
	// validate a particular view based on its key
	public void refreshAndValidateView(String key){
		if(key == null)
			return;
		
		int numOfItems = formAdapter.getCount();
		
		// must add the headers also
		for(int position = 0; position < numOfItems; position++){
			// validation
			View validationView = this.formViews.get(position);
			HElement el = (HElement) formAdapter.getItem(position);
			if(el.getKey()!=null && el.getKey().equals(key))
			{
				el.loadValueForUI(validationView); 		// refresh data
				el.doValidationForUI(validationView); 	// validate
				return;
			}
			
		}
	}
	
	// this is for checking the value when user click on Done button for example
	// validate all form elements
	// return true if has no error
	// return false if there is at least 1 error
	public boolean checkFormData(){
		
		// save all data first
		saveData();
		
		// must do validation here
		// let subclass handle this
		// default just call the views in adapter
		int numOfItems = formAdapter.getCount();
		
		boolean hasErrors = false;
		
		// must add the headers also
		for(int position = 0; position < numOfItems; position++){
			// validation
			View validationView = this.formViews.get(position);
			HElement el = (HElement) formAdapter.getItem(position);
			ValidationStatus valStatus = el.doValidationForUI(validationView);
			if(valStatus!=null)
				hasErrors = hasErrors | valStatus.isInvalid() ;
		}
		
		return (!hasErrors);
		
	}
	
	// for list element, after list selection, validation should be triggered immediately
	protected void onActivityResult (int requestCode, int resultCode, Intent data)
	{
//		if(requestCode == FormListElement.FORM_INTENT_REQUEST_CODE){
//
//			FormListElement listEl = myApp.getLastReadListElement();
//			
//			// update the label
//			refreshAndValidateView(listEl.getKey() );
//			
//		}
	}
	
	
	
	
	// display error message
	public void displayFormErrorMsg(String title, String msg){
		AlertDialog.Builder errAlert = new AlertDialog.Builder(this);

		errAlert.setTitle(title);
		errAlert.setMessage(msg);
		
		errAlert.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog,
							int whichButton) {
						
					}
				});

		errAlert.show();
	}


}
