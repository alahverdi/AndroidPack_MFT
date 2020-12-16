package ir.allahverdi.digiapplication.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.R;

public class SplashView extends View {

    Paint paint = new Paint();
    Map<Rect, Integer> map = new HashMap<>();

    int background = getResources().getColor(R.color.colorAccent);

    public float cx, cy, width, height;
    public int getWidth, getHeight, canvasWidth, canvasHeight;

    int logoLeft = 0, logoRight = 0, logoTop = 2000, logoBottom = 2700;

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.digikala_splash_new_logo);

    public SplashView(Context context) {
        super(context);

        init();

    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();

    }

    private void init() {
        this.post(new Runnable() {
            @Override
            public void run() {

                width = getMeasuredWidth();
                height = getMeasuredHeight();

                getWidth = (int) width;
                getHeight = (int) height;

                cx = width / 2;
                cy = height / 2;

                invalidate();
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawPaint(paint);

        //paint.setColor(Color.GREEN);
        canvasWidth = getWidth();
        canvasHeight = getHeight();
        Rect rect = new Rect(logoLeft + 200, logoTop , canvasWidth - 100, logoBottom);
        //Rect rect = new Rect(100 , 100 , getHeight , getWidth);
        map.put(rect, background);
        canvas.drawRect(rect, paint);
        canvas.drawBitmap(bitmap, new Rect(0, 0, getWidth() + 2000, getHeight() + 700), rect, null);

        //canvas.drawCircle(cx , cy , 100 , paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        Log.e(Const.SPLASH_VIEW, "onTouchEvent ");

        return super.onTouchEvent(event);
    }

}
