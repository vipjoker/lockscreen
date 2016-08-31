package com.test.addapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int WINDOW_PERMISSION = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.btnStartService);
        button.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {someMethod();}});



    }


    private void requestPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SYSTEM_ALERT_WINDOW);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?




            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, WINDOW_PERMISSION);

//
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW)) {
//
//                Toast.makeText(MainActivity.this, "Need internet access to get map tiles if working with online sources", Toast.LENGTH_LONG).show();
//
//            } else {
//
//
//
//            }


        }
    }


    private void startAddService(){
        Intent intent = new Intent(this,UpdateService.class);
        startService(intent);
    }



    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @TargetApi(Build.VERSION_CODES.M)
    public void someMethod() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }else{
            startAddService();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {

            if (!Settings.canDrawOverlays(this)) {

                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                MainActivity.this.startService(intent);

            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WINDOW_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Online map sources will be unavailable", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
