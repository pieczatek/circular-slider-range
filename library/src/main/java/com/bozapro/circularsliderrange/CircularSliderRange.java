
package com.bozapro.circularsliderrange;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

public class CircularSliderRange extends View {

    /**
     * Listener interface used to detect when slider moves around.
     */
    public static interface OnSliderMovedListener {

        /**
         * This method is invoked when slider moves, providing position of the slider thumb.
         *
         * @param pos Value between 0 and 1 representing the current angle.<br>
         *            {@code pos = (Angle - StartingAngle) / (2 * Pi)}
         */
        public void onSliderMoved(double pos);
    }

    private int mThumbStartX;
    private int mThumbStartY;

    private int mThumbEndX;
    private int mThumbEndY;

    private int mCircleCenterX;
    private int mCircleCenterY;
    private int mCircleRadius;

    private Drawable mThumbImage;
    private int mPadding;
    private int mThumbSize;
    private int mThumbColor;
    private int mBorderColor;
    private int mBorderThickness;
    private double mStartAngle;
    private double mAngle = mStartAngle;
    private double mAngleEnd;
    private boolean mIsThumbSelected = false;
    private boolean mIsThumbEndSelected = false;

    private Paint mPaint = new Paint();
    private Paint mLinePaint = new Paint();
    private RectF arcRectF = new RectF();
    private Rect arcRect = new Rect();
    private OnSliderMovedListener mListener;

    public CircularSliderRange(Context context) {
        this(context, null);
    }

    public CircularSliderRange(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularSliderRange(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularSliderRange(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    // common initializer method
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularSlider, defStyleAttr, 0);

        // read all available attributes
        float startAngle = a.getFloat(R.styleable.CircularSlider_start_angle, (float) Math.PI / 2);
        float angle = a.getFloat(R.styleable.CircularSlider_angle, (float) Math.PI / 2);
        int thumbSize = a.getDimensionPixelSize(R.styleable.CircularSlider_thumb_size, 50);
        int thumbColor = a.getColor(R.styleable.CircularSlider_thumb_color, Color.GRAY);
        int borderThickness = a.getDimensionPixelSize(R.styleable.CircularSlider_border_thickness, 20);
        int borderColor = a.getColor(R.styleable.CircularSlider_border_color, Color.RED);
        Drawable thumbImage = a.getDrawable(R.styleable.CircularSlider_thumb_image);

        // save those to fields (really, do we need setters here..?)
        setStartAngle(startAngle);
        setAngle(angle);
        setBorderThickness(borderThickness);
        setBorderColor(borderColor);
        setThumbSize(thumbSize);
        setThumbImage(thumbImage);
        setThumbColor(thumbColor);

        // assign padding - check for version because of RTL layout compatibility
        int padding;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int all = getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop() + getPaddingEnd() + getPaddingStart();
            padding = all / 6;
        } else {
            padding = (getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop()) / 4;
        }
        setPadding(padding);
        a.recycle();

        if (isInEditMode())
            return;
    }

    /* ***** Setters ***** */
    public void setStartAngle(double startAngle) {
        mStartAngle = startAngle;
    }

    public void setAngle(double angle) {
        mAngle = angle;
    }

    public void setThumbSize(int thumbSize) {
        mThumbSize = thumbSize;
    }

    public void setBorderThickness(int circleBorderThickness) {
        mBorderThickness = circleBorderThickness;
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }

    public void setThumbImage(Drawable drawable) {
        mThumbImage = drawable;
    }

    public void setThumbColor(int color) {
        mThumbColor = color;
    }

    public void setPadding(int padding) {
        mPadding = padding;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // use smaller dimension for calculations (depends on parent size)
        int smallerDim = w > h ? h : w;

        // find circle's rectangle points
        int largestCenteredSquareLeft = (w - smallerDim) / 2;
        int largestCenteredSquareTop = (h - smallerDim) / 2;
        int largestCenteredSquareRight = largestCenteredSquareLeft + smallerDim;
        int largestCenteredSquareBottom = largestCenteredSquareTop + smallerDim;

        // save circle coordinates and radius in fields
        mCircleCenterX = largestCenteredSquareRight / 2 + (w - largestCenteredSquareRight) / 2;
        mCircleCenterY = largestCenteredSquareBottom / 2 + (h - largestCenteredSquareBottom) / 2;
        mCircleRadius = smallerDim / 2 - mBorderThickness / 2 - mPadding;

        // works well for now, should we call something else here?
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // outer circle (ring)
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderThickness);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mPaint);

        // find thumb start position
        mThumbStartX = (int) (mCircleCenterX + mCircleRadius * Math.cos(mAngle));
        mThumbStartY = (int) (mCircleCenterY - mCircleRadius * Math.sin(mAngle));

        //find thumb end position
        mThumbEndX = (int) (mCircleCenterX + mCircleRadius * Math.cos(mAngleEnd));
        mThumbEndY = (int) (mCircleCenterY - mCircleRadius * Math.sin(mAngleEnd));

        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(80);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setTextSize(50);
        //helper lines
//        canvas.drawLine(mCircleCenterX, mCircleCenterY, mThumbStartX, mThumbStartY, mLinePaint);
//        canvas.drawLine(mCircleCenterX, mCircleCenterY, mThumbEndX, mThumbEndY, mLinePaint);
//        mLinePaint.setStrokeWidth(9);
//        canvas.drawLine(mCircleCenterX, mCircleCenterY, mCircleCenterX + mCircleRadius, mCircleCenterY, mLinePaint);

        //mLinePaint.setColor(Color.BLACK);
        arcRect.set(mCircleCenterX - mCircleRadius, mCircleCenterY + mCircleRadius, mCircleCenterX + mCircleRadius, mCircleCenterY - mCircleRadius);
        arcRectF.set(arcRect);
        arcRectF.sort();

        canvas.drawArc(arcRectF, toDrawingAngle(mAngle), toDrawingAngle(mAngleEnd), false, mLinePaint);

        if (mThumbImage != null) {
            // draw png
            mThumbImage.setBounds(mThumbStartX - mThumbSize / 2, mThumbStartY - mThumbSize / 2, mThumbStartX + mThumbSize / 2, mThumbStartY + mThumbSize / 2);
            mThumbImage.draw(canvas);
        } else {
            // draw colored circle
            mPaint.setColor(mThumbColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mThumbStartX, mThumbStartY, mThumbSize, mPaint);
            mPaint.setColor(Color.YELLOW);
            canvas.drawCircle(mThumbEndX, mThumbEndY, mThumbSize, mPaint);

            mLinePaint.setStrokeWidth(5);
            canvas.drawText(String.format(Locale.US, "%.1f", Math.toDegrees(mAngle)), mThumbStartX - 20, mThumbStartY, mLinePaint);
            canvas.drawText(String.format(Locale.US, "%.1f", Math.toDegrees(mAngleEnd)), mThumbEndX - 20, mThumbEndY, mLinePaint);
        }
    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    private void updateSliderState(int touchX, int touchY) {
//        Timber.d("Touch x: %d, y: %d", touchX, touchY);
        int distanceX = touchX - mCircleCenterX;
        int distanceY = mCircleCenterY - touchY;
        //noinspection SuspiciousNameCombination
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        mAngle = Math.acos(distanceX / c);
        if (distanceY < 0) {
            mAngle = -mAngle;
        }

//        Timber.d("Calculated start angle radians: %.2f, degrees: %.2f", mAngle, toDrawingAngle(mAngle));

        if (mListener != null) {
            // notify slider moved listener of the new position which should be in [0..1] range
            mListener.onSliderMoved((mAngle - mStartAngle) / (2 * Math.PI));
        }
    }

//    private float toDrawingAngleStart(double angleInRadians) {
////        if (angleInRadians < 0)
////            angleInRadians = -angleInRadians;
//        double fixedAngle = Math.toDegrees(angleInRadians);
//        if (fixedAngle < 0)
//            fixedAngle = 360 + fixedAngle;
//        //fixedAngle = 360 - fixedAngle;
//        return (float) fixedAngle;
//    }

    private float toDrawingAngle(double angleInRadians) {
        double fixedAngle = Math.toDegrees(angleInRadians);
        if (angleInRadians < 0)
            fixedAngle = 360 + fixedAngle;
        fixedAngle = 360 - fixedAngle;
        return (float) fixedAngle;
    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    private void updateSliderStateEnd(int touchX, int touchY) {
//        Timber.d("Touch x: %d, y: %d", touchX, touchY);
        int distanceX = touchX - mCircleCenterX;
        int distanceY = mCircleCenterY - touchY;
        //noinspection SuspiciousNameCombination
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        mAngleEnd = Math.acos(distanceX / c);
        if (distanceY < 0) {
            mAngleEnd = -mAngleEnd;
        }

//        Timber.d("Calculated angle radians: %.2f, degrees: %.2f", mAngle, Math.toDegrees(mAngle));

        if (mListener != null) {
            // notify slider moved listener of the new position which should be in [0..1] range
            mListener.onSliderMoved((mAngleEnd - mStartAngle) / (2 * Math.PI));
        }
    }

    /**
     * Position setter. This method should be used to manually position the slider thumb.<br>
     * Note that counterclockwise {@link #mStartAngle} is used to determine the initial thumb position.
     *
     * @param pos Value between 0 and 1 used to calculate the angle. {@code Angle = StartingAngle + pos * 2 * Pi}<br>
     *            Note that angle will not be updated if the position parameter is not in the valid range [0..1]
     */
    public void setPosition(double pos) {
        if (pos >= 0 && pos <= 1) {
            mAngle = mStartAngle + pos * 2 * Math.PI;
        }
    }

    /**
     * Saves a new slider moved listner. Set {@link OnSliderMovedListener} to {@code null} to remove it.
     *
     * @param listener Instance of the slider moved listener, or null when removing it
     */
    public void setOnSliderMovedListener(OnSliderMovedListener listener) {
        mListener = listener;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // start moving the thumb (this is the first touch)
                int x = (int) ev.getX();
                int y = (int) ev.getY();

                boolean isThumbStartPressed = x < mThumbStartX + mThumbSize
                        && x > mThumbStartX - mThumbSize
                        && y < mThumbStartY + mThumbSize
                        && y > mThumbStartY - mThumbSize;

                boolean isThumbEndPressed = x < mThumbEndX + mThumbSize
                        && x > mThumbEndX - mThumbSize
                        && y < mThumbEndY + mThumbSize
                        && y > mThumbEndY - mThumbSize;

                if (isThumbStartPressed) {
                    mIsThumbSelected = true;
                    updateSliderState(x, y);
                } else if (isThumbEndPressed) {
                    mIsThumbEndSelected = true;
                    updateSliderStateEnd(x, y);
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // still moving the thumb (this is not the first touch)
                if (mIsThumbSelected) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    updateSliderState(x, y);
                } else if (mIsThumbEndSelected) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    updateSliderStateEnd(x, y);
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                // finished moving (this is the last touch)
                mIsThumbSelected = false;
                mIsThumbEndSelected = false;
                break;
            }
        }

        // redraw the whole component
        invalidate();
        return true;
    }

}
