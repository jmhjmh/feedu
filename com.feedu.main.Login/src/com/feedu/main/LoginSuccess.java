package com.feedu.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginSuccess extends Activity {

	private TextView userid_show;
	private TextView username_show;
	private TextView sessionid_show;
	private Button getInfo;
	private Button getKs;
	private Button getCj;
	private Button getKb;
	private Button getBy;
	private Button getTx;

	private String xm = "";
	private String xh = "";
	private String myUrl = "";
	private String[] mTime = null;
	private String[] ss = null;
	private String ksInfo = null;
	private String __VIEWSTATE_xskscx = "";
	private String __VIEWSTATE_xscj_gc = "";

	private int i = 0;

	private ProgressDialog m_Dialog = null;
	private Handler handler = new Handler();

	private static Boolean isExit = false;
	private static Boolean hasTask = false;

	DefaultHttpClient mHttpClient = new DefaultHttpClient();
	Timer tExit = new Timer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_success);
		Intent get = getIntent();
		xm = get.getBundleExtra("session").getSerializable("xm").toString();
		xh = get.getBundleExtra("session").getSerializable("xh").toString();

		MyApp myApp = (MyApp) getApplication();
		mHttpClient = myApp.getmHttpClient();
		myUrl = myApp.getMyUrl();

		userid_show = (TextView) findViewById(R.id.userid_show);
		userid_show.setText(xh);
		username_show = (TextView) findViewById(R.id.username_show);
		username_show.setText(xm);
		sessionid_show = (TextView) findViewById(R.id.sessionid_show);
		sessionid_show.setText("");

		try {
			xm = java.net.URLEncoder.encode(xm, "gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		getInfo = (Button) findViewById(R.id.getinfo);
		getKs = (Button) findViewById(R.id.getks);
		getCj = (Button) findViewById(R.id.getcj);
		getKb = (Button) findViewById(R.id.getkb);
		getBy = (Button) findViewById(R.id.getby);
		getTx = (Button) findViewById(R.id.gettx);

		getInfo.setOnClickListener(getInfoClick);
		getKs.setOnClickListener(getKsClick);
		getCj.setOnClickListener(getCjClick);
		getKb.setOnClickListener(getKbClick);
		getBy.setOnClickListener(getByClick);
		getTx.setOnClickListener(getTxClick);

	}

	// 等级考试查询
	OnClickListener getInfoClick = new OnClickListener() {
		String cetInfo = "";

		public void onClick(final View v) {
			m_Dialog = ProgressDialog.show(LoginSuccess.this, "请稍后...", "获取数据中...", true);

			new Thread(new Runnable() {

				public void run() {

					try {
						cetInfo = HttpUtil.getUrl("http://" + myUrl + "/xsdjkscx.aspx?xh=" + xh + "&xm="
								+ xm.getBytes("gb2132") + "&gnmkdm=N121606", mHttpClient,
								"http://" + myUrl + "/xs_main.aspx?xh=" + xh);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String temp = cetInfo.replaceAll("</td>", "</td>\n");// 转化换行
					Pattern p = Pattern.compile("(?<=<td>).*(?=</td>)");
					Matcher m = p.matcher(temp);
					ss = null;
					ss = new String[100];
					i = 0;
					while (m.find()) {
						ss[i] = m.group().toString();
						i++;
					}

					// 更新界面
					handler.post(new Runnable() {
						public void run() {
							Context context = v.getContext();
							Intent intent = new Intent(context, List_cet.class);
							Bundle b = new Bundle();
							b.putStringArray("ss", ss);
							b.putInt("i", i);
							intent.putExtras(b);
							if (ss[0] == null) {
								Toast.makeText(LoginSuccess.this, "没有获取到数据\n请检查网络或服务器是否存在数据！", Toast.LENGTH_SHORT)
										.show();
								return;
							} else {
								context.startActivity(intent);
								Toast.makeText(getApplicationContext(), "点击项目可查看详细情况", Toast.LENGTH_SHORT).show();
							}
						}
					});

					m_Dialog.dismiss();
				}
			}).start();

		}

	};

	// 考试查询
	OnClickListener getKsClick = new OnClickListener() {
		String __VIEWSTATE = "";
		StringTokenizer tokenizer = null;
		String xqd = "";
		String xnd = "";
		String[] ss = null;
		int i = 0;

		public void onClick(final View v) {

			ss = new String[100];
			mTime = new String[] { "2012-2013 第一学期", "2012-2013 第二学期", "2013-2014 第一学期", "2013-2014 第二学期",
					"2014-2015 第一学期", "2014-2015 第二学期", "2015-2016 第一学期", "2015-2016 第二学期" };
			new AlertDialog.Builder(LoginSuccess.this).setTitle("选择学期")
					.setItems(mTime, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, final int whichTime) {

					m_Dialog = ProgressDialog.show(LoginSuccess.this, "请稍后...", "获取数据中...", true);

					new Thread(new Runnable() {

						public void run() {
							if (__VIEWSTATE_xskscx == "") {
								try {
									ksInfo = HttpUtil.getUrl(
											"http://" + myUrl + "/xskscx.aspx?xh=" + xh + "&xm=" + xm
													+ "&gnmkdm=N121604",
											mHttpClient, "http://" + myUrl + "/xs_main.aspx?xh=" + xh);
								} catch (IOException e) {
									e.printStackTrace();
								}

								if (ksInfo != null) {
									tokenizer = new StringTokenizer(ksInfo);
									while (tokenizer.hasMoreTokens()) {
										String valueToken = tokenizer.nextToken();
										// System.out.println(valueToken);
										if (StringUtil.isValue(valueToken, "value") && valueToken.length() > 100) {
											if (StringUtil.getValue(valueToken, "value", "\"", 7).length() > 100) {
												__VIEWSTATE = StringUtil.getValue(valueToken, "value", "\"", 7);// value
												__VIEWSTATE_xskscx = __VIEWSTATE;

											}
										}
									}
								}

							}
							if (__VIEWSTATE_xskscx != "") {
								__VIEWSTATE = __VIEWSTATE_xskscx;

							}
							ss = new String[100];
							i = 0;
							List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
							if (whichTime == 0) {
								xnd = "2012-2013";
								xqd = "1";
							}
							if (whichTime == 1) {
								xnd = "2012-2013";
								xqd = "2";
							}
							if (whichTime == 2) {
								xnd = "2013-2014";
								xqd = "1";
							}
							if (whichTime == 3) {
								xnd = "2013-2014";
								xqd = "2";
							}
							if (whichTime == 4) {
								xnd = "2014-2015";
								xqd = "1";
							}
							if (whichTime == 5) {
								xnd = "2014-2015";
								xqd = "2";
							}
							if (whichTime == 6) {
								xnd = "2015-2016";
								xqd = "1";
							}
							if (whichTime == 7) {
								xnd = "2015-2016";
								xqd = "2";
							}
							pairs.add(new BasicNameValuePair("__EVENTTARGET", "xqd"));
							pairs.add(new BasicNameValuePair("__EVENTARGUMENT", null));
							pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
							pairs.add(new BasicNameValuePair("xnd", xnd));
							pairs.add(new BasicNameValuePair("xqd", xqd));

							String info = "";
							// Log.i("qwe", info);
							try {
								info = HttpUtil.postUrl(
										"http://" + myUrl + "/xskscx.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121604",
										pairs, mHttpClient,
										"http://" + myUrl + "/xskscx.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121604");
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}

							if (info != null) {

								String temp = info.replaceAll("</td>", "</td>\n");// 转化换行
								Pattern p = Pattern.compile("(?<=<td>).*(?=</td>)");
								Matcher m = p.matcher(temp);
								while (m.find()) {
									ss[i] = m.group().toString();
									i++;
								}

							}

							// 更新界面
							handler.post(new Runnable() {
								public void run() {
									Context context = v.getContext();
									Intent intent = new Intent(context, List_ks.class);
									Bundle b = new Bundle();
									b.putStringArray("ss", ss);
									b.putInt("i", i);
									intent.putExtras(b);
									if (ss[0] == null) {
										Toast.makeText(LoginSuccess.this, "没有获取到数据\n请检查网络或服务器是否存在数据！",
												Toast.LENGTH_SHORT).show();
										return;
									} else {
										context.startActivity(intent);
										Toast.makeText(getApplicationContext(), "点击项目可查看详细情况", Toast.LENGTH_SHORT)
												.show();
									}
								}
							});
							m_Dialog.dismiss();
						}
					}).start();

				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int which) {
					d.dismiss();
				}
			}).show();
		}
	};

	// 成绩查询
	OnClickListener getCjClick = new OnClickListener() {
		String __VIEWSTATE = "";
		StringTokenizer tokenizer = null;
		String ddlXN = "";
		String ddlXQ = "";
		String[] ss = null;
		int i = 0;

		public void onClick(final View v) {

			mTime = new String[] { "在校所有成绩查询", "2012-2013 第一学期", "2012-2013 第二学期", "2013-2014 第一学期", "2013-2014 第二学期",
					"2014-2015 第一学期", "2014-2015 第二学期", "2015-2016 第一学期", "2015-2016 第二学期" };
			new AlertDialog.Builder(LoginSuccess.this).setTitle("选择学期")
					.setItems(mTime, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, final int whichTime) {

					m_Dialog = ProgressDialog.show(LoginSuccess.this, "请稍后...", "获取数据中...", true);

					new Thread(new Runnable() {

						public void run() {
							if (__VIEWSTATE_xscj_gc == "") {

								try {

									ksInfo = HttpUtil.getUrl(
											"http://" + myUrl + "/xscj_gc.aspx?xh=" + xh + "&xm=" + xm
													+ "&gnmkdm=N121605",
											mHttpClient, "http://" + myUrl + "/xs_main.aspx?xh=" + xh);
								} catch (IOException e) {
									e.printStackTrace();
								}

								if (ksInfo != null) {
									tokenizer = new StringTokenizer(ksInfo);
									while (tokenizer.hasMoreTokens()) {
										String valueToken = tokenizer.nextToken();
										// System.out.println(valueToken);
										if (StringUtil.isValue(valueToken, "value") && valueToken.length() > 100) {
											if (StringUtil.getValue(valueToken, "value", "\"", 7).length() > 100) {
												__VIEWSTATE = StringUtil.getValue(valueToken, "value", "\"", 7);// value
												__VIEWSTATE_xscj_gc = __VIEWSTATE;
											}
										}
									}
								}

							}
							if (__VIEWSTATE_xscj_gc != "") {
								__VIEWSTATE = __VIEWSTATE_xscj_gc;
							}

							ss = new String[900];
							i = 0;
							List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
							// System.out.println(__VIEWSTATE);
							if (whichTime == 0) {
								ddlXN = null;
								ddlXQ = null;
							}
							if (whichTime == 1) {
								ddlXN = "2012-2013";
								ddlXQ = "1";
							}
							if (whichTime == 2) {
								ddlXN = "2012-2013";
								ddlXQ = "2";
							}
							if (whichTime == 3) {
								ddlXN = "2013-2014";
								ddlXQ = "1";
							}
							if (whichTime == 4) {
								ddlXN = "2013-2014";
								ddlXQ = "2";
							}
							if (whichTime == 5) {
								ddlXN = "2014-2015";
								ddlXQ = "1";
							}
							if (whichTime == 6) {
								ddlXN = "2014-2015";
								ddlXQ = "2";
							}
							if (whichTime == 7) {
								ddlXN = "2015-2016";
								ddlXQ = "1";
							}
							if (whichTime == 8) {
								ddlXN = "2015-2016";
								ddlXQ = "2";
							}

							pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
							pairs.add(new BasicNameValuePair("ddlXN", ddlXN));
							pairs.add(new BasicNameValuePair("ddlXQ", ddlXQ));
							pairs.add(new BasicNameValuePair("Button1", "%B0%B4%D1%A7%C6%DA%B2%E9%D1%AF"));

							String info = null;
							try {
								info = HttpUtil.postUrl(
										"http://" + myUrl + "/xscj_gc.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121605",
										pairs, mHttpClient,
										"http://" + myUrl + "/xscj_gc.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121605");
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							if (info != null) {
								String temp = info.replaceAll("</td>", "</td>\n");// 转化换行
								Pattern p = Pattern.compile("(?<=<td>).*(?=</td>)");
								Matcher m = p.matcher(temp);
								while (m.find() && (!m.group().toString().equals("课程性质名称"))) {
									ss[i] = m.group().toString();
									i++;
								}
							}
							// 更新界面
							handler.post(new Runnable() {
								public void run() {
									Context context = v.getContext();
									Intent intent = new Intent(context, List_cj.class);
									Bundle b = new Bundle();
									b.putStringArray("ss", ss);
									b.putInt("i", i);
									intent.putExtras(b);
									if (ss[0] == null) {
										Toast.makeText(LoginSuccess.this, "没有获取到数据\n请检查网络或服务器是否存在数据！",
												Toast.LENGTH_SHORT).show();
										return;
									} else {
										context.startActivity(intent);
										Toast.makeText(getApplicationContext(), "点击项目可查看详细情况", Toast.LENGTH_SHORT)
												.show();
									}
								}
							});

							m_Dialog.dismiss();
						}
					}).start();

				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int which) {
					d.dismiss();
				}
			}).show();
		}
	};

	// 课表查询
	OnClickListener getKbClick = new OnClickListener() {
		String kbInfo = "";

		public void onClick(final View v) {
			m_Dialog = ProgressDialog.show(LoginSuccess.this, "请稍后...", "获取数据中...", true);

			new Thread(new Runnable() {

				public void run() {

					try {
						kbInfo = HttpUtil.getUrl(
								"http://" + myUrl + "/xskbcx.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121603",
								mHttpClient, "http://" + myUrl + "/xs_main.aspx?xh=" + xh);
					} catch (IOException e) {
						e.printStackTrace();
					}

					String temp = kbInfo.replaceAll("</td>", "</td>\n");// 转化换行
					Pattern p = Pattern.compile("(?<=>).*(?=</td>)");
					Matcher m = p.matcher(temp);
					ss = null;
					ss = new String[200];
					i = 0;
					while (m.find() && (!m.group().toString().equals("编号"))) {
						if (!(m.group().toString().equals("&nbsp;")) && !(m.group().toString().equals(""))
								&& !(m.group().toString().equals("早晨")) && !(m.group().toString().equals("上午"))
								&& !(m.group().toString().equals("下午")) && !(m.group().toString().equals("晚上"))
								&& !(m.group().toString().substring(0, 1).equals("星"))
								&& !(m.group().toString().equals("时间"))
								&& !(m.group().toString().substring(0, 1).equals("第"))) {
							ss[i] = m.group().toString();
							i++;
						}
					}

					// 更新界面
					handler.post(new Runnable() {
						public void run() {
							Context context = v.getContext();
							Intent intent = new Intent(context, List_kb.class);
							Bundle b = new Bundle();
							b.putStringArray("ss", ss);
							b.putInt("i", i);
							intent.putExtras(b);
							if (ss[0] == null) {
								Toast.makeText(LoginSuccess.this, "没有获取到数据\n请检查网络或服务器是否存在数据！", Toast.LENGTH_LONG)
										.show();
								return;
							} else {
								context.startActivity(intent);
							}
						}
					});

					m_Dialog.dismiss();
				}
			}).start();
		}

	};

	// 毕业设计查询
	OnClickListener getByClick = new OnClickListener() {
		String byInfo = "";

		public void onClick(final View v) {
			m_Dialog = ProgressDialog.show(LoginSuccess.this, "请稍后...", "获取数据中...", true);
			new Thread(new Runnable() {
				public void run() {
					try {
						byInfo = HttpUtil.getUrl(
								"http://" + myUrl + "/lw_xsxkcx.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121707",
								mHttpClient, "http://" + myUrl + "/xs_main.aspx?xh=" + xh);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String temp = byInfo.replaceAll("</TD>", "</td>");
					temp = temp.replaceAll("<TD", "<td");
					temp = temp.replaceAll("&nbsp;", "");
					temp = temp.replaceAll("</span>", "");
					temp = temp.replaceAll("<span id=\"tmmc\">", "");
					temp = temp.replaceAll("<span id=\"tmdm\">", "");
					temp = temp.replaceAll("<span id=\"xkkh\">", "");
					temp = temp.replaceAll("<span id=\"Label2\">", "");
					temp = temp.replaceAll("<span id=\"Label1\">", "");
					Pattern p = Pattern.compile("(?<=>).*(?=</td>)");
					Matcher m = p.matcher(temp);
					ss = null;
					ss = new String[200];
					i = 0;
					while (m.find()) {
						ss[i] = m.group().toString().replaceAll("</?[^>]+>", "");
						i++;

					}

					// 更新界面
					handler.post(new Runnable() {
						public void run() {
							Context context = v.getContext();
							Intent intent = new Intent(context, List_by.class);
							Bundle b = new Bundle();
							b.putStringArray("ss", ss);
							b.putInt("i", i);
							intent.putExtras(b);
							if (ss[0] == null) {
								Toast.makeText(LoginSuccess.this, "没有获取到数据\n请检查网络或服务器是否存在数据！", Toast.LENGTH_SHORT)
										.show();
								return;
							} else {
								context.startActivity(intent);
								Toast.makeText(getApplicationContext(), "点击项目可查看详细情况\n此功能仅对大四选题后有数据", Toast.LENGTH_LONG)
										.show();
							}
						}
					});

					m_Dialog.dismiss();
				}
			}).start();
		}

	};

	// 个人头像查询
	OnClickListener getTxClick = new OnClickListener() {
		byte[] txInfo;

		public void onClick(final View v) {
			m_Dialog = ProgressDialog.show(LoginSuccess.this, "请稍后...", "获取数据中...", true);

			new Thread(new Runnable() {

				public void run() {
					try {
						txInfo = HttpUtil.getUrl_byte("http://" + myUrl + "/readimagexs.aspx?xh=" + xh, mHttpClient,
								"http://" + myUrl + "/xs_main.aspx?xh=" + xh);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 更新界面
					handler.post(new Runnable() {
						public void run() {
							Context context = v.getContext();
							Intent intent = new Intent(context, List_tx.class);
							Bundle b = new Bundle();
							b.putByteArray("txInfo", txInfo);
							b.putString("xh", xh);
							intent.putExtras(b);
							if (txInfo == null) {
								Toast.makeText(LoginSuccess.this, "没有获取到数据\n请检查网络或服务器是否存在数据！", Toast.LENGTH_SHORT)
										.show();
								return;
							} else {
								context.startActivity(intent);
							}

						}
					});

					m_Dialog.dismiss();
				}
			}).start();
		}

	};
	TimerTask task = new TimerTask() {
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	// 退出键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
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
		/*
		 * menu.add(Menu.NONE, Menu.FIRST + 2, 2, "帮助").setIcon(
		 * android.R.drawable.ic_menu_help);
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "重新登录").setIcon(android.R.drawable.ic_menu_myplaces);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case Menu.FIRST + 1:
			Intent intent = new Intent(LoginSuccess.this, Login.class);
			LoginSuccess.this.startActivity(intent);
			LoginSuccess.this.finish();
			break;

		/*
		 * case Menu.FIRST + 2: Toast.makeText(this, "By jmhjmh1",
		 * Toast.LENGTH_SHORT).show(); break;
		 */
		}
		return false;
	}
}
