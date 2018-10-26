package com.shrey.canvas.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Shreyansh on 05-07-2018.
 */
public class TriangleTool extends Shapes {
    private int x1,x2,x3,y1,y2,y3;
    private int left,right,top,bottom;
    private int angle;
    public TriangleTool(){
        setMarginValue(0,0,300,200);
        angle = 0;
    }
    @Override
    public void draw(Canvas canvas, Paint paint) {
        Path path = new Path();
        path.moveTo(x1,y1);
        path.lineTo(x2,y2);
        path.lineTo(x3,y3);
        path.close();
        canvas.drawPath(path,paint);
        drawText(canvas);
    }

    public int getX2() {
        return x2;
    }
    public int getX3() {
        return x3;
    }
    public int getY1() {
        return y1;
    }
    public int getY2() {
        return y2;
    }



    @Override
    public void setMarginValue(int l, int t, int r,int b) {
        this.left = l;
        this.right = r;
        this.top= t;
        this.bottom = b;

        if (angle == 0){
            setXY((l+ r) / 2,t,l,b,r,b);
        }else if (angle == 1){
            setXY(l,t,r,t,r,b);
        }else if (angle == 2){
            setXY(l,t,l,b,r,(t+b)/2);
        }else if (angle == 3){
            setXY(r,t,r,b,l,b);
        }else if (angle == 4){
            setXY(l,t,r,t,(l+r)/2,b);
        }else if (angle == 5){
            setXY(l,t,l,b,r,b);
        }else if (angle == 6){
            setXY(l,(t+b)/2,r,t,r,b);
        }else{
            setXY(l,t,r,t,l,b);
        }
    }

    @Override
    public boolean within(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
//        return area(x1, y1, x2, y2, x3, y3) == (area(x, y, x1, y1, x2, y2) + area(x, y, x2, y2, x3, y3) + area(x, y, x1, y1, x3, y3));
    }
    @Override
    public boolean onBoundary(int x, int y) {
        double a = area(x3, y3, x1, y1, x2, y2);
        double a8 = a * 0.08;
        double a1 = area(x, y, x1, y1, x2, y2),
                a2 = area(x, y, x2, y2, x3, y3),
                a3 = area(x, y, x1, y1, x3, y3);
        return isSelectable() && (a1 <= a8 || a2 <= a8 || a3 <= a8) && (a1+a2+a3 == a);
    }
    @Override
    public void move(int x, int y, int parentWidth, int parentHeight) {
        if(isSelected() && within(x, y)){
            int h =bottom - top, w = right - left;
            left = x - w/2;
            top = y - h/2;

            if(getMaxL()!=-1 && left<=getMaxL()){
                left = getMaxL();
            }else if(getMaxR()!=-1 && (left + w)>=getMaxR()){
                left = getMaxR() - w;
            }else if(x <= w/2){
                left = 0;
            }else if( x + w/2 >= parentWidth){
                left = parentWidth - w;
            }

            if(getMaxT()!=-1 && top<=getMaxT()){
                top = getMaxT();
            }else if(getMaxB()!=-1 && (top + h)>=getMaxB()){
                top = getMaxB() - h;
            }else if(y <= h/2){
                top = 0;
            }else if(y +h/2 >= parentHeight){
                top = parentHeight - h;
            }
            setMarginValue(left ,top,left + w, top + h);
        }
    }

    @Override
    public void zoom(int factor, int parentWidth, int parentHeight) {
        if(isSelected()){
            int h = bottom-top, w =right-left;
            factor = factor * 2;
//            if(h>=getMaxB()-getMaxT() && getMaxT()!=-1){
//                y1 = getMaxT() + factor + 5;
//                y2 = getMaxB() - factor - 5;
//            }
//            if(w>=getMaxR()-getMaxL() && getMaxR()!=-1){
//                x1 = getMaxL() + factor + 5;
//                x2 = getMaxR() - factor - 5;
//            }
            if((h <= 10 || w<=10 )&& factor < 0) return;
            if(h>=parentHeight -5 && factor > 0) return;
            if(w>=parentWidth -5 && factor > 0) return;

            setMarginValue(left - factor, top - factor,right+ factor, bottom+ factor);
        }
    }

    @Override
    public void drawText(Canvas canvas) {
        if(!this.getText().isEmpty()) {
            canvas.drawText(this.getText(),(left+right)/2 - this.getText().length() * 12,(top+bottom)/2 +40,this.getTextPaint());
        }
    }
    private void setXY(int x1,int y1,int x2,int y2,int x3,int y3){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }
    public void rotate(){
        angle = (angle+1)%8;
        setMarginValue(left,top,right,bottom);
    }
    private double area(int x,int y,int x2,int y2,int x3,int y3){ //isosceles triangle
        return 0.5 * Math.abs(x*(y3-y2) + x2*(y - y3) + x3*(y2 - y)) ;
    }
}
