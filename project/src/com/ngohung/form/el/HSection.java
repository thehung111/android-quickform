package com.ngohung.form.el;

import java.util.ArrayList;
import java.util.List;

/* 
 * This class represent a form section. Form elements can be grouped into sections which have their own headers
 */
public class HSection {

	private List<HElement> elements;
	private String title; // title for this section 
	
	public HSection(String title) {
		this.title = title;
		elements = new ArrayList<HElement>();
	}
	
	
	public void addEl(HElement el){
		if(el == null)
			return;
		
		if(!elements.contains(el))
			elements.add(el);
	}
	
	public void removeEl(HElement el){
		
		if(el == null)
			return;
		
		if(elements.contains(el))
			elements.remove(el);
	}
	
	public List<HElement> getElements() {
		return elements;
	}
	public void setElements(List<HElement> elements) {
		this.elements = elements;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
