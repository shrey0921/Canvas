package com.shrey.canvas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.shrey.canvas.shapes.ImageTool;
import com.shrey.canvas.shapes.LineTool;
import com.shrey.canvas.shapes.OvalTool;
import com.shrey.canvas.shapes.PencilTool;
import com.shrey.canvas.shapes.RectangleTool;
import com.shrey.canvas.shapes.TextTool;
import com.shrey.canvas.shapes.TriangleTool;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {
    private List<Shape> shapes;
    private Shape selectedShape;

    private float preX = 0, preY =0;
    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        shapes = new ArrayList<>();
    }
    private int downX,downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.performClick();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downX = x;
            downY = y;
            selectedShape = null;
            RectangleTool rectSelected = null;
            TriangleTool triSelected = null;
            LineTool lineToolSel = null;
            ImageTool imgSel = null;
            TextTool txtSel = null;
            PencilTool penTool = null;
            int select = -1;
            for (Shape shape : shapes) {
                if(shape instanceof RectangleTool) {
                    RectangleTool rect = (RectangleTool) shape;
                    if(rect.onBoundary(x, y)){
                        rectSelected = rect;
                        select = 0;
                    }
                    if (rect.within(x, y) && rect.isSelected()) {
                        rectSelected = rect;
                    }
                    rect.setSelection(false);
                }else if (shape instanceof TriangleTool){
                    TriangleTool tria = (TriangleTool) shape;
                    if(tria.onBoundary(x, y)){
                        triSelected = tria;
                        select = 1;
                    }
                    if (tria.within(x, y) && tria.isSelected()) {
                        triSelected = tria;
                    }
                    tria.setSelection(false);
                }else if (shape instanceof LineTool){
                    LineTool lin = (LineTool) shape;
                    if (lin.onBoundary(x, y)) {
                        lineToolSel = lin;
                        select = 2;
                    }
                    lin.setSelection(false);
                }else if (shape instanceof ImageTool){
                    ImageTool img = (ImageTool) shape;
                    if(img.within(x, y)){
                        imgSel = img;
                        select = 3;
                    }
                    img.setSelection(false);
                }else if(shape instanceof TextTool) {
                    TextTool txt = (TextTool) shape;
                    if(txt.within(x, y)){
                        txtSel = txt;
                        select = 4;
                    }
                    txt.setSelection(false);
                }else if (shape instanceof PencilTool){
                    PencilTool pTool = (PencilTool)shape;
                    if (pTool.within(x,y)){
                        penTool = pTool;
                        select = 5;
                    }
                    pTool.setSelection(false);
                }
            }
            if (rectSelected != null && (select == -1 || select == 0)) {
                rectSelected.setSelection(true);
                selectedShape = rectSelected;
            } else if (triSelected != null && (select == -1 || select == 1)) {
                triSelected.setSelection(true);
                selectedShape = triSelected;
            } else if (imgSel != null && (select == -1 || select == 3)) {
                imgSel.setSelection(true);
                selectedShape = imgSel;
            } else if (txtSel != null && (select == -1 || select == 4)) {
                txtSel.setSelection(true);
                selectedShape = txtSel;
            } else if (lineToolSel != null) {
                lineToolSel.setSelection(true);
                selectedShape = lineToolSel;
            }else if (penTool != null && (select == 5|| select == -1)){
                penTool.setSelection(true);
                selectedShape = penTool;
            }
        }
        if(event.getPointerCount() == 2){ // zoom
            float x1 = event.getX(0),  x2 = event.getX(1);
            float y1 = event.getY(0),  y2 = event.getY(1);
            float dx = Math.abs(x2-x1),dy = Math.abs(y2-y1);

            if (preX < dx && preY < dy ) {
                for (Shape shape : shapes) {
                    if(shape instanceof RectangleTool) {
                        ((RectangleTool) shape).zoom(1, this.getWidth(), this.getHeight());
                    }else if(shape instanceof TriangleTool){
                        ((TriangleTool) shape).zoom(1, this.getWidth(), this.getHeight());
                    }/*else if(shape instanceof  LineTool){
                        ((LineTool) shape).zoom(1, this.getWidth(), this.getHeight());
                    }*/
                }
            } else if (preX > dx && preY > dy ){
                for (Shape shape : shapes) {
                    if(shape instanceof RectangleTool) {
                        ((RectangleTool) shape).zoom(-1, this.getWidth(), this.getHeight());
                    }else if(shape instanceof TriangleTool){
                        ((TriangleTool) shape).zoom(-1, this.getWidth(), this.getHeight());
                    }
                }
            }

            if( Math.abs(preX-dx)<5 && preY - dy != 0){ //zoom along y axis for rectangle
                for (Shape shape : shapes) {
                    if(shape instanceof RectangleTool) {
                        ((RectangleTool) shape).zoomY((dy-preY)>0?1:-1, this.getWidth(), this.getHeight());
                    }
                }
            }
            //zoom along x axis for rectangle
            if(Math.abs(preY-dy)< 5 && preX - dx != 0){
                for (Shape shape : shapes) {
                    if(shape instanceof RectangleTool) {
                        ((RectangleTool) shape).zoomX((dx-preX)>0?1:-1, this.getWidth(), this.getHeight());
                    }
                }
            }
            if(preX != -1 && preY != -1) { // rotation and zoom of lines
                for (Shape shape : shapes) {
                    if (shape instanceof LineTool) {
                        ((LineTool) shape).rotate((int) x1, (int) y1 - 150, (int) x2, (int) y2 - 150);
                    }
                }
            }

//            invalidate();
            preX = dx;
            preY = dy;

        } else if(event.getAction() == MotionEvent.ACTION_MOVE ){
            for (Shape shape : shapes) {
                if(shape instanceof RectangleTool) {
                    ((RectangleTool) shape).move(x, y, this.getWidth(), this.getHeight());
                }else if(shape instanceof TriangleTool) {
                    ((TriangleTool) shape).move(x, y, this.getWidth(), this.getHeight());
                }else if(shape instanceof LineTool) {
                    ((LineTool) shape).move(x, y, this.getWidth(), this.getHeight());
                } else if(shape instanceof ImageTool) {
                    ((ImageTool) shape).move(x, y, this.getWidth(), this.getHeight());
                }else if(shape instanceof TextTool) {
                    ((TextTool) shape).move(x, y, this.getWidth(), this.getHeight());
                }else if (shape instanceof PencilTool){
                    PencilTool pTool = (PencilTool)shape;
                    if (pTool.drawEnabled()){
                        if (selectedShape instanceof RectangleTool){
                            RectangleTool tool = (RectangleTool) selectedShape;
                            tool.setSelection(false);
                        }else if (selectedShape instanceof TriangleTool){
                            TriangleTool tool = (TriangleTool) selectedShape;
                            tool.setSelection(false);
                        }else if (selectedShape instanceof TextTool){
                            TextTool tool = (TextTool) selectedShape;
                            tool.setSelection(false);
                        }
                        pTool.add(event.getX(),event.getY());
                    }else if (pTool.isSelected()){
                        float dx = event.getX()-downX, dy = event.getY()-downY;
                        pTool.move((int) dx, (int) dy, this.getWidth(), this.getHeight());
                    }
                }
            }
        }

        if(event.getActionMasked() == MotionEvent.ACTION_UP){
            for (Shape shape : shapes) {
                if (shape instanceof PencilTool){
                    PencilTool pTool = (PencilTool)shape;
                    if (pTool.drawEnabled()){
                        pTool.enableDraw(false);
                        pTool.modify();
                    }
                }
            }
            preX = -1;
            preY = -1;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Shape shape: shapes) {
            if(shape instanceof RectangleTool)
                shape.draw(canvas,((RectangleTool)shape).getPaint());
            else if (shape instanceof TriangleTool)
                shape.draw(canvas,((TriangleTool)shape).getPaint());
            else if (shape instanceof LineTool)
                shape.draw(canvas,((LineTool)shape).getPaint());
            else if (shape instanceof ImageTool)
                shape.draw(canvas,((ImageTool)shape).getPaint());
            else if (shape instanceof TextTool) {
                shape.draw(canvas, ((TextTool) shape).getPaint());
            }
            else if (shape instanceof OvalTool){
                shape.draw(canvas,((OvalTool)shape).getPaint());
            }else if (shape instanceof PencilTool){
                shape.draw(canvas,((PencilTool)shape).getPaint());
            }
        }
        invalidate();
    }
    private Paint p;
    public Paint getPaint(){
        if(p!=null) {
            p = new Paint();
        }
        return p;
    }
    public void addShape(Shape shape){
        shapes.add(shape);
    }

    public void delete(){
        shapes.remove(getSelected());
        selectedShape = null;
    }
    public Shape getSelected(){
        return this.selectedShape;
    }
    public void removeAll(){
        shapes.clear();
    }
    public boolean save(String path,String fileName){
        if(selectedShape instanceof RectangleTool)
            ((RectangleTool)selectedShape).setSelection(false);
        else if (selectedShape instanceof TriangleTool)
            ((TriangleTool)selectedShape).setSelection(false);
        else if (selectedShape instanceof LineTool)
            ((LineTool)selectedShape).setSelection(false);
        else if (selectedShape instanceof ImageTool)
            ((ImageTool)selectedShape).setSelection(false);
        else if (selectedShape instanceof TextTool)
            ((TextTool)selectedShape).setSelection(false);
        else if (selectedShape instanceof OvalTool){
            ((OvalTool)selectedShape).setSelection(false);
        }else if (selectedShape instanceof PencilTool){
            ((PencilTool)selectedShape).setSelection(false);
        }

        Bitmap  bitmap = Bitmap.createBitmap( this.getWidth(),this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        FileOutputStream fos = null;

        try {
            File file = new File(path);
            if(!file.exists()){
                file.mkdir();
            }

            fos = new FileOutputStream(path+"/"+fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();
            fos = null;
            try {
                Toast.makeText(getContext(), getContext().getString(R.string.image_saved), Toast.LENGTH_SHORT).show();
            }catch (Exception ignored){}
            return true;
        } catch (Exception ignored){
            try {
            Toast.makeText(getContext(), getContext().getString(R.string.image_not_save), Toast.LENGTH_SHORT).show();
            }catch (Exception ignored2){}
        } finally {
            if (fos != null)
            {
                try {
                    fos.close();
                    fos = null;
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }
    public List<Shape> getShapes(){return shapes;}
    public void rotate(){
        if(selectedShape instanceof RectangleTool){
            RectangleTool tool = (RectangleTool) selectedShape;
            tool.rotate();
        }else if(selectedShape instanceof ImageTool){
            ImageTool tool = (ImageTool) selectedShape;
            tool.rotate();
        }else if (selectedShape instanceof TextTool){
            TextTool tool = (TextTool) selectedShape;
            tool.rotate();
        }else if (selectedShape instanceof TriangleTool){
            TriangleTool tool = (TriangleTool) selectedShape;
            tool.rotate();
        }
    }
}
