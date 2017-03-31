package org.yanzi.camera.preview;

import org.yanzi.activity.CameraActivity;
import org.yanzi.activity.HttpApplication;
import org.yanzi.activity.MainActivity;
import org.yanzi.activity.MeidaActivity;
import org.yanzi.camera.CameraInterface;
import org.yanzi.constant.Constant;
import org.yanzi.playcamera.R;
import org.yanzi.util.JniTool;
import org.yanzi.util.Util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huashi.otg.sdk.HsOtgService;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static android.R.attr.bitmap;
import static android.R.attr.testOnly;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback,
		Camera.PreviewCallback{
	private static final String TAG = "yanzi";
    private final Matrix matrix;
    CameraInterface mCameraInterface;
	Context mContext;
	SurfaceHolder mSurfaceHolder;
	Camera camera;
	Bitmap bm, bitmap;
	Paint paint;
    private Rect area;
	public static boolean ISSHOWINGMOVIE = false;
	public static Thread movieThread;


	public CameraSurfaceView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		paint = new Paint();
		paint.setColor(Color.RED);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent��͸�� transparent͸��
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);
        area = new Rect(0, 0, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        matrix = new Matrix();
        matrix.postRotate(270);
		if(movieThread == null){
			movieThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for (int i = 0; i < 60; i++) {
							Log.d("TT", "还有"+(60-i)+"s");
							Thread.sleep(1000);
						}
						CameraActivity.sv_movie.setTranslationX(0f);
						ISSHOWINGMOVIE = true;
						((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								CameraActivity.ll_panel.setVisibility(View.INVISIBLE);
							}
						});
						CameraActivity.showMovie();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
    }


	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceCreated...");
		try{
			camera = CameraInterface.getInstance().doOpenCamera(null, CameraInfo.CAMERA_FACING_FRONT);
			camera.setDisplayOrientation(180);
			Camera.Parameters parameters = camera.getParameters();
			List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
			//List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
			for (int i = 0; i < supportedPreviewSizes.size(); i++) {
				Log.d("TT", supportedPreviewSizes.get(i).width+" "+supportedPreviewSizes.get(i).height);
			}
			camera.setPreviewCallback(this);
		}catch (Exception e){

		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceChanged...");

		try{
			CameraInterface.getInstance().doStartPreview(mSurfaceHolder, 1.333f);

		}catch (Exception e){

		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceDestroyed...");
		CameraInterface.getInstance().doStopCamera();

	}
	public SurfaceHolder getSurfaceHolder(){
		return mSurfaceHolder;
	}

	byte[] faceData;

	public byte[] saveScreenshot() {
		if(faceData != null){
			return faceData;
		}
		return null;
	}

	/**
	 * http://blog.csdn.net/xu_fu/article/details/23087951
	 * Android自拍相机应用——图片的镜像翻转
     */
	public static Bitmap convertBmp(Bitmap bmp) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(-1, 1); // 镜像水平翻转
		Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);

		return convertBmp;
	}

	YuvImage image;
    ByteArrayOutputStream out;

	int[] test;

	public int[] getFaceRect(){
		return test;
	}

	public static void startTimeToShow(){

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		faceData = data;
		if (data != null) {
			test = Util.strToArr(JniTool.faceDetectCamera(data, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
//			if(CameraActivity.faceView != null){
//				CameraActivity.faceView.setRects(CameraActivity.getRect(new Rect((test[1])*3/2, (test[2])*3/2, ((test[1]+test[3]))*3/2,((test[2]+test[4]))*3/2)));
//			}
			Log.d("NUM", test[0]+"------------------"+ISSHOWINGMOVIE);
		}

	}
}
