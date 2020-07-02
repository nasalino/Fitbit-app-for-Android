package com.example.assignment03;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import static java.lang.Math.min;

//class definition
public class AnalysisView extends View {


    // private fields that are necessary for rendering the view
    // the colours of our squares
    private Paint red, green, blue, white, cream, black, grey, textPaint;

    private Rect square, yIndex, xIndex;
    private int sizeF, size;
    private ArrayList<String> al_run1 = new ArrayList<String>();
    private ArrayList<String> al_run2 = new ArrayList<String>();


    private ArrayList<CustomPoints> run1  ;
    private ArrayList<CustomPoints> run2 = new ArrayList<CustomPoints>();
    float max = 0;

    // default constructor for the class that takes in a context
    public AnalysisView(Context c) {
        super(c);
        init();
    }


    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public AnalysisView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certain way
    public AnalysisView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    // refactored init method as most of this code is shared by all the constructors
    public void init() {



        //Create the paint objects for rendering our rectangles
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
        canvas.drawText(" Speed ", size / 32, size / 8, black);
        canvas.drawText("in km/h ", size / 32, size / 6, black);
        canvas.drawText("400 km/h", size / 32, size / 5, black);
        canvas.drawText("max", size / 32, size / 4, black);


        //draw the x axis
        canvas.drawLine(0, (7 * size) / 8, size, (7 * size) / 8, black);

        //draw the x axis labels
        canvas.drawText(" distance over 10km", size / 7, size - (size / 16), black);


        //if a value for distance and speed have been recorded
        if (run1.size() != 0) {
            canvas.save();
            //translate canvas to start drawing xTics
            canvas.translate(size / 16, (((7 * size) / 8) - size / 9));
            //  canvas.drawRect(xIndex, black);
            for (int i = 0; i < al_run1.size(); i++) {
                System.out.println("Hello " + run1.get(i).getKm() + ", " + run1.get(i).getSpeed());

                //Draw the values for the x axis in Kilometers
                canvas.translate((7 * size) / 8 / 10, 0);
                canvas.drawRect(xIndex, black);
            }

            for (int i = 0; i < al_run2.size(); i++) {
                System.out.println("Hello " + run2.get(i).getKm() + ", " + run2.get(i).getSpeed());

                //Draw the values for the x axis in Kilometers
                canvas.translate((7 * size) / 8 / 10, 0);
                canvas.drawRect(xIndex, black);
            }

            canvas.restore();
            //draw the yTics

            canvas.save();
            canvas.translate(0, -size / 15);

            for (int i = 0; i < run1.size(); i++) {
                canvas.translate(0, (size - (size / 8)) / max);
                for (int j = 0; j < run1.size(); j++) {
                    int num = Math.round(Float.parseFloat(run1.get(j).getSpeed()));

//print the values of speed on the line graph
                    if (i == num) {
                        textPaint.setColor(Color.BLACK);
                        textPaint.setTextSize(20);
                        if (num == Math.round(max)) {
                            canvas.drawText(num + " km/h", 0, -((size - (size / 8)) + (7 * size) / 8) / max, black);
                        }
                    }
                }
            }





            canvas.restore();

//Draw the line graph of speed against distance based on calculations from the location listener one one km point to the next
            //This will be the line graph for the first run in color red
            for (int i = 0; i < run1.size() - 1; i++) {
                canvas.drawLine(((size / 8) + (((7 * size) / 8 / 10) * (i))),
                        -(((size - (size / 8)) / max) * Float.parseFloat(run1.get(i).getSpeed())) + ((size)),
                        ((size / 8) + (((7 * size) / 8 / 10) * (i + 1))),
                        -(((size - (size / 8)) / max) * Float.parseFloat(run1.get(i + 1).getSpeed())) + ((size)), red);




//Draw y axis labels
                textPaint.setTextSize(15);
                canvas.drawText("" + Math.round(Float.parseFloat(run1.get(i).getSpeed())) + " Km/h ", ((size / 8) + (((7 * size) / 8 / 10) * (i))), (-(((size - (size / 8)) / max) * Float.parseFloat(run1.get(i).getSpeed())) + size), black);


            }

//The line graph for the second run in color blue
            for (int i = 0; i < run2.size() - 1; i++) {
                canvas.drawLine(((size / 8) + (((7 * size) / 8 / 10) * (i))),
                        -(((size - (size / 8)) / max) * Float.parseFloat(run2.get(i).getSpeed())) + ((size)),
                        ((size / 8) + (((7 * size) / 8 / 10) * (i + 1))),
                        -(((size - (size / 8)) / max) * Float.parseFloat(run2.get(i + 1).getSpeed())) + ((size)), blue);






//Draw y axis labels
                textPaint.setTextSize(15);
                canvas.drawText("" + Math.round(Float.parseFloat(run2.get(i).getSpeed())) + " Km/h ", ((size / 8) + (((7 * size) / 8 / 10) * (i))), (-(((size - (size / 8)) / max) * Float.parseFloat(run2.get(i).getSpeed())) + size), black);


            }
            for(int i=0; i<10;i++){

                //Draw x axis labels
                textPaint.setColor(Color.BLACK);
                textPaint.setTextSize(20);
                canvas.drawText(" " + (i+1), (((size / 8)+ ((7 * size) / 8 / 10))+ (((7 * size) / 8 / 10) * (i))), (7 * size) / 8, black);

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
    public void updateLineChart(ArrayList<String> runa1, ArrayList<String> runa2) {


        al_run1 = new ArrayList<String>(runa1);
        al_run2 = new ArrayList<String>(runa2);

        run1 = new ArrayList<CustomPoints>();
        run2 = new ArrayList<CustomPoints>();

        for (int i = 0; i < al_run1.size(); i++) {
            //split each element of array list into distance and speed
            String[] dets1 = al_run1.get(i).split(":");
            //construct coordinate points for plotting by creating custom point objects
            //Do this for the two runs
            run1.add(new CustomPoints(dets1[0], dets1[1]));
            System.out.println("runt1 " + run1.get(i).getKm()+" "+ run1.get(i).getSpeed());

        }

        for (int i = 0; i < al_run2.size(); i++) {
            String[] dets2 = al_run2.get(i).split(":");
            run2.add(new CustomPoints(dets2[0], dets2[1]));
            System.out.println("runt2 " + run2.get(i).getKm()+" "+ run2.get(i).getSpeed());

        }

//The maximum speed used to compare both runs is 400km/h
 max = 400.0f;
        System.out.println("maxwell" + max);
        invalidate();
    }


}









