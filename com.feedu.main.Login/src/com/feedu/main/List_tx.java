package com.feedu.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class List_tx extends Activity {

	private ImageView imageView1;
	private Button saveButton;
	private ZoomControls zoom;
	private Bitmap bmp;
	private String xh = "";
	private ProgressDialog m_Dialog = null;
	private Handler handler = new Handler();
	private int zooms = 0;
	private float scaleWidth = 1;
	private float scaleHeight = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_tx);

		Bundle b = this.getIntent().getExtras();
		byte[] txInfo = b.getByteArray("txInfo");
		xh = b.getString("xh");

		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(saveClick);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		bmp = BitmapFactory.decodeByteArray(txInfo, 0, txInfo.length);// 生成位图
		imageView1.setImageBitmap(bmp);

		zoom = (ZoomControls) findViewById(R.id.zoomcontrol);
		zoom.setIsZoomInEnabled(true);
		zoom.setIsZoomOutEnabled(true);

		// 图片放大
		zoom.setOnZoomInClickListener(new OnClickListener() {
			public void onClick(View v) {
				zoom.setIsZoomOutEnabled(true);
				int bmpWidth = bmp.getWidth();
				int bmpHeight = bmp.getHeight();
				// 设置图片放大但比例
				double scale = 1.25;
				// 计算这次要放大的比例
				if (zooms >= -3 && zooms < 6) {
					scaleWidth = (float) (scaleWidth * scale);
					scaleHeight = (float) (scaleHeight * scale);
					// 产生新的大小但Bitmap对象
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleHeight);
					Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
					imageView1.setImageBitmap(resizeBmp);
					zooms++;
				} else
					zoom.setIsZoomInEnabled(false);

			}

		});
		// 图片减小
		zoom.setOnZoomOutClickListener(new OnClickListener() {

			public void onClick(View v) {
				zoom.setIsZoomInEnabled(true);
				int bmpWidth = bmp.getWidth();
				int bmpHeight = bmp.getHeight();
				// 设置图片放大但比例
				double scale = 0.8;
				// 计算这次要放大的比例
				if (zooms > -3 && zooms <= 6) {
					scaleWidth = (float) (scaleWidth * scale);
					scaleHeight = (float) (scaleHeight * scale);
					// 产生新的大小Bitmap对象
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleHeight);
					Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
					imageView1.setImageBitmap(resizeBmp);
					zooms--;
				} else
					zoom.setIsZoomOutEnabled(false);
			}

		});

	}

	// 保存
	OnClickListener saveClick = new OnClickListener() {

		public void onClick(final View v) {
			m_Dialog = ProgressDialog.show(List_tx.this, "请稍后...", "图像保存中...", true);

			new Thread(new Runnable() {

				public void run() {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + xh + ".jpg");
						try {
							FileOutputStream out = new FileOutputStream(file);
							if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
								out.flush();
								out.close();
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						Toast.makeText(getApplicationContext(), "保存失败，SD卡不存在！", Toast.LENGTH_SHORT).show();
					}
					// 更新界面
					handler.post(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),
									"头像保存在" + Environment.getExternalStorageDirectory().toString() + "/" + xh + ".jpg",
									Toast.LENGTH_SHORT).show();
						}
					});
					m_Dialog.dismiss();
				}
			}).start();
		}

	};

}