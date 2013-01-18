package com.example.androidquickformdemo;

import java.util.ArrayList;


import com.ngohung.form.HBaseFormActivity;
import com.ngohung.form.el.*;
import com.ngohung.form.el.validator.HEmailValidatorListener;
import com.ngohung.form.el.validator.HNumericValidatorListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DemoActivity extends HBaseFormActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("HBaseFormActivity", "Demo started!!" );
		
		this.setInstructions("* fields are mandatory");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_demo, menu);
		return true;
	}
	
	 public boolean onOptionsItemSelected(MenuItem item) {
		 
		 switch (item.getItemId()) {
		 	case R.id.validateAllFields:
		 	{
		 		if(!this.checkFormData())
		 			this.displayFormErrorMsg("Error", "There are errors in the form");
		 		else
		 			this.displayFormErrorMsg("Success", "There are no errors in the form");
		 	}		
		 	break;
		 }
		 
		 return super.onOptionsItemSelected(item);
	 }

	
	
	@Override
	protected HRootElement createRootElement() {
		
		// Generate test form with all the fields
		ArrayList<HSection> sections = new ArrayList<HSection>();
		
		HSection personalInfoSection = new HSection("Personal Information");
		
		personalInfoSection.addEl(new HTextEntryElement("name_key", "Name", "Enter your name", true) );
		personalInfoSection.addEl(new HPickerElement("gender_key", "Gender", "Select gender", true, "Male|Female") );
		personalInfoSection.addEl(new HDatePickerElement("dob_key", "Date of Birth", "Select date", true) );
		
		
		HSection contactInfoSection = new HSection("Contact Information");
		
		HNumericElement hpEl = new HNumericElement("hp_key", "Mobile", "Enter number", true);
		hpEl.setRequireMsg("Please enter a number");
		hpEl.setMaxLength(8);
		
		contactInfoSection.addEl(hpEl);
		
		// test optional field
		HNumericElement homeEl = new HNumericElement("homeno_key", "Home No.", "Enter number", false);
		contactInfoSection.addEl(homeEl);
		
		HTextEntryElement emailEl = new HTextEntryElement("email_key", "Email", "Enter your email", true); 
		emailEl.addValidator(new HEmailValidatorListener());
		contactInfoSection.addEl(emailEl);
		
		
		HSection mailingAddrSection = new HSection("Mailing Address");
		
		HTextAreaEntryElement addrEl = new HTextAreaEntryElement("addr_key", "Address", "Enter your address", true);
		mailingAddrSection.addEl(addrEl);
		
		HNumericElement postalCodeEl = new HNumericElement("post_office_key", "Postal Code", "Enter 6 digits number", true);
		postalCodeEl.setMaxLength(6);
		postalCodeEl.addValidator(new HNumericValidatorListener(6, "Please enter 6 digits")); // must be exactly 6 digits
		mailingAddrSection.addEl(postalCodeEl);
		
		
		sections.add(personalInfoSection);
		sections.add(contactInfoSection);
		sections.add(mailingAddrSection);
		
		
		HRootElement rootEl = new HRootElement("Simple Form" , sections);
		
		return rootEl;
	}

}
