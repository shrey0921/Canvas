package com.shrey.canvas.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Shreyansh on 05-07-2018.
 */
public class LineTool extends Shapes {
    private int x1,x2,y2,y1;

    public LineTool() {
        setMarginValue(50,600,400,50);
    }

    public void rotate(int x, int y, int x0, int y0){
        if(isSelected()) {
            setMarginValue(x, y,x0, y0);
        }
    }
    @Override
    public boolean within(int x, int y) {
        return onBoundary(x,y);
    }

    @Override
    public boolean onBoundary(int x, int y) {
        double l1 = Math.sqrt(Math.pow(x-x1,2)+Math.pow(y-y1,2)) + Math.sqrt(Math.pow(x-x2,2)+Math.pow(y-y2,2)) - Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)) ;
        return (l1 <= 10) && isSelectable();
    }

    @Override
    public void move(int x, int y, int parentWidth, int parentHeight) {
        if(isSelected() && within(x, y)){
            int h = y2 -y1, w = x2 - x1;
            if(x <= w/2){
                x = w/2;
            } else if(x + w / 2 > parentWidth) {
                x = parentWidth - w/2;
            }
            if(y <= h/2){
                y = h/2;
            } else if(y + h / 2 > parentHeight){
                y = parentHeight - h/2;
            }
            setMarginValue(x - w/2,y-h/2,x+w/2, y+h/2);
        }
    }

    @Override
    public void zoom(int factor, int parentWidth, int parentHeight) {
        if(isSelected()){
            int h = Math.abs(y2 -y1), w = Math.abs(x1 - x2);
            if((h <= 20 || w<=20)&& factor < 0)  return;
            if(h>=parentHeight -5 && factor > 0) return;
            if(w>=parentWidth -5 && factor > 0) return;
            factor = factor * 2;
            double m = (y2-y1)/(x2-x1);

            setMarginValue(x1 - factor, y1 - (int)m*factor,x2+ factor, y2+ (int)m*factor);
        }
    }

    @Override
    public void drawText(Canvas canvas) {

    }

    @Override
    public void setMarginValue(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawLine(x1,y1,x2,y2,getPaint());
    }
}
