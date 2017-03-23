package org.yanzi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.name;
import static android.content.Context.TELEPHONY_SERVICE;

public class Util {

    private static BufferedOutputStream bos;

    public Util(){

    }

    public static void getCameraSize(){
        Camera camera = Camera.open();
        if(camera == null){
            Log.d("TT", "camera is null");
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        //List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            Log.d("TT", supportedPreviewSizes.get(i).width+" "+supportedPreviewSizes.get(i).height);
        }
    }

    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    //Bitmap转换成String
    public static String bitmap2String(Bitmap bm) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int bytes = bm.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bm.copyPixelsToBuffer(buf);

        byte[] byteArray = buf.array();
        return new String(byteArray);
    }



    public static int[] strToArr(String str){
        String[] strs = str.split("/");
        int[] ints = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            ints[i] = Integer.valueOf(strs[i]);
        }
        return ints;
    }

    public static String getIMEI(Context ctx){
        TelephonyManager TelephonyMgr = (TelephonyManager)ctx.getSystemService(TELEPHONY_SERVICE);
        return  TelephonyMgr.getDeviceId();
    }


    public static int[] bitmapToPixels(Bitmap bitmap){
        int [] pix = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(pix, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return pix;
    }

    public Bitmap rotateBitmap(Bitmap bm, Matrix m)
    {
        Log.d("KK", bm.getWidth()+" "+bm.getHeight());
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
    }

    public static String getCurrTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str =  formatter.format(curDate);
        return str;
    }

    public static void saveBitmap(Bitmap bitmap, String name){
        try {
            bos = new BufferedOutputStream(new
                    FileOutputStream("/data/data/org.yanzi.playcamera/camera_img/"+name));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("TT", "发生了异常"+e.getMessage());
        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
            int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }
}
