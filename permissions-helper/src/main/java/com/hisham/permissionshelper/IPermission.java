package com.hisham.permissionshelper;

import android.app.Activity;

/**
 * Created by Hisham on 2/4/2016.
 */
public interface IPermission {
    void requestPermissions(Activity activity, String[] permissions, int requestCode, IPermissionCallback callback);
    void requestPermission(Activity activity, String permission, int requestCode, IPermissionCallback callback);
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
