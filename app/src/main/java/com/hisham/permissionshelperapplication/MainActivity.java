package com.hisham.permissionshelperapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hisham.permissionshelper.IPermission;
import com.hisham.permissionshelper.IPermissionCallback;
import com.hisham.permissionshelper.PermissionImplementation;

import java.util.List;


/**
 * How this library was created: Used the following link: http://brianattwell.com/distributing-android-libs-via-jcenter/
 * IMPORTANT : Dont run the command --> gradlew install, Only run gradlew bintrayUpload
 * IMPORTANT : Both of the above commands can be executed from Android Studio's Terminal
 */
public class MainActivity extends AppCompatActivity {

    private IPermission iPermission = PermissionImplementation.getInstance();
    private View coordinatorLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayoutView = findViewById(R.id.coordinatorLayout);

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
            public void permissionDenied(int requestCode, boolean willShowCheckBoxNextTime) {
                if (willShowCheckBoxNextTime) {
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
        String[] permissions = new String[]{Manifest.permission.READ_SMS, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO };
        iPermission.requestPermission(MainActivity.this, permissions, 2, new IPermissionCallback() {
            @Override
            public void permissionGranted(int requestCode) {
                Snackbar.make(view, "Permissions Granted", Snackbar.LENGTH_INDEFINITE).show();
            }

            @Override
            public void permissionDenied(int requestCode, boolean willShowCheckBoxNextTime) {
                if (willShowCheckBoxNextTime) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        List<String> permissions;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_all:
                permissions = iPermission.getAllPermissionsList();
                break;
            case R.id.action_granted_list:
                permissions = iPermission.getGrantedPermissionList(getApplicationContext());
                break;
            case R.id.action_settings:
                iPermission.openPermissionSettings(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        if(permissions != null) {
            String message = "";
            // if there is no permission granted
            if (permissions.size() == 0) {
                message = "No permissions granted";
            } else {
                for (String permission : permissions) {
                    message = message + permission + System.getProperty("line.separator");
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Permissions: ");
            builder.setMessage(message);
            builder.create().show();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        iPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
