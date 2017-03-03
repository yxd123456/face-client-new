package org.yanzi.ui;

import org.yanzi.camera.CameraInterface;
import org.yanzi.playcamera.R;
import org.yanzi.util.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FaceView extends ImageView {
	private static final String TAG = "YanZi";
	private Context mContext;
	private Paint mLinePaint;
	private Face[] mFaces;
	private Matrix mMatrix = new Matrix();
	private RectF mRect = new RectF();

	private Drawable mFaceIndicator = null;
	private Rect rect;


	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//initPaint();
		mContext = context;
		mFaceIndicator = getResources().getDrawable(R.drawable.ic_face_find_2);
	}

	public void setRects(Rect rect){
		this.rect = rect;
		invalidate();
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(rect == null){
			return;
		}
		boolean isMirror = false;
		int Id = CameraInterface.getInstance().getCameraId();
		if(Id == CameraInfo.CAMERA_FACING_BACK){
			isMirror = true; //����Camera����mirror
		}else if(Id == CameraInfo.CAMERA_FACING_FRONT){
			isMirror = true;  //ǰ��Camera��Ҫmirror
		}
		Util.prepareMatrix(mMatrix, isMirror, 90, getWidth(), getHeight());
		canvas.save();
		mMatrix.postRotate(0); //Matrix.postRotateĬ����˳ʱ��
		canvas.rotate(0);   //Canvas.rotate()Ĭ������ʱ��
		mRect.set(rect);
		mMatrix.mapRect(mRect);
		mFaceIndicator.setBounds(Math.round(rect.left), Math.round(rect.top),
				Math.round(rect.right), Math.round(rect.bottom));
		mFaceIndicator.draw(canvas);
//			canvas.drawRect(mRect, mLinePaint);
		canvas.restore();
		super.onDraw(canvas);
	}

	private void initPaint(){
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		int color = Color.rgb(0, 150, 255);
		int color = Color.rgb(10, 10, 10);
//		mLinePaint.setColor(Color.RED);
		mLinePaint.setColor(color);
		mLinePaint.setStyle(Style.STROKE);
		mLinePaint.setStrokeWidth(100f);
		mLinePaint.setAlpha(180);
	}
}
