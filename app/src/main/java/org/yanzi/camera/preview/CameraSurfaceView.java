package org.yanzi.camera.preview;

import org.yanzi.activity.CameraActivity;
import org.yanzi.activity.HttpApplication;
import org.yanzi.activity.MainActivity;
import org.yanzi.activity.MeidaActivity;
import org.yanzi.camera.CameraInterface;
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

	public CameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		paint = new Paint();
		paint.setColor(Color.RED);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent��͸�� transparent͸��
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);
        area = new Rect(0, 0, 800, 600);
        matrix = new Matrix();
        matrix.postRotate(270);
    }


	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceCreated...");
		camera = CameraInterface.getInstance().doOpenCamera(null, CameraInfo.CAMERA_FACING_FRONT);
		camera.setDisplayOrientation(0);
		camera.setPreviewCallback(this);


	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceChanged...");


		CameraInterface.getInstance().doStartPreview(mSurfaceHolder, 1.333f);

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

	int num = 0;

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		faceData = data;
		if (data != null) {
			test = Util.strToArr(JniTool.faceDetectCamera(data, 800, 600));
//			if(CameraActivity.faceView != null){
//				CameraActivity.faceView.setRects(CameraActivity.getRect(new Rect((test[1])*3/2, (test[2])*3/2, ((test[1]+test[3]))*3/2,((test[2]+test[4]))*3/2)));
//			}
			Log.d("Test", test[0]+"------------------"+ISSHOWINGMOVIE);
//			if(test[0] == 0){
//				if(num >= 300 && !ISSHOWINGMOVIE){
//					Log.d("Test", "zhi xing le a");
//					num = 0;
//					setAlpha(0);
//					Log.d("TT", getWidth()+"");
//					CameraActivity.sv_movie.setTranslationX(0f);
//					ISSHOWINGMOVIE = true;
//					CameraActivity.ll_panel.setVisibility(View.INVISIBLE);
//					CameraActivity.showMovie();
//				} else {
//					Log.d("Test", "num ' value is "+num);
//					num++;
//				}
//
//
//
//
////			} else if(test[0] == 1 && ISSHOWINGMOVIE){
////				Log.d("Test", "gai hui lai le********************************");
////				CameraActivity.stopMovie();
////				CameraActivity.sv_movie.setTranslationX(-1280f);
////				CameraActivity.ll_panel.setVisibility(View.VISIBLE);
////				setAlpha(1);
////				ISSHOWINGMOVIE = false;
//			}
		}

//		image = new YuvImage(data, 17, 800, 600, null);
//		Bitmap bitmap1 = BitmapFactory.decodeByteArray(image.getYuvData(), 0, image.getYuvData().length);
//		Log.d("Test", (bitmap1 == null)+"");
//		image.getYuvData();
//		out = new ByteArrayOutputStream();
//        image.compressToJpeg(area, 100, out);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
//
//        Util.saveBitmap(bitmap, "zhufu.jpg");
//
//        bm = Bitmap.createBitmap(bitmap, 0, 0,800, 600, matrix, true);


	}

//	Camera.Parameters params = camera.getParameters();
//	int w = params.getPreviewSize().width;
//	int h = params.getPreviewSize().height;
//	int format = params.getPreviewFormat();
//	YuvImage image = new YuvImage(data, format, w, h, null);
//	ByteArrayOutputStream out = new ByteArrayOutputStream();
//	Rect area = new Rect(0, 0, w, h);
//	image.compressToJpeg(area, 100, out);
//	Bitmap bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
//	Matrix matrix = new Matrix();
//	matrix.postRotate(270);
//	bm = Bitmap.createBitmap(bitmap, 0, 0,w, h, matrix, true);
}
