package com.hisham.permissionshelper;

import android.app.Activity;
import android.content.Context;

import java.util.List;

/**
 * Created by Hisham on 2/4/2016.
 */
public interface IPermission {
    /**
     * Requesting runtime permissions, You can pass an array of permissions here
     * @param activity - activity
     * @param permissions - permissions array ex: new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}
     * @param requestCode - a unique integer code (unique within an activity), it will be returned in the callback
     * @param callback - callback whether granted or denied - IPermissionCallback
     */
    void requestPermission(Activity activity, String[] permissions, int requestCode, IPermissionCallback callback);

    /**
     * Requesting runtime permission
     * @param activity - activity
     * @param permission - permission ex: Manifest.permission.ACCESS_FINE_LOCATION
     * @param requestCode - a unique integer code (unique within an activity), it will be returned in the callback
     * @param callback - callback whether granted or denied - IPermissionCallback
     */
    void requestPermission(Activity activity, String permission, int requestCode, IPermissionCallback callback);

    /**
     * Opens the settings activity of your application.
     * @param activity - activity example: MainActivity.this
     */
    void openPermissionSettings(Activity activity);

    /**
     * You must override this method inside your activity like this
     * <pre>
     *  public class MainActivity extends AppCompatActivity {

            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                iPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
     </pre>
     * @param requestCode - requestCode
     * @param permissions - permissions 
     * @param grantResults - grantResults
     */
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    /**
     * @param context - context example: getApplicationContext()
     * @return - a list of all granted permissions
     */
    List<String> getGrantedPermissionList(Context context);

    /**
     * @param context - context example: getApplicationContext()
     * @return - a list of all denied permissions
     */
    List<String> getDeniedPermissionList(Context context);

    /**
     * Get all possible runtime permissions
     * @return a list of all runtime permissions (whether denied, granted or not requested yet)
     */
    List<String> getAllPermissionsList();

    /**
     * Check if a permission is granted
     * @param context - context example: getApplicationContext()
     * @param permission - permission ex: Manifest.permission.ACCESS_FINE_LOCATION
     * @return true if permission is already granted
     */
    boolean isPermissionGranted(Context context, String permission);

    /**
     * Check if a permission is denied
     * @param context - context example: getApplicationContext()
     * @param permission - permission ex: Manifest.permission.ACCESS_FINE_LOCATION
     * @return true if permission is already denied
     */
    boolean isPermissionDenied(Context context, String permission);
}
