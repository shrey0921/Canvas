package com.shrey.canvas;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by Shreyansh on 07-08-2018.
 */
public class PaintValues extends HorizontalScrollView {
    private int color,textColor;
    private boolean fill;
    private int strokeWidth,textSize;

    private EditText etStroke,etText;
    private TextView tvColor,tvText;
    private RadioGroup rgFill;
    private Button btnOK;

    public PaintValues(Context context) {
        super(context);
        init();
    }


    public PaintValues(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintValues(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void setOKClickListener(OnClickListener cc){
        btnOK.setOnClickListener(cc);
    }
    private void init() {
        inflate(getContext(), R.layout.paint_values, this);
        tvColor = findViewById(R.id.tv_paint_color);
        tvText = findViewById(R.id.tv_paint_text);
        etStroke = findViewById(R.id.et_paint_stroke);
        etText = findViewById(R.id.et_paint_text);
        btnOK= findViewById(R.id.b_paint_ok);
        setColor(Color.BLACK);
        setFill(true);
        strokeWidth = 5;
        tvColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(),color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        setColor(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });
                dialog.show();
            }
        });
        tvText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(),color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        setTextColor(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }
                });
                dialog.show();
            }
        });
        rgFill = findViewById(R.id.rg_fill_stroke);
        rgFill.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFill(checkedId == R.id.rb_fill);
            }
        });
        etStroke.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    strokeWidth = Integer.parseInt(s.toString());
                }catch (Exception ignore){}
            }
        });
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    textSize = Integer.parseInt(s.toString());
                }catch (Exception ignore){}
            }
        });
    }

    public void setColor(int c){
        this.color = c;
        tvColor.setBackgroundColor(color);
    }
    public void setFill(boolean f){
        this.fill = f;
        if (f){
            RadioButton radioButton = findViewById(R.id.rb_fill);
            radioButton.setChecked(true);
            etStroke.setEnabled(false);
        }else{
            RadioButton radioButton = findViewById(R.id.rb_stroke);
            radioButton.setChecked(true);
            etStroke.setEnabled(true);
            etStroke.setText(String.format(Locale.ENGLISH,"%d",strokeWidth));
        }
    }
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        tvText.setTextColor(textColor);
    }
    public void setStrokeWidth(float width){
        this.strokeWidth = (int)width;
        etStroke.setText(String.format(Locale.ENGLISH,"%d",strokeWidth));
    }
    public int getStrokeWidth() {
        return strokeWidth;
    }

    public int getColor() {
        return color;
    }

    public boolean getFill() {
        return fill;
    }

    public int getTextColor() {
        return textColor;
    }
    public void isText(int isText){
        if (isText == 0){
            tvColor.setVisibility(GONE);
            rgFill.setVisibility(GONE);
            etStroke.setVisibility(GONE);
            tvText.setVisibility(View.VISIBLE);
            etText.setVisibility(View.VISIBLE);
        }else if (isText ==1){
            tvColor.setVisibility(View.VISIBLE);
            rgFill.setVisibility(View.GONE);
            etStroke.setVisibility(VISIBLE);
            tvText.setVisibility(View.GONE);
            etText.setVisibility(View.GONE);
        }else if (isText == 2){
            tvColor.setVisibility(View.VISIBLE);
            rgFill.setVisibility(View.VISIBLE);
            etStroke.setVisibility(VISIBLE);
            tvText.setVisibility(View.VISIBLE);
            etText.setVisibility(View.VISIBLE);
        }else{
            tvColor.setVisibility(View.VISIBLE);
            rgFill.setVisibility(View.VISIBLE);
            etStroke.setVisibility(VISIBLE);
            tvText.setVisibility(View.GONE);
            etText.setVisibility(View.GONE);
        }
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = (int)textSize;
        etText.setText(String.format(Locale.ENGLISH,"%f",textSize));
    }
}
