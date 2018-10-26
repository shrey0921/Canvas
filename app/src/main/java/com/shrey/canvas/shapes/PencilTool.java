package com.shrey.canvas.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreyansh on 09-08-2018.
 */
public class PencilTool extends Shapes{
    private List<Point> points;
    private boolean draw;
    public PencilTool(){
        points = new ArrayList<>();
        enableDraw(true);
    }
    @Override
    public boolean within(int x, int y) {
        return onBoundary(x,y);
    }

    @Override
    public boolean onBoundary(int x, int y) {
        int dx = 20;
        for (Point pt : points){
            if((pt.x <= x + dx && pt.x>=x - dx ) && (pt.y <= y + dx && pt.y>=y - dx)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void move(int dx, int dy, int parentWidth, int parentHeight) {
        dx = (int)(dx * 0.05);
        dy = (int)(dy * 0.05);
        boolean mx = false,my = false;
        for (Point pt: points){
            if (pt.x + dx > parentWidth || pt.x+dx< 0){
                mx = true;
            }
            if (pt.y +dy > parentHeight || pt.y +dy < 0){
                my = true;
            }
        }
        for (Point pt: points) {
            if (!mx)
                pt.x = pt.x + dx;
            if (!my)
                pt.y = pt.y + dy;
        }
    }

    @Override
    public void zoom(int factor, int parentWidth, int parentHeight) {

    }

    @Override
    public void drawText(Canvas canvas) {

    }

    @Override
    public void setMarginValue(int l, int t, int r, int b) {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        Path path = new Path();
        if (points.size()>1) {
            path.moveTo(points.get(0).x,points.get(0).y);
            for (Point p : points) {
                path.lineTo(p.x, p.y);
            }
        }
        canvas.drawPath(path,paint);
    }

    public void modify() { //todo create a straight line
        if (points.size()>1) {
            final int n = 15;
            List<Point> pts = new ArrayList<>();
            int size = points.size();
            for (int i = 0 ;i<size ;i=i+n) {
                if (i%n==0){
                    pts.add(points.get(i));
                }
            }
            pts.add(points.get(size-1));
            size = pts.size();
            int preX = pts.get(0).x,preY=pts.get(0).y;
            for (int i = 0 ;i<size ;i++) {
                Point p = pts.get(i);
                if (Math.abs(preX - p.x) <  Math.abs(preY - p.y)){
                    p.x = preX;
                    preY = p.y;
                }else{
                    p.y = preY;
                    preX = p.x;
                }
            }
             points = pts;
        }
    }
    public void enableDraw(boolean d){
        this.draw = d;
        this.setSelection(d);
    }
    public boolean drawEnabled(){
        return this.draw;
    }

    public void add(float x, float y) {
        points.add(new Point((int)x,(int)y));
    }
}
