package com.shrey.canvas.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;

/**
 * Created by Shreyansh on 06-07-2018.
 */
public class ImageTool extends Shapes {
    private Bitmap bmp;
    private int left,top,right,bottom;
    private boolean rotate;
    private int angle;
    public ImageTool(Drawable drawable){
        rotate = false;
        angle = 0;
        if(drawable instanceof BitmapDrawable) {
            this.bmp = ((BitmapDrawable) drawable).getBitmap();
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(drawable instanceof VectorDrawable){
                try {
                    bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bmp);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                } catch (OutOfMemoryError ignored) {
                }
            }
        }
        setMarginValue(10,10);
    }
    @Override
    public void zoom(int factor, int parentWidth, int parentHeigth) {
    }

    @Override
    public void drawText(Canvas canvas) {
    }

    @Override
    public void setMarginValue(int l, int t, int rIgnore, int bIgnore) {
        this.left = l;
        this.top = t;
        if (angle == 0 || angle == 2) {
            this.right = bmp.getWidth() + left;
            this.bottom = bmp.getHeight() + top;
        }else{
            this.right = bmp.getHeight() + left;
            this.bottom = bmp.getWidth() + top;
        }
    }

    public void setMarginValue(int l,int t){
        setMarginValue(l,t,0,0);
    }
    @Override
    public boolean within(int x, int y) {
        return (x >= left && x<=right && y >= top && y <= bottom) ;
    }
    @Override
    public boolean onBoundary(int x, int y) {
        int a =20;
        return isSelectable() && (Math.abs(y - top)<a || Math.abs(y - bottom)<a) && (x >= left && x <= right) || (Math.abs(x - left)<a || Math.abs(x - right)<a) && (y >= top && y <= bottom);
    }
    @Override
    public void move(int x, int y, int parentWidth, int parentHeigth) {
        if(isSelected()&& within(x, y)) {
            int h = bottom-top, w = right - left;

            if(x <= w/2){
                x = w/2;
            }else if(x + w / 2 > parentWidth){
                x = parentWidth - w/2;
            }
            if(y <= h/2){
                y = h/2;
            }else if(y + h / 2 > parentHeigth){
                y = parentHeigth - h/2;
            }
            setMarginValue(x - w / 2, y - (h / 2));
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (rotate) {
            canvas.save();
            canvas.rotate(90 * angle, left, top);
        }
        if (angle == 0) {
            canvas.drawBitmap(bmp, left, top, paint);
        }else if (angle ==1) {
            canvas.drawBitmap(bmp, left, top-bmp.getHeight(), paint);
        }else if(angle == 2){
            canvas.drawBitmap(bmp, left - bmp.getWidth(), top - bmp.getHeight() , paint);
        }else if (angle == 3){
            canvas.drawBitmap(bmp, left- bmp.getWidth(), top , paint);
        }

        if (rotate) {
            canvas.restore();
        }
        if(isSelected()){
            canvas.drawRect(left-4,top-4,right+4,bottom+4,getPaint());
        }
    }
    public void rotate(){
        rotate = true;
        angle = (angle+1)%4;
        setMarginValue(left,top);
    }
}
