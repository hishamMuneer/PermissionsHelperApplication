package com.hisham.permissionshelperapplication;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.hisham.permissionshelper.IPermission;
import com.hisham.permissionshelper.IPermissionCallback;
import com.hisham.permissionshelper.PermissionImplementation;


/**
 * How this library was created: Used the following link: http://brianattwell.com/distributing-android-libs-via-jcenter/
 * IMPORTANT : Dont run the command --> gradlew install, Only run gradlew bintrayUpload
 * IMPORTANT : Both of the above commands can be executed from Android Studio's Terminal
 */
public class MainActivity extends AppCompatActivity {

    IPermission iPermission = PermissionImplementation.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "A simple snackbar :)", Snackbar.LENGTH_INDEFINITE).show();

            }
        });
    }

    public void reqPermission(final View view){
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        iPermission.requestPermission(MainActivity.this, permission, 1, new IPermissionCallback() {
            @Override
            public void permissionGranted(int requestCode) {
                Snackbar.make(view, "Location Permission Granted", Snackbar.LENGTH_INDEFINITE).show();
            }

            @Override
            public void permissionDenied(int requestCode, boolean isDeniedPreviously) {
                if (isDeniedPreviously) {
                    Snackbar.make(view, "You need to enable location permission for this thing to work.", Snackbar.LENGTH_INDEFINITE).setAction("Open Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iPermission.openPermissionSettings(MainActivity.this);
                        }
                    }).show();
                } else {
                    Snackbar.make(view, "Location Permission Denied", Snackbar.LENGTH_INDEFINITE).show();

                }
            }
        });
    }

    public void reqPermissions(final View view){
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO };
        iPermission.requestPermission(MainActivity.this, permissions, 2, new IPermissionCallback() {
            @Override
            public void permissionGranted(int requestCode) {
                Snackbar.make(view, "Permissions Granted", Snackbar.LENGTH_INDEFINITE).show();
            }

            @Override
            public void permissionDenied(int requestCode, boolean isDeniedPreviously) {
                if (isDeniedPreviously) {
                    Snackbar.make(view, "You need to enable these permissions for this thing to work.", Snackbar.LENGTH_INDEFINITE).setAction("Open Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iPermission.openPermissionSettings(MainActivity.this);
                        }
                    }).show();
                } else {
                    Snackbar.make(view, "Permissions Denied", Snackbar.LENGTH_INDEFINITE).show();

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        iPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
