package com.ngohung.form.el;

import java.util.ArrayList;
import java.util.List;

import com.ngohung.form.el.validator.ValidationStatus;

// this class represent the form root element with form sections. each section has their own elements
public class HRootElement {


	private List<HSection> sections;
	private String title; 	// title for this form. should be used as the title
							// for the activity

	// optimization for the adapter
	private int elCount; 			// total number of elements
	private HElement mElIndex[]; 	// index for different elements in each section
	private int mSectionIndex[]; 	// indexes for the sections
	
	private int lastInputKeyboardElPosition = -1; 	// index of the last focusable item which require keyboard input,
													// the ime option for the last element must be Done 
													// the ime option for other elements should be next

	protected ArrayList<Integer> elementTypes = new ArrayList<Integer>(); // array of possible element types, used for adapter to reuse the converterview if necessary
																			

	public HRootElement( String title, List<HSection> sections) {

		this.title = title;
		this.sections = sections;
		
		int count = 0;
		
		for (HSection section : sections) {
			count = count + section.getElements().size();
		}
		elCount = count;

		// we need to store the display index for individual element also
		mElIndex = new HElement[elCount];
		mSectionIndex = new int[elCount];

		// indexing all the elements for faster retrieval
		int i = 0;
		int sectionIndex = 0;
		for (HSection section : sections) {
			List<HElement> els = section.getElements();
			for (HElement el : els) {
				mElIndex[i] = el;
				mSectionIndex[i] = sectionIndex;

				int elType = el.getElType();
				
				if (!elementTypes.contains(elType)) {
					elementTypes.add(elType);
				}

				if (el.isKeyboardInputRequired()) {
					lastInputKeyboardElPosition = i;
				}

				i++;
			}

			sectionIndex++;
		}

	}

	public int getSectionIndexForPosition(int position) { // return the section index for the position of the element
		if (position < 0 || position >= elCount)
			return -1;

		return mSectionIndex[position];

	}

	public HElement getElForIndex(int positionIndex) {
		if (positionIndex < 0 || positionIndex >= elCount)
			return null;

		return mElIndex[positionIndex];
	}

	// do validation for all elements in this root element
	// return true if there is no error
	// return false if there is at least 1 error
	public boolean doValidation() {
		boolean hasErrors = false;

		for (int position = 0; position < elCount; position++) {
			HElement el = this.getElForIndex(position);
			ValidationStatus status = el.doValidation();
			if (status != null && (!status.isValid())) {
				hasErrors = true;
				break;
			}

		}

		return (!hasErrors);

	}

	public int getElCount() {
		return elCount;
	}

	public List<HSection> getSections() {
		return sections;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<Integer> getElementTypes() {
		return elementTypes;
	}

	public void setElementTypes(ArrayList<Integer> elementTypes) {
		this.elementTypes = elementTypes;
	}

	public int getLastInputKeyboardElPosition() {
		return lastInputKeyboardElPosition;
	}

	public void setLastInputKeyboardElPosition(int lastInputKeyboardElPosition) {
		this.lastInputKeyboardElPosition = lastInputKeyboardElPosition;
	}

}
