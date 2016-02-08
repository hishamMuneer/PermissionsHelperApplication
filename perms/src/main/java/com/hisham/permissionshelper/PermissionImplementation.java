package com.hisham.permissionshelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.HashMap;
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
    private Map<String, Boolean> permissionsNotGrantedMap = null;

    private PermissionImplementation (){
        // private constructor
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

        this.callback = callback;
        this.requestCode = requestCode;

        permissionsNotGrantedMap = new HashMap();
        // lower sdk don't need runtime permission, grant instantly
        if (Build.VERSION.SDK_INT < 23) {
            callback.permissionGranted(requestCode);
        } else {
            boolean isAllPermissionsGranted = true;
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    isAllPermissionsGranted = false;
                    permissionsNotGrantedMap.put(permission, ActivityCompat.shouldShowRequestPermissionRationale(activity, permission));
                }
            }
            if (isAllPermissionsGranted) {
                callback.permissionGranted(requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, permissionsNotGrantedMap.keySet().toArray(new String[0]), requestCode);
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
            Log.i(TAG, "Context is null, Call like this: openPermissionSettings(MainActivity.this);");
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
                    callback.permissionDenied(requestCode, permissionsNotGrantedMap.get(permissions[0]).booleanValue());
                } else {
                    callback.permissionDenied(requestCode, false);
                }
            }
        } else {
            Log.i(TAG, "RequestCode doesn't match");
        }
    }
}
