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
     * If user denies a permission once, next time that permission comes with a check box(never ask again).
     * If user check that box and denies again, no permission dialog is shown to the user next time and permission will be denied automatically.
     * @param requestCode - same integer is returned that you have passed when you called requestPermission();
     * @param willShowCheckBoxNextTime - For a request of multiple permissions at the same time, you will always receive false.
     */
    void permissionDenied(int requestCode, boolean willShowCheckBoxNextTime);
}
