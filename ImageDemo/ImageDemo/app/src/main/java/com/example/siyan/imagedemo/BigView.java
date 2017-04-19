package com.example.siyan.imagedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by siYan on 2016/10/29.
 */
public class BigView extends View {

    private BitmapRegionDecoder decoder;
    private int imageWidth;
    private int imageHeight;
    private Rect currentRect;

    private static BitmapFactory.Options ops = new BitmapFactory.Options();

    static {
        ops.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    private int downX;
    private int downY;
    private int measuredWidth;
    private int measuredHeight;

    public BigView(Context context) {
        super(context);
    }

    public BigView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取屏幕宽和高
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();


        int top = imageHeight / 2 - measuredHeight / 2;
        int bottom = imageHeight / 2 + measuredHeight / 2;
        int left = imageWidth / 2 - measuredWidth / 2;
        int right = imageWidth / 2 + measuredWidth / 2;


        currentRect = new Rect(left, top, right, bottom);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //获取当前的x轴和轴
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:

                //获取移动后的x轴和y轴
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();

                int diffX = downX - moveX;
                int diffY = downY - moveY;

                downX = moveX;
                downY = moveY;

                refreshRect(diffX, diffY);
                System.out.println("按下的x:" + downX + " 移动后的x:" + moveX);

                break;
        }
        invalidate();
        return true;
    }

    private void refreshRect(int diffX, int diffY) {
        currentRect.offset(diffX, diffY);

        if (currentRect.left <= 0) {
            currentRect.left = 0;
            currentRect.right = measuredWidth;
        } else if (currentRect.right >= imageWidth) {
            currentRect.right = imageWidth;
           currentRect.left=imageWidth-measuredWidth;
        }


        if (currentRect.top<=0){
            currentRect.top=0;
            currentRect.bottom=measuredHeight;
        }else if(currentRect.bottom>=imageHeight){
            currentRect.bottom=imageHeight;
            currentRect.top=imageHeight-measuredHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //加载图片
        Bitmap bitmap = decoder.decodeRegion(currentRect, ops);

        //画图片
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * @param inputStream 代表着超大图片文件
     */
    public void setInput(InputStream inputStream) {
               //[1] 首先获取图片的宽和高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize=4;
        BitmapFactory.decodeStream(inputStream, null, options);

        //[1.1]取到图片的宽和高
        imageWidth = options.outWidth;
        imageHeight = options.outHeight;


        //[2]加载这张图片
        try {
            decoder = BitmapRegionDecoder.newInstance(inputStream, false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
