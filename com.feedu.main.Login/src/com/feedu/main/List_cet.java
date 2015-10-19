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

public class List_cet extends ListActivity {
	String ss[] = new String[100];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_cet);
		Bundle b = this.getIntent().getExtras();
		ss = b.getStringArray("ss");
		int s_l = b.getInt("i");

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < s_l; i = i + 10) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("cet_date", ss[i + 4]);
			map.put("cet_name", ss[i + 2]);
			map.put("cet_cent", ss[i + 5]);
			list.add(map);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, list,
				R.layout.list_cet_1, new String[] { "cet_date", "cet_name",
						"cet_cent" }, new int[] { R.id.cet_date, R.id.cet_name,
						R.id.cet_cent });
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("position" + position);
		System.out.println("id" + id);

		String temp[] = new String[10];
		if (position != 0) {
			for (int i = 0; i < 10; i++) {
				temp[i] = ss[i] + ": "
						+ ss[(position) * 10 + i].replaceAll("&nbsp;", "");
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("详细");
			builder.setItems(temp, null);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}

					});
			builder.create().show();
		}
	}
}