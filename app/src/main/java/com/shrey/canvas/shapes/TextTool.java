package com.shrey.canvas.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Shreyansh on 06-07-2018.
 */
public class TextTool extends Shapes {
    private boolean isFenced;
    private int left,top,right,bottom;
    private boolean rotate;
    private int angle;

    public TextTool(String text,boolean isFenced){
        setText(text);
        rotate = false;
        angle = 0;
        this.isFenced = isFenced;
        setMarginValue(50,50,0,0);
    }
    @Override
    public boolean within(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom && isSelectable();
    }
    @Override
    public boolean onBoundary(int x, int y) {
        int a =20;
        return isSelectable() && (Math.abs(y - top)<a || Math.abs(y - bottom)<a) && (x >= left && x <= right) || (Math.abs(x - left)<a || Math.abs(x - right)<a) && (y >= top && y <= bottom);
    }
    @Override
    public void move(int x, int y, int parentWidth, int parentHeigth) {
        if(isSelected() && within(x, y)) {
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
            setMarginValue(x - w / 2, y - (h / 2), x + w / 2, y + h / 2);
        }
    }
    @Override
    public void zoom(int factor, int parentWidth, int parentHeight) {
        if(isSelected()){
            int h = bottom-top, w = right - left;
            if((h <= 10 || w<=10) && factor < 0) return;
            if(h>=parentHeight -5 && factor > 0) return;
            if(w>=parentWidth -5 && factor > 0) return;
            factor = factor * 2;
            setMarginValue(left - factor, top - factor, right+ factor, bottom+ factor);
        }
    }
    @Override
    public void drawText(Canvas canvas) {
        if(!this.getText().isEmpty()) {
            int y = bottom/2 + top/2+ 10;
            int x = right/2 + left/2 - (int)(this.getText().length() * 0.3 * getTextPaint().getTextSize());
            if (getText().contains("\n")) {
                String[] strs = getText().split("\n");
                int i= 0;
                for (String s : strs){
                    int width = (int)(s.length()* 0.62 * getTextPaint().getTextSize() + left);
                    if (getMaxR()!=-1 && getMaxL()!=-1 && width > (getMaxR() - getMaxL())){
                        int len = getMaxR() - getMaxL();
                        int lines = width / len ;
                        int j=0;
                        for (;j<lines-1;j++) {
                            canvas.drawText(s.substring(j*len,j*len + len), left + 10, y + i * getTextPaint().getTextSize(), this.getTextPaint());
                            i++;
                        }
                        canvas.drawText(s.substring(j*len), left + 10, y + i * getTextPaint().getTextSize(), this.getTextPaint());
                    }else {
                        canvas.drawText(s, left + 10, y + i * getTextPaint().getTextSize(), this.getTextPaint());
                    }
                    i++;
                }
            }else{
                int i= 0;
                if (getMaxR()!=-1 && getMaxL()!=-1){
                    int width = (int)(getText().length()* 0.62 * getTextPaint().getTextSize() + left);
                    int lines =(width / (getMaxR() - getMaxL()))+1 ;
                    int len = width / lines;
                    int j;
                    for (j = 0;j<lines-1;j++){
                        canvas.drawText(getText().substring(j*len,j*len + len), left + 10, y + i * getTextPaint().getTextSize(), this.getTextPaint());
                        i++;
                    }
                    canvas.drawText(getText().substring(j*len), left + 10, y + i * getTextPaint().getTextSize(), this.getTextPaint());
                }else {
                    canvas.drawText(this.getText(), x, y, this.getTextPaint());
                }
            }
        }
    }
    @Override
    public void setMarginValue(int l, int t, int rIgnore, int bIgnore) {
        this.left = l;
        this.top = t;
        if (angle == 0 || angle == 2) {
            this.right = (int)(getText().length()* 0.62 * getTextPaint().getTextSize() + left);
            if (right >= getMaxR() && getMaxR() != -1) {
                right = getMaxR();
            }
            this.bottom = 50 + top;
        }else{
            this.right = 50 + left;
            this.bottom = (int)(getText().length()* 0.62 * getTextPaint().getTextSize() + top);
        }

//        this.right = (int)(this.left + );
//        this.bottom = this.top + 50;
    }
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (rotate) {
            canvas.save();
            canvas.rotate(90 * angle, (left + right) / 2, (top + bottom) / 2);
        }
        drawText(canvas);
        if (rotate) {
            canvas.restore();
        }
        if (isFenced || isSelected()){
            canvas.drawRect(left-15,top-5,right+15,bottom+5,paint);
        }
    }
    public void rotate(){
        rotate = true;
        angle = (angle+1)%4;

        setMarginValue(left,top,left + Math.abs(top-bottom),top + Math.abs(left-right));
    }
}
