package com.shrey.canvas.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Shreyansh on 03-07-2018.
 */
public class RectangleTool extends Shapes {
    private int left,top,right,bottom;
    private boolean rotate;
    private int angle;
    public int getL(){return left;}
    public int getR(){return right;}
    public int getB(){return bottom;}
    public int getT(){return top;}

    public RectangleTool(){
        rotate = false;
        angle = 0;
        setMarginValue(10,10,400,400);
    }
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(left, top, right, bottom, paint);
        if (rotate) {
            canvas.save();
            canvas.rotate(90 * angle, (left + right) / 2, (top + bottom) / 2);
        }
        drawText(canvas);
        if (rotate) {
            canvas.restore();
        }
    }
    @Override
    public void setMarginValue(int l, int t, int r, int b){
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
    }
    public void drawText(Canvas canvas) {
        if(!this.getText().isEmpty()) {
            int w = this.right -this.left;
            int l = (int)(this.getText().length() * (this.getTextPaint().getTextSize()) * 0.7);
            if (l<w) {
                canvas.drawText(this.getText(), (this.right + this.left) / 2 - l / 2, (this.top + this.bottom) / 2, this.getTextPaint());
            } else {
                int size = l/w + 1;
                String[] str;
                if (getText().contains(" ")) {
                    str = this.getText().split(" ");
                } else {
                    str = new String[size];
                    int ll = getText().length() / size;
                    for (int i=0;i<size;i++){
                        str[i] = getText().substring(i*ll,(i+1)*ll);
                    }
                }
                for ( int i = 0;i<str.length;i++){
                    String s = str[i];
                    l = (int)(s.length() * (this.getTextPaint().getTextSize()) * 0.62);
                    canvas.drawText(s,
                            (this.right + this.left) / 2 - l / 2,
                            (this.top + this.bottom) / 2 + (i - str.length/2)*getTextPaint().getTextSize(),
                                this.getTextPaint());
                }
            }
        }
    }
    @Override
    public void move(int x, int y,int parentWidth,int parentHeight){
        if(isSelected() && within(x, y)) {
            int h = bottom-top, w = right - left;
            if(getMaxL()!=-1 && (x-w/2)<=getMaxL()){
                x = w/2 + getMaxL();
            }else if(getMaxR()!=-1 && (x+w/2)>=getMaxR()){
                x = getMaxR() - w/2;
            }else if(x <= w/2){
                x = w/2;
            }else if(x + w / 2 > parentWidth){
                x = parentWidth - w/2;
            }

            if(getMaxT()!=-1 && (y-h/2)<=getMaxT()){
                y = h/2 + getMaxT();
            }else if(getMaxB()!=-1 && (y+h/2)>=getMaxB()){
                y = getMaxB() - h/2;
            }else if(y <= h/2){
                y = h/2;
            }else if(y + h / 2 > parentHeight){
                y = parentHeight - h/2;
            }
            setMarginValue(x - w / 2 , y - (h / 2), x + w / 2, y + h / 2);
        }
    }

    @Override
    public void zoom(int factor,int parentWidth,int parentHeight) {
        if(isSelected()){
            int h = bottom-top, w = right - left;
            if(h>=getMaxB()-getMaxT() && getMaxT()!=-1){
                top = getMaxT() + factor + 5;
                bottom = getMaxB() - factor - 5;
            }
            if(w>=getMaxR()-getMaxL() && getMaxR()!=-1){
                left = getMaxL() + factor + 5;
                right = getMaxR() - factor - 5;
            }
            if((h <= 10 || w<=10) && factor < 0) return;
            if((h>=parentHeight -5 )&& factor > 0) return;
            if(w>=parentWidth -5  && factor > 0) return;

            setMarginValue(left - factor, top - factor, right+ factor, bottom+ factor);
        }
    }

    @Override
    public boolean within(int x, int y){
        return x >= left && x <= right && y >= top && y <= bottom;
    }
    @Override
    public boolean onBoundary(int x, int y) {
        int a = 20;
        return isSelectable() && (Math.abs(y - top)<a || Math.abs(y - bottom)<a) && (x >= left && x <= right) || (Math.abs(x - left)<a || Math.abs(x - right)<a) && (y >= top && y <= bottom);
    }

    public void zoomY(int factor, int parentWidth, int parentHeight) {
        if(isSelected()){
            int h = bottom-top, w = right - left;
            if((h <= 10 || w<=10) && factor < 0) return;
            if(h>=parentHeight -5 && factor > 0) return;
            if(w>=parentWidth -5 && factor > 0) return;
            factor = factor * 2;
            setMarginValue(left, top - factor, right, bottom+ factor);
        }
    }
    public void zoomX(int factor, int parentWidth, int parentHeight) {
        if(isSelected()) {
            int h = bottom-top, w = right - left;
            if((h <= 10 || w<=10) && factor < 0) return;
            if(h>=parentHeight -5 && factor > 0) return;
            if(w>=parentWidth -5 && factor > 0) return;
            factor = factor * 2;
            setMarginValue(left- factor, top , right+ factor, bottom);
        }
    }
    public void rotate(){
        rotate = true;
        angle = (angle+1)%4;
        setMarginValue(left,top,left + Math.abs(top-bottom),top + Math.abs(left-right));
    }
}
