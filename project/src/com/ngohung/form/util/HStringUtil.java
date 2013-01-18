package com.ngohung.form.util;

public class HStringUtil {

	/*
	 * Give an option array, find the index for value
	 * return -1 if not found
	 * e.g. options: { "Option1","Option2", "Option3" }
	 *      value : "Option2" will return index 1
	 * 
	 */
	public static int getSelectedIndex(String options[], String value){
		int selectedIndex = -1; 
		
		if(value == null || value.length() == 0)
			return -1;
		
		for(int i = 0 ; i < options.length ;i++){
			if(options[i].equals(value))
			{
				selectedIndex = i;
				break;
			}
		}
		
		return selectedIndex;
	}
	
	public static boolean isNumeric(String str)
	{
		if(str == null)
			return false;
		
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	public static boolean isEmail(String str)
	{
		if(str == null)
			return false;
		
		 return android.util.Patterns.EMAIL_ADDRESS.matcher(str.trim() ).matches();
	}
	
	
}
