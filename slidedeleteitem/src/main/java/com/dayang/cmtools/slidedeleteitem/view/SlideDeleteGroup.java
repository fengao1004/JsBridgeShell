package com.dayang.cmtools.slidedeleteitem.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by 冯傲 on 2017/4/18.
 * e-mail 897840134@qq.com
 */

public class SlideDeleteGroup extends LinearLayout {
    private int SLIDETOLEFT = -12;
    private int SLIDETORIGHT = 12;
    private int SLIDETONORMAL = 13;
    private int UNFOLDING = 155;
    private int UNFOLDED = 157;
    private int UNUNFOLDING = 156;
    boolean isOpening = false;
    private int SLIDING_DIRECTION_UNKNOWN = 693;
    private int SLIDING_DIRECTION_VERTICAL = 692;
    private int SLIDING_DIRECTION_HORIZONTAL = 691;
    int scrollState;
    String TAG = "fengao";
    boolean hasInit = false;
    View childLeft;
    View childRight;
    private LayoutParams childLeftLayoutParams;
    long signCode = 0;
    static long rollingSignCode;
    private int width;
    private float initialX;
    private float initialY;
    private long startTime;
    private long endTime;
    private float last;
    private float now;
    private OpenListener openListener;
    private CloseListener closeListener;
    int direction;

    public SlideDeleteGroup(Context context) throws Exception {
        this(context, null);
    }

    public SlideDeleteGroup(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);
        signCode = getSignCode();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return scrollState == SLIDING_DIRECTION_HORIZONTAL;
    }

    private void initTouchEvent() {
        childLeft = getChildAt(0);
        childRight = getChildAt(1);
        ((TextView) childRight).setText("" + signCode);
        hasInit = true;
        childLeftLayoutParams = (LayoutParams) childLeft.getLayoutParams();
    }


    private void moveEnd(int slidingDirection) {
        int distance = -childLeftLayoutParams.leftMargin;
        if (slidingDirection == SLIDETORIGHT) {
            retraction(distance);
            return;
        }
        if (slidingDirection == SLIDETOLEFT) {
            unfold(distance);
            return;
        }
        if (0 <= distance && distance < (width / 3)) {
            retraction(distance);
        } else {
            unfold(distance);
        }
    }

    /**
     * 打开动画
     *
     * @param parameter
     * @return
     */
    private void unfold(int distance) {
        ValueAnimator animator = ValueAnimator.ofInt(distance, width);
        int i = (1000 * (width - distance)) / width;
        float a = ((float) i) / 1000.0f;
        if (a > 0) {
            animator.setDuration((long) (a * 400));
        } else {
            isOpening = true;
            if (openListener != null) {
                openListener.open();
            }
            animator.setDuration(0);
        }
        animator.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return ((-((input - 1.0f) * (input - 1.0f))) + 1);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (openListener != null) {
                    openListener.open();
                }
                isOpening = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (openListener != null) {
                    openListener.open();
                }
                isOpening = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                childLeftLayoutParams.leftMargin = -value;
                childLeft.setLayoutParams(childLeftLayoutParams);
            }
        });
        animator.start();
    }

    private void retraction(int distance) {
        rollingSignCode = signCode;
        ValueAnimator animator = ValueAnimator.ofInt(distance, 0);
        int i = (1000 * distance) / width;
        float a = ((float) i) / 1000.0f;
        if (a > 0) {
            animator.setDuration((long) (a * 400));
        } else {
            if (rollingSignCode == signCode) {
                rollingSignCode = 0;
            }
            isOpening = false;
            animator.setDuration(0);
            if (closeListener != null) {
                closeListener.close();
            }
        }
        animator.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return ((-((input - 1.0f) * (input - 1.0f))) + 1);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOpening = false;
                if (rollingSignCode == signCode) {
                    rollingSignCode = 0;
                }
                if (closeListener != null) {
                    closeListener.close();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isOpening = false;
                if (rollingSignCode == signCode) {
                    rollingSignCode = 0;
                }
                if (closeListener != null) {
                    closeListener.close();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                childLeftLayoutParams.leftMargin = -value;
                childLeft.setLayoutParams(childLeftLayoutParams);
            }
        });
        animator.start();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean b = super.dispatchTouchEvent(ev);
        if (rollingSignCode != 0 && rollingSignCode != signCode) {
            return b;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                rollingSignCode = signCode;
                startTime = new Date().getTime();
                initialX = ev.getRawX();
                initialY = ev.getRawY();
                scrollState = SLIDING_DIRECTION_UNKNOWN;
                last = ev.getRawX();
                direction = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                float rawX = ev.getRawX();
                float rawY = ev.getRawY();
                float differenceX = initialX - rawX;
                float differenceY = initialY - rawY;
                if ((Math.abs(differenceX) > Math.abs(differenceY)) && scrollState == SLIDING_DIRECTION_UNKNOWN && (differenceY * differenceY + differenceX * differenceX) > 50 * 50) {
                    scrollState = SLIDING_DIRECTION_HORIZONTAL;
                } else if ((Math.abs(differenceX) < Math.abs(differenceY)) && scrollState == SLIDING_DIRECTION_UNKNOWN && (differenceY * differenceY + differenceX * differenceX) > 50 * 50) {
                    scrollState = SLIDING_DIRECTION_VERTICAL;
                }
                if (scrollState == SLIDING_DIRECTION_HORIZONTAL) {
                    now = ev.getRawX();
                    float moveDistance = now - last;
                    float v1 = childLeftLayoutParams.leftMargin + moveDistance;
                    if (v1 < 0 && v1 > (-width)) {
                        childLeftLayoutParams.leftMargin = (int) (v1);
                        childLeft.setLayoutParams(childLeftLayoutParams);
                    }
                    direction = (int) (now - last);
                    last = now;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (scrollState == SLIDING_DIRECTION_HORIZONTAL) {
                    endTime = new Date().getTime();
                    if ((endTime - startTime) < 300 && direction > 10) {
                        moveEnd(SLIDETORIGHT);
                    } else if ((endTime - startTime) < 300 && direction < -10) {
                        moveEnd(SLIDETOLEFT);
                    } else {
                        moveEnd(SLIDETONORMAL);
                    }
                } else {
                    if (!isOpening)
                        rollingSignCode = 0;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (scrollState == SLIDING_DIRECTION_HORIZONTAL) {
                    endTime = new Date().getTime();
                    if ((endTime - startTime) < 300 && direction > 10) {
                        moveEnd(SLIDETORIGHT);
                    } else if ((endTime - startTime) < 300 && direction < -10) {
                        moveEnd(SLIDETOLEFT);
                    } else {
                        moveEnd(SLIDETONORMAL);
                    }
                } else {
                    if (!isOpening)
                        rollingSignCode = 0;
                }
                break;
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!hasInit)
            initTouchEvent();
        int leftMargin = childLeftLayoutParams.leftMargin;
        childLeft.setMinimumWidth(r - l);
        childLeft.layout(l + leftMargin, t, r + leftMargin, b);
        width = childRight.getMeasuredWidth();
        childRight.layout(r + leftMargin, t, r + width + leftMargin, b);
    }

    public long getSignCode() {
        long time = new Date().getTime();
        long l = time % 1000000;
        signCode = (long) (Math.random() * 1000000L + l);
        return signCode;
    }

    public void setOpenListener(OpenListener openListener) {
        this.openListener = openListener;
    }

    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public void close(boolean hasAnimation) {
        if (hasAnimation) {
            retraction(-childLeftLayoutParams.leftMargin);
        } else {
            if (rollingSignCode == signCode) {
                rollingSignCode = 0;
            }
            isOpening = false;
            if (childLeftLayoutParams != null) {
                childLeftLayoutParams.leftMargin = 0;
                childLeft.setLayoutParams(childLeftLayoutParams);
            }
        }
    }

    public void open(boolean hasAnimation) {
        if (hasAnimation) {
            if (rollingSignCode == 0) {
                isOpening = true;
                rollingSignCode = signCode;
                unfold(-childLeftLayoutParams.leftMargin);
            }
        } else {
            if (rollingSignCode == 0) {
                isOpening = true;
                rollingSignCode = signCode;
                childLeftLayoutParams.leftMargin = width;
                childLeft.setLayoutParams(childLeftLayoutParams);
            }
        }
    }

    public interface OpenListener {
        void open();
    }

    public interface CloseListener {
        void close();
    }

}
