package com.cqf.fenglib.aop;

import android.util.Log;

import com.cqf.fenglib.utils.MyUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by Binga on 2/3/2019.
 */
@Aspect
public class AspectTest {
    final String TAG = AspectTest.class.getSimpleName();

    private boolean isFastDoubleClick = false;


    @Before("execution(@com.cqf.fenglib.aop.DoubleClick  * *(..))")
    public void enableDoubleClick(JoinPoint joinPoint) throws Throwable {
        Log.e(TAG, "enableDoubleClick: ");
        isFastDoubleClick = true;
    }

    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onClickListener(ProceedingJoinPoint proceedingJoinPoint){
        Log.v(TAG, "OnClick");
        if (isFastDoubleClick||!MyUtils.isFastDoubleClick()) {
            try {
                proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            isFastDoubleClick=false;
        }else {
            Log.v(TAG,"重复点击，已过滤");
        }
    }
}
