package com.hisham.permissionshelper;

import android.app.Activity;

/**
 * Created by Hisham on 2/4/2016.
 */
public interface IPermission {
    /**
     * Requesting runtime permissions, You can pass an array of permissions here
     * @param activity
     * @param permissions
     * @param requestCode
     * @param callback
     */
    void requestPermission(Activity activity, String[] permissions, int requestCode, IPermissionCallback callback);

    /**
     * Requesting runtime permission
     * @param activity
     * @param permission
     * @param requestCode
     * @param callback
     */
    void requestPermission(Activity activity, String permission, int requestCode, IPermissionCallback callback);

    /**
     * Opens the settings activity of your application.
     * @param context
     */
    void openPermissionSettings(Activity context);

    /**
     * You must override this method inside your activity like this
     * <pre>
     *  public class MainActivity extends AppCompatActivity {

            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                iPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
     </pre>
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
