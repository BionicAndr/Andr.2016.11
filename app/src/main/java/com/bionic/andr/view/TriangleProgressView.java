package com.bionic.andr.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**  */
public class TriangleProgressView extends View {

    private static final long ANIMATION_DURATION = 2000;

    private static final int FPS = 60;

    private static final long INVALIDATION_DELAY = 1000 / FPS;

    private static final int MSG_INVALIDATION = 1;

    private static final float MIN_SCALE = 0.5f, MAX_SCALE = 1f;

    private Paint p;
    private Path path;

    private boolean isAnimationEnabled;

    private long start = 0;

    private Handler invalidator = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            invalidate();
            sendEmptyMessageDelayed(MSG_INVALIDATION, INVALIDATION_DELAY);
        }
    };

    public TriangleProgressView(final Context context) {
        super(context);
        init();
    }

    public TriangleProgressView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriangleProgressView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TriangleProgressView(final Context context, final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(4);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setAnimationEnabled(true);
        invalidator.sendEmptyMessageDelayed(MSG_INVALIDATION, INVALIDATION_DELAY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        invalidator.removeMessages(MSG_INVALIDATION);
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        isAnimationEnabled = animationEnabled;
        start = System.currentTimeMillis();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        w = w - left - right;
        h = h - top - bottom;

        path = new Path();
        path.moveTo(left + w / 2, top + 0);
        path.lineTo(left + w, top + h);
        path.lineTo(left + 0, top + h);
        path.lineTo(left + w / 2, top + 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long now = System.currentTimeMillis();
        long diffRotation = (now - start) % ANIMATION_DURATION;
        long diffScale = (now - start) % (ANIMATION_DURATION / 2);

        float rotation = 360f * (float) diffRotation / ANIMATION_DURATION;
        float scale = (1 + MAX_SCALE - MIN_SCALE) * (float) diffRotation / ANIMATION_DURATION;

        int px = getWidth() / 2, py = getHeight() / 2;
        final int state = canvas.save();
        canvas.rotate(rotation, px, py);
        canvas.scale(scale, scale, px, py);
        canvas.drawPath(path, p);
        canvas.restoreToCount(state);
    }
}















