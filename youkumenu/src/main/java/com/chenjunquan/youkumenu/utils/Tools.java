package com.chenjunquan.youkumenu.utils;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

/**
 * Created by Administrator on 2017/11/11.
 */

public class Tools {
    //属性动画(解决控件属性没有真实移动的问题)
    public static void hideViewbyObjectAnimator(View view, int startOffset) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, 180);
        objectAnimator.setDuration(500);
        objectAnimator.setStartDelay(startOffset);
        objectAnimator.start();

        //view.setRotationX();
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight());
    }

    //属性动画
    public static void showViewbyObjectAnimator(View view, int startOffset) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 180, 360);
        objectAnimator.setDuration(500);
        objectAnimator.setStartDelay(startOffset);
        objectAnimator.start();

        //view.setRotationX();
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight());
    }
    //属性动画
    public static void showViewbyObjectAnimator(View view) {
        showViewbyObjectAnimator(view,0);
    }
    public static void hideViewbyObjectAnimator(View view) {
        hideViewbyObjectAnimator(view,0);
    }
    //以下为补间动画模式(存在问题)
    public static void hideView(ViewGroup viewGroup) {
        //代码复用
        hideView(viewGroup, 0);
    }

    public static void showView(ViewGroup viewGroup) {
        showView(viewGroup, 0);
    }

    //延迟隐藏

    public static void hideView(ViewGroup viewGroup, int startOffset) {
        //默认是相对于自己旋转的!!!!
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, viewGroup.getWidth() / 2, viewGroup.getHeight());
        //设置动画停留在播放完成的状态
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(500);
        //延迟执行动画
        rotateAnimation.setStartOffset(startOffset);
        //startAnimation和set不一样
        viewGroup.startAnimation(rotateAnimation);
        //屏蔽孩子节点的点击事件(解决问题)
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setEnabled(false);
        }

    }

    public static void showView(ViewGroup viewGroup, int startOffset) {
        //默认是相对于自己旋转的!!!!
        RotateAnimation rotateAnimation = new RotateAnimation(180, 360, viewGroup.getWidth() / 2, viewGroup.getHeight());
        //设置动画停留在播放完成的状态
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(500);
        rotateAnimation.setStartOffset(startOffset);
        viewGroup.startAnimation(rotateAnimation);
        //释放孩子节点的点击事件
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setEnabled(true);
        }
    }
}
