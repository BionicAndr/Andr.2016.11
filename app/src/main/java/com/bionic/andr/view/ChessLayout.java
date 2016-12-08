package com.bionic.andr.view;

import com.bionic.andr.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**  */
public class ChessLayout extends ViewGroup {

    private static final int COLUMNS = 3;

    private int columns = COLUMNS;

    public ChessLayout(final Context context) {
        super(context);
        init(context, null);
    }

    public ChessLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChessLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChessLayout(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChessLayout);
        columns = a.getInt(R.styleable.ChessLayout_columns, COLUMNS);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);

        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);

        int width = getMeasuredWidth();
        if (width <= 0) {
            return;
        }

        int childSize = width / columns;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            child.measure(
                    MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int cl = 0, ct = 0;

        int col = 0;
        final int childSize = getMeasuredWidth() / columns;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            int row = col / columns;
            int realCol = (columns % 2 == 0 && row % 2 == 1) ? 1 + col % columns : col % columns;
            child.layout(realCol * childSize, row * childSize,
                    (realCol + 1) * childSize,
                    (row + 1) * childSize);
            col += 2;
        }
    }
}










