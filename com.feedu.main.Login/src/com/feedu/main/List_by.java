package com.feedu.main;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class List_by extends ListActivity {
	String ss[] = new String[100];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_by);

		Bundle b = this.getIntent().getExtras();
		String[] ss = b.getStringArray("ss");
		int s_l = b.getInt("i");

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < 22; i = i + 2) {
			HashMap<String, String> map = new HashMap<String, String>();

			if (i != 6 && i != 7) {
				map.put("by_title", ss[i].replaceAll(" ", ""));
				map.put("by_name", ss[i + 1].replaceAll(" ", ""));
				list.add(map);
			}

		}
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("by_title", "任务书内容：");
		map2.put("by_name", ss[22].replaceAll(" ", ""));
		list.add(map2);
		for (int i = 23; i < s_l - 1; i = i + 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			if (i != 29 && i != 30) {
				map.put("by_title", ss[i].replaceAll(" ", ""));
				map.put("by_name", ss[i + 1].replaceAll(" ", ""));
				list.add(map);

			}
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.list_by_1,
				new String[] { "by_title", "by_name" }, new int[] { R.id.by_title, R.id.by_name });
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("position" + position);
		System.out.println("id" + id);

		@SuppressWarnings("unchecked")
		HashMap<String, String> item = (HashMap<String, String>) l.getItemAtPosition(position);
		String title = item.get("by_title");
		String name = item.get("by_name");
		AlertDialog.Builder dialog = new AlertDialog.Builder(List_by.this);
		dialog.setTitle("详细").setIcon(android.R.drawable.ic_dialog_info).setMessage(title + "\n" + name)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}

				}).create().show();
	}
}