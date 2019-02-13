package com.cqf.fenglib.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cqf.fenglib.custom.MyThread;

import java.text.SimpleDateFormat;

/**
 * Created by Binga on 8/24/2018.
 */

@SuppressLint("AppCompatCustomView")
public class CountdownTextView extends TextView {
    MyThread t;
    public SimpleDateFormat format;
    CountdownCallback callback;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (msg.arg1==0){
                        callback.complete();
                        return;
                    }
                    if (format!=null){
                        setText(format.format(msg.arg1*1000)+"");
                    }else {
                        setText(msg.arg1+"");
                    }
                    break;
            }
        }
    };
    public CountdownTextView(Context context) {
        super(context);
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void countDown(final int countdownNum,CountdownCallback callback){
        this.callback=callback;
        t=new MyThread(new Runnable() {
            @Override
            public void run() {
                for (int i = countdownNum; i >=0 ; i--) {
                    if (t.runTag){
                        Message message=Message.obtain();
                        message.what=1;
                        message.arg1=i;
                        handler.sendMessage(message);
                        SystemClock.sleep(1000);
                    }
                }
            }
        });
        t.start();
    }
    public interface CountdownCallback{
        void complete();
    }
}
