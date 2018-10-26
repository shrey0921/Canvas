package com.shrey.canvas.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.view.View;

import com.shrey.canvas.PaintValues;

abstract class Shapes extends Shape {
    private boolean selected;
    private Paint paint,textPaint;
    private String text;
    private int maxL,maxT,maxR,maxB;
    private boolean isSelectable;
    private int strokeColor;
    private PaintValues paintValue;

    Shapes(){
        this.text = "";
        this.selected = false;
        this.paint = new Paint();
        this.textPaint = new Paint();
        this.strokeColor = Color.BLACK;
        this.setMaxXY(-1,-1,-1,-1);
        paint.setColor(strokeColor);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        isSelectable(true);
    }
    public void setPaintValue(PaintValues pv){
        this.paintValue = pv;
    }
    public void setSelection(boolean selected){
        if(selected){
            paint.setColor(Color.RED);
            if (paintValue != null) {
                onSelected();
            }
        }else{
            paint.setColor(strokeColor);
            if (paintValue != null)
                paintValue.setVisibility(View.INVISIBLE);
        }
        this.selected = selected;
    }
    private void onSelected(){
        if (this instanceof TextTool) {
            paintValue.isText(0);
        }else if (this instanceof PencilTool){
            paintValue.isText(1);
        }else if(this instanceof RectangleTool){
            paintValue.isText(2);
        }else{
            paintValue.isText(9);
        }

        paintValue.setVisibility(View.VISIBLE);
        paintValue.setColor(strokeColor);
        if (paint.getStyle().equals(Paint.Style.FILL) || paint.getStyle().equals(Paint.Style.FILL_AND_STROKE)){
            paintValue.setFill(true);
        }else{
            paintValue.setFill(false);
        }
        paintValue.setStrokeWidth(paint.getStrokeWidth());
        paintValue.setTextColor(textPaint.getColor());
        paintValue.setTextSize(textPaint.getTextSize());
        paintValue.setOKClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setStrokeWidth(paintValue.getStrokeWidth());
                strokeColor = paintValue.getColor();
                paint.setStyle(paintValue.getFill()? Paint.Style.FILL: Paint.Style.STROKE);
                textPaint.setColor(paintValue.getTextColor());
                textPaint.setTextSize(paintValue.getTextSize());

                setSelection(false);
            }
        });
    }
    public boolean isSelected(){
        return this.selected;
    }
    public void isSelectable(boolean set){this.isSelectable = set;}
    boolean isSelectable(){return this.isSelectable;}
    public abstract boolean within(int x,int y);
    public abstract boolean onBoundary(int x,int y);
    public abstract void move (int x, int y,int parentWidth,int parentHeight) ;
    public abstract void zoom(int factor,int parentWidth,int parentHeight);
    public abstract void drawText(Canvas canvas);
    public abstract void setMarginValue(int l, int t, int r,int b);

    public Paint getPaint() {
        return paint;
    }
    public void setPaint(Paint paint) {
        this.paint = paint;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text.toUpperCase();
    }
    public void setTextPaint(Paint txt){
        textPaint = txt;
    }
    public Paint getTextPaint() {
        return textPaint;
    }

    public void setMaxXY(int l,int t,int r,int b){
        this.maxL = l;
        this.maxT = t;
        this.maxR = r;
        this.maxB = b;
    }
    int getMaxL() {
        return maxL;
    }
    int getMaxT() {
        return maxT;
    }
    int getMaxR() {
        return maxR;
    }
    int getMaxB() {
        return maxB;
    }
}
