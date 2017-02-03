package com.zilideus.jukebox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class VisualizerView extends View {

    public static final int TYPE_BAR = 0;
    public static final int TYPE_LINES = 1;
    public int typeVisualizer;
    Rect rect = new Rect();
    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Paint mForePaint = new Paint();
    private Rect[] rects = new Rect[100];

    public VisualizerView(Context context) {
        super(context);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getTypeVisualizer() {
        return typeVisualizer;
    }

    public void setTypeVisualizer(int typeVisualizer) {
        this.typeVisualizer = typeVisualizer;
    }

    private void init() {
        mBytes = null;
        mForePaint.setStrokeWidth(2f);
        mForePaint.setAntiAlias(false);
//        mForePaint.setColor(Color.rgb(0, 128, 255));
        mForePaint.setColor(getContext().getResources().getColor(R.color.colorPremiere));
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (mBytes == null) {
            return;
        }
        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }

        if (typeVisualizer == VisualizerView.TYPE_BAR) {

            mRect.set(0, 0, getWidth(), getHeight());
            int widthBetweenBuildings = 2;
            int noOfBuildings = 25;

            int widthRects = ((getWidth() - widthBetweenBuildings + 1) / noOfBuildings);

            int leftOffset = 0;
            int rightOffset = 0;

            for (int i = 0; i < noOfBuildings; i++) {
                leftOffset = (i * widthRects) + i;
                rightOffset = ((i * widthRects) + i) + widthRects;

                int alora = mRect.height() / 2
                        + ((byte) (mBytes[i * (mBytes.length / noOfBuildings)] + 128)) * (mRect.height() / 2) / 128;

                rect.set(leftOffset, 0, rightOffset, alora);


//            rects[i] = rect;
                canvas.drawRect(new Rect(rect), mForePaint);
            }
        } else if (typeVisualizer == VisualizerView.TYPE_LINES) {

            for (int i = 0; i < mBytes.length - 1; i++) {
                mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
                mPoints[i * 4 + 1] = mRect.height() / 2
                        + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
                mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
                mPoints[i * 4 + 3] = mRect.height() / 2
                        + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2)
                        / 128;
            }
            canvas.drawLines(mPoints, mForePaint);
        }
        canvas.rotate(180);
    }

    public void changeTypeEqualizer() {
        if (this.typeVisualizer == TYPE_LINES) {
            this.typeVisualizer = TYPE_BAR;
        } else {
            this.typeVisualizer++;
        }
    }

}