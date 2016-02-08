package com.hisham.permissionshelper;

/**
 * Created by Hisham on 2/4/2016.
 */
public interface IPermissionCallback {
    /**
     * Gets called if the permission/permissions you requested are granted successfully by the user.
     * @param requestCode - same integer is returned that you have passed when you called requestPermission();
     */
    void permissionGranted(int requestCode);

    /**
     * Gets called if the permission/permissions you requested are denied by the user.
     * @param requestCode - same integer is returned that you have passed when you called requestPermission();
     * @param isDeniedPreviously -- Let you know if user is denying this permission first or not. If user denies a permission once, second time that permission comes with a check box (never ask again) and if user check it and deny, no permission dialog will be shown to the user and your permission will be denied automatically.
     */
    void permissionDenied(int requestCode, boolean isDeniedPreviously);
}
