package com.feedu.main;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;

public class MyApp extends Application {

	private String myUrl;
	private DefaultHttpClient mHttpClient;

	public String getMyUrl() {
		return myUrl;
	}

	public void setMyUrl(String myUrl) {
		this.myUrl = myUrl;
	}

	public DefaultHttpClient getmHttpClient() {
		return mHttpClient;
	}

	public void setmHttpClient(DefaultHttpClient mHttpClient) {
		this.mHttpClient = mHttpClient;
	}
}