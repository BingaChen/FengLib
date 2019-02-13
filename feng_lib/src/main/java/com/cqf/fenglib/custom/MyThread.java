package com.cqf.fenglib.custom;

/**
 * Created by Binga on 8/24/2018.
 */

public class MyThread extends Thread {
    public boolean runTag=true;

    public MyThread(Runnable target) {
        super(target);
    }
}
