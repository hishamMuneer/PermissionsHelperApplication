package com.hisham.permissionshelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hisham on 2/4/2016.
 */
public class PermissionImplementation implements IPermission  {

    private final String TAG = PermissionImplementation.class.getSimpleName();
    private static PermissionImplementation instance = null;

    private IPermissionCallback callback = null;

    /**
     * Request code of requested Permission/s
     */
    private int requestCode;

    /**
     * A map that stores the permissions which are not granted by the user.
     * it stores data this way : Map<PERMISSION_NAME, SHOULD_SHOW_RATIONALE>
     */
    private List<String> permissionsNotGrantedMap = null;

    List<String> allPermissions = new ArrayList<>();
    private Activity activity;

    // private constructor
    private PermissionImplementation (){
        // SMS
        allPermissions.add(Manifest.permission.SEND_SMS);
        allPermissions.add(Manifest.permission.READ_SMS);
        allPermissions.add(Manifest.permission.RECEIVE_SMS);
        allPermissions.add(Manifest.permission.RECEIVE_WAP_PUSH);
        allPermissions.add(Manifest.permission.RECEIVE_MMS);
        // Location
        allPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        allPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        // Calendar
        allPermissions.add(Manifest.permission.WRITE_CALENDAR);
        allPermissions.add(Manifest.permission.READ_CALENDAR);
        // Camera
        allPermissions.add(Manifest.permission.CAMERA);
        // Contacts
        allPermissions.add(Manifest.permission.WRITE_CONTACTS);
        allPermissions.add(Manifest.permission.READ_CONTACTS);
        allPermissions.add(Manifest.permission.GET_ACCOUNTS);
        // Microphone
        allPermissions.add(Manifest.permission.RECORD_AUDIO);
        // Phone
        allPermissions.add(Manifest.permission.CALL_PHONE);
        allPermissions.add(Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            allPermissions.add(Manifest.permission.READ_CALL_LOG);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            allPermissions.add(Manifest.permission.WRITE_CALL_LOG);
        }
        allPermissions.add(Manifest.permission.ADD_VOICEMAIL);
        allPermissions.add(Manifest.permission.USE_SIP);
        allPermissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        // Body Sensors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            allPermissions.add(Manifest.permission.BODY_SENSORS);
        }
        // Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            allPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        allPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    /**
     * @return an Instance of this class
     */
    public static PermissionImplementation getInstance(){
        if(instance == null)
            return new PermissionImplementation();
        return instance;
    }

    @Override
    public void requestPermission(final Activity activity, String[] permissions, int requestCode, IPermissionCallback callback) {
        requestPermission(activity, Arrays.asList(permissions), requestCode, callback);
    }

    @Override
    public void requestPermission(Activity activity, List<String> permissions, int requestCode, IPermissionCallback callback) {
        this.activity = activity;
        this.callback = callback;
        this.requestCode = requestCode;

        permissionsNotGrantedMap = new ArrayList<>();
        // lower sdk don't need runtime permission, grant instantly
        if (Build.VERSION.SDK_INT < 23) {
            callback.permissionGranted(requestCode);
        } else {
            boolean isAllPermissionsGranted = true;
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    isAllPermissionsGranted = false;
                    permissionsNotGrantedMap.add(permission);
                }
            }
            if (isAllPermissionsGranted) {
                callback.permissionGranted(requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, permissionsNotGrantedMap.toArray(new String[0]), requestCode);
            }
        }
    }

    @Override
    public void requestPermission(Activity activity, String permission, int requestCode, IPermissionCallback callback) {
        requestPermission(activity, new String[]{permission}, requestCode, callback);
    }

    @Override
    public void openPermissionSettings(final Activity context) {
        if (context == null) {
            Log.i(TAG, "Context is null, Call like this: iPermission.openPermissionSettings(MainActivity.this);");
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(callback == null){
            Log.i(TAG, "Permission is not requested from Permission Library.");
            return;
        }
        if (this.requestCode == requestCode) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                callback.permissionGranted(requestCode);
            } else {
                if (permissions.length == 1 && permissionsNotGrantedMap != null && permissionsNotGrantedMap.size() > 0) {
                    callback.permissionDenied(requestCode, ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsNotGrantedMap.get(0)));
                } else {
                    callback.permissionDenied(requestCode, false);
                }
            }
        } else {
            Log.i(TAG, "RequestCode doesn't match");
        }
    }

    @Override
    public List<String> getAllPermissionsList(){
        return allPermissions;
    }

    @Override
    public List<String> getGrantedPermissionList(final Context context){
        List<String> grantedPermissions = new ArrayList<>();
        for(String permission : allPermissions) {
            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permission);
            }
        }
        return grantedPermissions;
    }

    @Override
    public boolean isPermissionGranted(Context context, String permission){
        if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
}
