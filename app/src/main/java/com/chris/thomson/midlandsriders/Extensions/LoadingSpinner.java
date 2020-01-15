package com.chris.thomson.midlandsriders.Extensions;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.chris.thomson.midlandsriders.R;

public class LoadingSpinner extends android.support.v7.widget.AppCompatImageView {

    private static final int ROTATE_ANIMATION_DURATION = 800;
    private static final int IMAGE_RESOURCE_ID = R.drawable.mr_loader2;

    public LoadingSpinner(Context context) {
        super(context, null);
    }

    public LoadingSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Set and scale the image
        setImageResource(IMAGE_RESOURCE_ID);

        // Start the animation
        setBackgroundColor(Color.TRANSPARENT);
        startAnimation();
    }

    /**
     * Starts the rotate animation.
     */
    private void startAnimation() {
        clearAnimation();

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(ROTATE_ANIMATION_DURATION);
        rotate.setRepeatCount(Animation.INFINITE);
        startAnimation(rotate);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            startAnimation();
        } else {
            clearAnimation();
        }
    }
}
