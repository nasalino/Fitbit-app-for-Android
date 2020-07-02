package com.example.assignment03;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import static java.lang.Math.min;

//class definition
public class CustomView extends View {


    // private fields that are necessary for rendering the view
    // the colours of our squares
    private Paint red, green, blue, white, cream, black, grey, textPaint;

    private Rect square, yIndex, xIndex;
    private int sizeF, size;

    private ArrayList<Integer> xTics = new ArrayList<Integer>();

    private ArrayList<Float> yTics = new ArrayList<Float>();
    float max = 0;

    // default constructor for the class that takes in a context
    public CustomView(Context c) {
        super(c);
        init();
    }


    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certain way
    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    // refactored init method as most of this code is shared by all the constructors
    public void init() {


// create the paint objects for rendering our rectangles
        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        green = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);
        cream = new Paint(Paint.ANTI_ALIAS_FLAG);
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        grey = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        red.setColor(0xFFFF0000);
        green.setColor(0xFF00FF00);
        blue.setColor(0xFF0000FF);
        black.setColor(0xFF000000);
        cream.setColor(0xFFFFFDD0);
        white.setColor(0xFFFFFFFF);
        grey.setColor(0xFFA9A9A9);

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        size = min(w, h);


// initialise the rectangle
        square = new Rect(-size, -size, size, size);


        //primitive shapes to draw ticks on the view
        yIndex = new Rect(size / 9, size / 16, size / 7, size / 15);
        xIndex = new Rect(size / 16, size / 9, size / 15, size / 7);

        invalidate();

    }

    // public method that needs to be overridden to draw the contents of this widget
    public void onDraw(Canvas canvas) {
// call the superclass method
        super.onDraw(canvas);
        canvas.drawRect(square, grey);
        // canvas.drawRect(yIndex, black);


        //draw the  y axis
        canvas.drawLine(size / 8, size, size / 8, 0, black);

       //draw the y axis labels
        canvas.drawText(" Speed ", size/32, size/8, black);
        canvas.drawText("in km/h ", size/32, size/6, black);



        //draw the x axis
        canvas.drawLine(0, (7 * size) / 8, size, (7 * size) / 8, black);

        //draw the x axis labels
        canvas.drawText(" distance in Km", size/7, size-(size/16), black);



        //if a value for distance and speed have been recorded
        if (xTics.size() != 0) {
            canvas.save();
            //translate canvas to start drawing xTics
            canvas.translate(size / 16, (((7 * size) / 8) - size / 9));
            //  canvas.drawRect(xIndex, black);
            for (int i = 0; i < xTics.size(); i++) {
                System.out.println("Hello " + xTics.get(i) + ", " + yTics.get(i));

                //Draw the values for the x axis in Kilometers
                canvas.translate((7 * size) / 8 / xTics.size(), 0);
                canvas.drawRect(xIndex, black);
            }

            canvas.restore();
            //draw the yTics

            canvas.save();
            canvas.translate(0, -size / 15);


            canvas.restore();

//Draw the line graph of speed against distance based on calculations from the location listener one one km point to the next
            for (int i = 0; i < xTics.size() - 1; i++) {
                canvas.drawLine(((size / 8) + (((7 * size) / 8 / xTics.size()) * (i))),
                        -(((size - (size / 8)) / max) * yTics.get(i)) + ((size)),
                        ((size / 8) + (((7 * size) / 8 / xTics.size()) * (i + 1))),
                        -(((size - (size / 8)) / max) * yTics.get(i + 1)) + (( size)), black);

                //Draw x axis labels
                textPaint.setColor(Color.BLACK);
                textPaint.setTextSize(20);
                canvas.drawText(" " + xTics.get(i), ((size / 8) + (((7 * size) / 8 / xTics.size()) * (i))), (7 * size) / 8, black);

//Draw y axis labels
                textPaint.setTextSize(15);
                canvas.drawText("" + Math.round(yTics.get(i)) + " Km/h ", ((size / 8) + (((7 * size) / 8 / xTics.size()) * (i))), (-(((size - (size / 8)) / max) * yTics.get(i)) + size), black);


            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeF = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            sizeF = height;
        } else {
            sizeF = width;
        }
        setMeasuredDimension(sizeF, sizeF);
    }

    //draw the line chart after the user stops tracking
    public void updateLineChart(ArrayList<Integer> xVal, ArrayList<Float> yVal) {


        xTics = new ArrayList<Integer>(xVal);
        yTics = new ArrayList<Float>(yVal);

        max = yTics.get(0);
        for (int i = 0; i < yTics.size(); i++) {
            if (yTics.get(i) > max) {
                max = yTics.get(i);
            }
        }
        invalidate();
    }

    //draw the line chart live when called
    public void updateLineChartLive(int xVal, float yVal) {
        xTics.add(xVal);
        yTics.add(yVal);

        max = yTics.get(0);
        for (int i = 0; i < yTics.size(); i++) {
            if (yTics.get(i) > max) {
                max = yTics.get(i);
            }
        }
        invalidate();

    }
}









