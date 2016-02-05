package com.hisham.permissionshelper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hisham on 2/4/2016.
 */
public class PermissionImplementation implements IPermission  {

    private final String TAG = PermissionImplementation.class.getSimpleName();

    private IPermissionCallback callback = null;

    private Activity activity;

    private int requestCode;

    /**
     * A map that stores the permissions which are not granted by the user
     */
    private Map<String, Boolean> permissionsNotGrantedMap = new HashMap();

    @Override
    public void requestPermissions(Activity activity, String[] permissions, int requestCode, IPermissionCallback callback) {

        this.callback = callback;
        this.activity = activity;
        this.requestCode = requestCode;

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
        requestPermissions(activity, new String[]{permission}, requestCode, callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(callback == null){
            Log.e(TAG, "Permission is not requested from Permission Library.");
            return;
        }
        if (this.requestCode == requestCode) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                callback.permissionGranted(requestCode);
            } else {
                if (permissions.length == 1) {
                    callback.permissionDenied(requestCode, permissionsNotGrantedMap.get(permissions[0]).booleanValue());
                } else {
                    callback.permissionDenied(requestCode, false);
                }
            }
        } else {
            Log.e(TAG, "Request code doesn't match");
        }
    }
}
