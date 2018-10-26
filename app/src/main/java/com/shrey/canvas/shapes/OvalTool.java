package com.shrey.canvas.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Shreyansh on 20-07-2018.
 */
public class OvalTool extends Shapes {
    private int left,top,right,bottom;
    public int getL(){return left;}
    public int getR(){return right;}
    public int getB(){return bottom;}
    public int getT(){return top;}
    public OvalTool(){
        setMarginValue(10,10,800,400);
    }
    @Override
    public boolean within(int x, int y) {
        return false;
    }

    @Override
    public boolean onBoundary(int x, int y) {
        return false;
    }

    @Override
    public void move(int x, int y, int parentWidth, int parentHeight) {

    }

    @Override
    public void zoom(int factor, int parentWidth, int parentHeight) {

    }

    @Override
    public void drawText(Canvas canvas) {

    }

    @Override
    public void setMarginValue(int l, int t, int r, int b) {
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawOval(new RectF(left,top,right,bottom),paint);
    }
}
