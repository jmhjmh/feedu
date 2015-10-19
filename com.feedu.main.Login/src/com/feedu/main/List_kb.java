package com.feedu.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class List_kb extends ListActivity {
	String ss[] = new String[100];
	String[] temp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_kb);
		Bundle b = this.getIntent().getExtras();
		String[] ss = b.getStringArray("ss");
		int s_l = b.getInt("i");

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < s_l; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			temp = null;
			temp = new String[200];
			temp = ss[i].split("<br>");
			for (int j = 0; j < temp.length; j++) {
				map.put("kb_name", temp[0]);
				map.put("kb_sj", temp[1]);
				map.put("kb_ls", temp[2]);
				map.put("kb_js", temp[3]);
			}
			list.add(map);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, list,
				R.layout.list_kb_1, new String[] { "kb_name", "kb_sj", "kb_ls",
						"kb_js" }, new int[] { R.id.kb_name, R.id.kb_sj,
						R.id.kb_ls, R.id.kb_js });
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("position" + position);
		System.out.println("id" + id);
	}
}