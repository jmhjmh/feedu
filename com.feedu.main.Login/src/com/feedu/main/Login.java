package com.feedu.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private EditText user;
	private EditText password;
	private Button loginBtn;
	private ProgressDialog m_Dialog = null;
	private CheckBox cbrp;
	private SharedPreferences sp;

	private String username = "";
	private String pass = "";
	private String xh = "";
	private String xm = "";
	private String myUrl = "jw.fe-edu.com.cn/(ydcvtyqhieuonsfh5pdyby55)";// 外网
	// private String myUrl = "192.168.12.124";// 内网

	private int result = 0;

	private static Boolean isExit = false;
	private static Boolean hasTask = false;

	private Handler handler = new Handler();
	Timer tExit = new Timer();
	DefaultHttpClient mHttpClient = new DefaultHttpClient();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		cbrp = (CheckBox) findViewById(R.id.cbrp);

		loginBtn.setOnClickListener(loginClick);

		// 请求超时
		mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// 读取超时
		mHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

		InitConfig();
		MyApp myApp = (MyApp) getApplication();
		myApp.setMyUrl(myUrl);
		cbrp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sp = getSharedPreferences("UserInfo", 0);
				sp.edit().putBoolean("cbrp", isChecked).commit();
				if (isChecked) {
					cbrp.setTextColor(0xffffffff);
				} else {
					cbrp.setTextColor(0xffB9D3EE);
				}
			}
		});
		if (cbrp.isChecked()) {
			cbrp.setTextColor(0xffffffff);
		}

	}

	// 初始化配置
	private void InitConfig() {
		sp = getSharedPreferences("UserInfo", 0);
		user.setText(sp.getString("account", null));
		password.setText(sp.getString("password", null));
		cbrp.setChecked(sp.getBoolean("cbrp", false));
	}

	OnClickListener loginClick = new OnClickListener() {

		public void onClick(final View v) {
			if (user.getText().toString().equals("") || password.getText().toString().equals("")) {
				Toast.makeText(Login.this, "请输入学号或者密码", Toast.LENGTH_SHORT).show();
				return;
			}
			m_Dialog = ProgressDialog.show(Login.this, "请稍后...", "验证登录中...", true);

			new Thread(new Runnable() {

				public void run() {
					MyApp myApp = (MyApp) getApplication();
					if (myApp.getMyUrl() != null && myApp.getMyUrl() != "") {
						myUrl = myApp.getMyUrl();
					}
					result = checkUser();// 获取登陆情况

					// 更新界面
					handler.post(new Runnable() {
						public void run() {
							if (result == 1) {
								if (cbrp.isChecked()) {
									sp = getSharedPreferences("UserInfo",
											Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);

									sp.edit().putString("account", user.getText().toString()).commit();
									sp.edit().putString("password", password.getText().toString()).commit();
								}
								Toast.makeText(v.getContext(), "用户登录成功！ 欢迎" + xm, Toast.LENGTH_SHORT).show();
								Context context = v.getContext();
								Intent intent = new Intent(context, LoginSuccess.class);
								Bundle map = new Bundle();
								map.putSerializable("xh", xh);
								map.putSerializable("xm", xm);
								intent.putExtra("session", map);
								context.startActivity(intent);
								Login.this.finish();
							}
							if (result == 0) {
								Toast.makeText(v.getContext(), "用户验证失败！", Toast.LENGTH_SHORT).show();
							}
							if (result == -1) {
								Toast.makeText(v.getContext(), "请检查网络连接！", Toast.LENGTH_SHORT).show();
							}
							if (result == -2) {
								Toast.makeText(v.getContext(), "错误原因：系统正忙！", Toast.LENGTH_SHORT).show();
							}
							if (result == -3) {
								Toast.makeText(v.getContext(), "密码错误！", Toast.LENGTH_SHORT).show();
							}
							if (result == -4) {
								Toast.makeText(v.getContext(), "密码不合法！", Toast.LENGTH_SHORT).show();
							}
							if (result == -5) {
								Toast.makeText(v.getContext(), "您的密码安全性较低，请先修改密码后再登录系统！", Toast.LENGTH_SHORT).show();
							}
						}
					});

					m_Dialog.dismiss();
				}
			}).start();

		}

	};

	private int checkUser() {
		// 此处先获取页面，读取到value值为post做准备
		String __VIEWSTATE = "";
		String temp = "";
		StringTokenizer tokenizer = null;

		try {
			temp = HttpUtil.getUrl("http://" + myUrl + "/default2.aspx", mHttpClient, "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (temp == "") {
			return -1;
		}
		tokenizer = new StringTokenizer(temp);
		while (tokenizer.hasMoreTokens()) {
			String valueToken = tokenizer.nextToken();
			if (StringUtil.isValue(valueToken, "value")) {
				if (StringUtil.getValue(valueToken, "value", "\"", 7).length() == 48) {
					__VIEWSTATE = StringUtil.getValue(valueToken, "value", "\"", 7);// value
				}
			}
		}

		username = user.getText().toString();
		pass = password.getText().toString();

		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
		pairs.add(new BasicNameValuePair("TextBox1", username));
		pairs.add(new BasicNameValuePair("TextBox2", pass));
		pairs.add(new BasicNameValuePair("RadioButtonList1", "%D1%A7%C9%FA"));
		pairs.add(new BasicNameValuePair("Button1", null));
		pairs.add(new BasicNameValuePair("lbLanguage", null));

		String info = "";
		try {
			info = HttpUtil.postUrl("http://" + myUrl + "/default2.aspx", pairs, mHttpClient, "");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (info != "") {
			MyApp myApp = (MyApp) getApplication();
			myApp.setmHttpClient(mHttpClient);// 将对象放入app中
			tokenizer = new StringTokenizer(info);

			while (tokenizer.hasMoreTokens()) {
				String valueToken = tokenizer.nextToken();
				if (StringUtil.isValue(valueToken, "defer")) {
					return 0;
				}
				if (StringUtil.isValue(valueToken, "<title>ERROR")) {
					return -2;
				}
				if (StringUtil.isValue(valueToken, "alert('密码错误！！');")) {
					return -3;
				}
				if (StringUtil.isValue(valueToken, "alert('密码不合法！');")) {
					return -4;
				}
				if (StringUtil.isValue(valueToken, "alert('您的密码安全性较低，请先修改密码后再登录系统！');")) {
					return -5;
				}
				if (StringUtil.isValue(valueToken, "id=\"xhxm")) {
					xh = StringUtil.getValue(valueToken, "id=\"xhxm", "<", 10);// value
					xm = StringUtil.getValue(tokenizer.nextToken().toString(), "", "同", 0);// 从登陆后页面取得姓名
				}
			}
			return 1;
		} else {
			return -1;
		}

	}

	TimerTask task = new TimerTask() {
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// DialogAPI.showExit(this);
			if (isExit == false) {
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				finish();
				System.exit(0);
			}
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "帮助").setIcon(android.R.drawable.ic_menu_help);

		/*
		 * menu.add(Menu.NONE, Menu.FIRST + 1, 1, "设置").setIcon(
		 * android.R.drawable.ic_menu_manage);
		 */
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		/*
		 * case Menu.FIRST + 1: Toast.makeText(this, "默认为外网环境",
		 * Toast.LENGTH_LONG).show(); Intent intent = new Intent(Login.this,
		 * SetNet.class); Login.this.startActivity(intent); break;
		 */

		case Menu.FIRST + 2:
			Toast.makeText(this, "By jmhjmh1", Toast.LENGTH_LONG).show();
			break;
		}
		return false;
	}
}