package com.test.addapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/2/15.
 */
public class UpdateService extends Service  {

    public static boolean isDestroyed;


    @Bind(R.id.tvNumber_DialogCaller)
    TextView tvNumber;





    View mView;
    private boolean isRequesting;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initUi();
        initExecutor();
        Log.v("UPDATE_SERVICE", "started");
        showDialog();
        isDestroyed = false;
        return 0;
    }

    private void initExecutor(){

        final Handler handler = new Handler(Looper.getMainLooper());

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showDialog();
                    }
                });



            }
        },0,5, TimeUnit.SECONDS);
    }


    @Override
    public void onDestroy() {


        if (mView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            isDestroyed = true;
        }

        super.onDestroy();
    }


    private void initUi() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        params.alpha = 1f;


        mView = inflater.inflate(R.layout.dialog_caller, null);
        ButterKnife.bind(this, mView);
        mView.setVisibility(View.INVISIBLE);
        windowManager.addView(mView, params);

    }




    @OnClick(R.id.ivCancel_DialogCaller)
    public void onCancelClick() {

       // callLater(String.valueOf(mCurrentCaller.getId()));
        hideDialog();

    }



    @OnClick(R.id.tvCall_DialogCaller)
    public void onCallPressed() {
        hideDialog();
        String url = "http://www.youtube.com";
        showWebsite(url);

    }

    public void showWebsite(String website){


        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        i.setData(Uri.parse(website));
        startActivity(i);
    }


    private void showDialog() {
//        tvNumber.setText(mCurrentCaller.getNumber());
        Log.v("UPDATE_SERVICE", "dialog shown");
        if(mView.getVisibility() != View.VISIBLE)
        mView.setVisibility(View.VISIBLE);
        else Log.v("UPDATE_SERVICE", "ignored");

    }

    private void hideDialog() {

        mView.setVisibility(View.GONE);
    }
}
