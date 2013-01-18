package com.ngohung.form.adapter;


import com.ngohung.form.R;
import com.ngohung.form.el.*;
import com.ngohung.form.el.listener.HNumericKeyboardKeyListener;

import android.content.Context;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HFormAdapter extends BaseAdapter implements HFormAdapterInterface{

	private LayoutInflater inflater;
	private HRootElement rootEl;
	
	public final static String REQUIRED_LABEL_FMT = "<font color='red'>*</font> %s" ; // for required field display * in red color
	public final static String STD_LABEL_FMT = "%s" ; // if we need to give spacing to label, we can edit this
	
	public HFormAdapter(Context context, HRootElement rootEl){
		this.rootEl = rootEl;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return rootEl.getElCount();
	}

	@Override
	public Object getItem(int position) {
		return rootEl.getElForIndex(position);
	}

	public int getItemViewType (int position){
		HElement curItem = (HElement) this.getItem(position);
		int itemType = curItem.getElType();
		int index = rootEl.getElementTypes().indexOf(itemType) ;
		
		if(index < 0)
			return  0;
		else return index;
	}
	
	
	public int getViewTypeCount() // return number of different views we have. This is used for reusing the convertview
	{
		return rootEl.getElementTypes().size();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		HElement curItem = (HElement) this.getItem(position);
		
		// inflate convertview from layout
		if (convertView == null) {
			holder = new ViewHolder();
			
			switch(curItem.getElType() ){
				case HElementType.TEXT_EL:
				{
					convertView = inflater.inflate(R.layout.form_text_row_item , parent, false);
				}
				break;
					
				// same layout
				case HElementType.NUMERIC_ENTRY_EL:
				case HElementType.MULTI_LINES_TEXT_ENTRY_EL: 
				case HElementType.TEXT_ENTRY_EL:
				{
					
					convertView = inflater.inflate(R.layout.form_text_entry_row_item , parent, false);
				}
				break;
				
				
				// same handling for date element and picker element
				case HElementType.DATE_ENTRY_EL:
				case HElementType.PICKER_EL:
				case HElementType.LIST_EL:
				{
					convertView = inflater.inflate(R.layout.form_picker_row_item , parent, false);
					
					final View pickerBtnView =convertView.findViewById(R.id.btn_picker);
					if(pickerBtnView!=null){
						holder.pickerBtn = (Button) pickerBtnView;	
					}
				}
				break;
				
					
			}
			
			holder.labelTV = (TextView) convertView.findViewById(R.id.labelTextView);
		
			Object valueView = convertView.findViewById(R.id.valueTextView);
			if(valueView!=null)
				holder.valueTV = (EditText) valueView;
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(holder.labelTV!=null && curItem.getLabel()!=null)
		{
			// if this field is required, then we need to mark this with a *
			if(curItem.isRequired() )
			{
				holder.labelTV.setText ( Html.fromHtml(  String.format(REQUIRED_LABEL_FMT, curItem.getLabel() ) ) );
			}
			else
			{
				holder.labelTV.setText(String.format(STD_LABEL_FMT, curItem.getLabel())  );
			}
		}
		
//		Log.i("HFormAdapter", "element key = " + curItem.getKey() + ", hidden: " + curItem.isHidden() + ", valueTV: " + holder.valueTV + ", eltype: " + curItem.getElType());
		
		// handle keyboard events
		// let our models (HElement) handle the events, update the behind models, and validate automatically
		if(holder.valueTV!=null && curItem.getValue()!=null)
		{
			
			holder.valueTV.setText(curItem.getValue() );
			
			if(curItem.getHint()!=null)
				holder.valueTV.setHint(curItem.getHint() );
			
			if(curItem instanceof HTextEntryElement)
			{
				HTextEntryElement textEntryEl = (HTextEntryElement) curItem;
				holder.valueTV.setOnFocusChangeListener(textEntryEl); // when element lose focus, it will be validated
			}
			
			// for last focusable item, we set keyboard to Done
			// for normal items we set to next
			holder.valueTV.setImeOptions( (position == rootEl.getLastInputKeyboardElPosition() ) ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_NEXT);
			
			
			// constrain max length
			if(curItem.hasSpecifyMaxLength() ){
				holder.valueTV.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(curItem.getMaxLength())   } );
			}
			
			if(curItem.hasSpecifyKeyboardType() ){
				holder.valueTV.setInputType(curItem.getKeyboardType() );
			}
						
		}
		

		// handle keyboard events
		// let our models (HElement) handle the events, update the behind models, and validate automatically
		switch(curItem.getElType()){
			
			case HElementType.MULTI_LINES_TEXT_ENTRY_EL:
			{
				int numOfLines = 3;
				if(curItem instanceof HTextAreaEntryElement)
				{
					HTextAreaEntryElement el = (HTextAreaEntryElement) curItem;
					numOfLines = el.getNumberOfLines();
				}
				
				holder.valueTV.setSingleLine(true);
				holder.valueTV.setMaxLines(numOfLines);
				holder.valueTV.setLines(numOfLines);
				if(!curItem.hasSpecifyKeyboardType())
					holder.valueTV.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS  ) ;
				holder.valueTV.setHorizontallyScrolling(false);
			}
			break;
			
			case HElementType.NUMERIC_ENTRY_EL:
			{
				holder.valueTV.setKeyListener(new HNumericKeyboardKeyListener()); // force numeric input
			}
			break;
			
			case HElementType.DATE_ENTRY_EL:
			{
				HDatePickerElement dateEl = (HDatePickerElement) curItem;
				holder.pickerBtn.setOnClickListener(dateEl);
				
				curItem.loadValueForUI(holder.pickerBtn);
				
			}
			break;
			
			case HElementType.PICKER_EL:
			{
				HPickerElement pickerItem = (HPickerElement) curItem ;
				holder.pickerBtn.setOnClickListener(pickerItem);
				curItem.loadValueForUI(holder.pickerBtn);
		
			}
			break;
			
//			case HElementType.LIST_EL: // list element will generate a single choice listview
//			{
//				HListElement pickerItem = (HListElement) curItem ;
//				holder.pickerBtn.setOnClickListener(pickerItem);
//				curItem.loadValueForUI(holder.pickerBtn);
//			}
//			break;
			
		}
		
		convertView.setVisibility(curItem.isHidden() ? View.GONE : View.VISIBLE);
	
		
		return convertView;
	}
	
	
	// get the main view that is tied to the convertview for each position
	// e.g. for picker type elements, we get the Button
	// for normal element, we get the EditText in the holder
	// we need this view sometimes as we need to display validation in the view e.g. the ! error icon at the right for button/edittext
	public View getViewForValidation(View convertView, int position){
		View mainView = null;
		HElement curItem = (HElement) this.getItem(position);
		
		if(convertView!=null && convertView.getTag()!=null){
			ViewHolder holder = (ViewHolder) convertView.getTag();
			switch(curItem.getElType() ){
				case HElementType.DATE_ENTRY_EL:
				case HElementType.PICKER_EL:
				case HElementType.LIST_EL:
					return holder.pickerBtn;
				
				case HElementType.TEXT_EL: // no need to do anything for text element
					return null;
			
				case HElementType.NUMERIC_ENTRY_EL:
				case HElementType.TEXT_ENTRY_EL:
				case HElementType.MULTI_LINES_TEXT_ENTRY_EL:
					return holder.valueTV;
				
				default:
					return holder.valueTV;
					
			}
			
			
		}
		
		return mainView;
		
	}
	
	

	// header methods
	public long getHeaderId(int position) {
		return rootEl.getSectionIndexForPosition(position);
	}
	
	// this should be override by subclass for further customization
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(R.layout.default_form_section_header, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.sectionHeaderText);
			
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		HSection curSection = getSectionForPosition(position);
		
		// hide header if not there
		if(curSection.getTitle() != null && curSection.getTitle().length() > 0)
		{
			holder.text.setText(curSection.getTitle() );
			convertView.setVisibility(View.VISIBLE);
		}
		else{
			convertView.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}
	
	public HSection getSectionForPosition(int position)
	{
		int sectionIndex = rootEl.getSectionIndexForPosition(position);
		HSection curSection = rootEl.getSections().get(sectionIndex);
		return curSection;
	}
	
	// view holder optimization
	class ViewHolder {
		TextView labelTV;
		EditText valueTV;
		
		Button pickerBtn; // only applicable for picker element
		
	}
	
	// hold the header text
	class HeaderViewHolder {
		TextView text;
	}
	
}
