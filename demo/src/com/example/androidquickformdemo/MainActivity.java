package com.example.androidquickformdemo;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] values = new String[] { "Simple Form (Without Data Store)",
				"Simple Form (With SharedPreferences autosaving)" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(position == 0){
			Intent i = new Intent(this, DemoActivity.class);
			this.startActivity(i);
		}
		else if(position == 1){
			Intent i = new Intent(this, SharedPrefActivity.class);
			this.startActivity(i);
		}

	}

}
