# PermissionsHelperApplication
A helper library for Marshmallow permissions.
[![](https://jitpack.io/v/hishamMuneer/PermissionsHelperApplication.svg)](https://jitpack.io/#hishamMuneer/PermissionsHelperApplication)


![Single Permission Dialog](https://cloud.githubusercontent.com/assets/3941245/12883479/5386a678-ce7e-11e5-81d1-2d415ca0a4ca.png)
![Multiple Permissions Dialog](https://cloud.githubusercontent.com/assets/3941245/12883480/538af818-ce7e-11e5-8e3a-ac43953824d7.png)
![Open Settings](https://cloud.githubusercontent.com/assets/3941245/12908825/8891768e-cf20-11e5-9411-791fa8ccb005.png)

## How to import it?

Add the following lines in your app's module build.gradle:

    repositories {
        maven {
            url "https://dl.bintray.com/hisham/maven"
        }
    }
    dependencies {
        compile 'com.hisham.perms:perms:1.0.3'
    }

[![Download](https://api.bintray.com/packages/hisham/maven/permissions-helper/images/download.svg) ](https://bintray.com/hisham/maven/permissions-helper/_latestVersion)

## How to use: 

    public class MainActivity extends AppCompatActivity {

        IPermission iPermission = PermissionImplementation.getInstance();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

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

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            iPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

## Runtime Permissions Flow Chart: 

This is the flow that this library simplifies. 

![Runtime Permission Flow Chart](https://cloud.githubusercontent.com/assets/3941245/13917231/6957d38c-ef85-11e5-89e7-39cb34913438.png)
