package com.example.todungnguyen.liseuse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

public class pageView extends View implements ViewTreeObserver.OnGlobalLayoutListener {
    private int page = 0;
    private ArrayList<String> textOfPages = new ArrayList<>(); //list of pages

    TextPaint textPaint; //how to draw text
    private int height;
    private int width;
    private StringBuffer text;
    private static final int TEXT_SIZE = 50;

    public pageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(TEXT_SIZE);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        StaticLayout layout = new StaticLayout(textOfPages.get(page), textPaint, width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        layout.draw(canvas);
    }

    public void splitTextIntoPages(StringBuffer text) {
        this.text = text;
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
            height = getHeight();
            width = getWidth();

            // offset to split text
            int posStart = 0, posFin;

            //creat a layout into this view for draw multiline text
            StaticLayout layout = new StaticLayout(text, textPaint, width - getPaddingLeft() - getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

            //the number of lines of text in this layout
            int totalLines = layout.getLineCount();
            //Log.d("TEST", "totalLines: " + Integer.toString(totalLines));

            //line per page
            int linePerPage = layout.getLineForVertical(height - getPaddingTop() - getPaddingBottom());
            //Log.d("TEST", "linePerPage: " + Integer.toString(linePerPage));

            //number of page
            int i = 1;
            do {
                //last line of page
                //last line is null => - 1
                int line = Math.min(linePerPage * i, totalLines - 1);
                //Log.d("TEST", "line: " + Integer.toString(line));

                //position last character of page = posFin - 1
                posFin = layout.getOffsetForHorizontal(line, width - getPaddingLeft() - getPaddingRight());
                //Log.d("TEST", "posFin: " + Integer.toString(posFin));

                //get subString (posFin exclus)
                String sub = text.substring(posStart, posFin);
                //Log.d("TEST", sub);

                posStart = posFin;
                textOfPages.add(sub);
                i++;
            } while (posFin < text.length());
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < width / 2) {
                if (page > 0) {
                    page--;
                }
            } else if (page < textOfPages.size() - 1) {
                page++;
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}
