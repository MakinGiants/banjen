package com.makingiants.android.banjotuner;
/**
 * Created by danielgomez22 on 6/10/15.
 */
public class AnimationHelper {

    public static void setElevation(android.view.View view, float elevation) {
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofObject(view, "elevation",
                new android.animation.FloatEvaluator(), elevation);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator.setInterpolator(new android.view.animation.PathInterpolator(0.4f, 0f, 1f, 1f));
        }

        animator.setDuration(600);
        animator.start();
    }
}
