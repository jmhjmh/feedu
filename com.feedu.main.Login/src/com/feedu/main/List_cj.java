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

public class List_cj extends ListActivity {
	String ss[] = new String[100];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_cj);
		Bundle b = this.getIntent().getExtras();
		ss = b.getStringArray("ss");
		int s_l = b.getInt("i");

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < s_l; i = i + 15) {
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("cj_name", ss[i + 3]);
			map.put("cj_xf", ss[i + 6]);
			// map.put("cj_jd", ss[i + 7]);
			map.put("cj_cj", ss[i + 8]);
			list.add(map);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, list,
				R.layout.list_cj_1,
				new String[] { "cj_name", "cj_xf", "cj_cj" }, new int[] {
						R.id.cj_name, R.id.cj_xf, R.id.cj_cj });
		/*
		 * SimpleAdapter listAdapter = new SimpleAdapter(this, list,
		 * R.layout.list_cj_1, new String[] { "cj_name", "cj_xf", "cj_jd",
		 * "cj_cj" }, new int[] { R.id.cj_name, R.id.cj_xf, R.id.cj_jd,
		 * R.id.cj_cj });
		 */
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("position" + position);
		System.out.println("id" + id);

		String temp[] = new String[15];
		if (position != 0) {
			for (int i = 0; i < 15; i++) {
				temp[i] = ss[i] + ": "
						+ ss[(position) * 15 + i].replaceAll("&nbsp;", "");
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