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

public class List_ks extends ListActivity {
	String ss[] = new String[100];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_ks);
		Bundle b = this.getIntent().getExtras();
		ss = b.getStringArray("ss");
		int s_l = b.getInt("i");

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < s_l; i = i + 8) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ks_date", ss[i + 1]);
			map.put("ks_name", ss[i + 3]);
			map.put("ks_address", ss[i + 4]);
			map.put("ks_address2", ss[i + 6]);
			list.add(map);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, list,
				R.layout.list_ks_1, new String[] { "ks_date", "ks_name",
						"ks_address", "ks_address2" }, new int[] {
						R.id.ks_date, R.id.ks_name, R.id.ks_address,
						R.id.ks_address2 });
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("position" + position);
		System.out.println("id" + id);

		String temp[] = new String[8];
		if (position != 0) {
			for (int i = 0; i < 8; i++) {
				temp[i] = ss[i] + ": "
						+ ss[(position) * 8 + i].replaceAll("&nbsp;", "");
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