# PermissionsHelperApplication
A helper library for Marshmallow permissions.

## How to import it?

Add the following lines in your module build.gradle:

    repositories {
        maven {
            url "https://jitpack.io"
        }
    }

    dependencies {
        compile 'com.github.tajchert:nammu:1.1.1'
    }

## How to use: 


    public class MainActivity extends AppCompatActivity {

    IPermission iPermission = new PermissionImplementation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iPermission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, 1, new IPermissionCallback() {
            @Override
            public void permissionGranted(int requestCode) {
                // if permission granted successfully
            }

            @Override
            public void permissionDenied(int requestCode, boolean isDeniedPreviously) {
              // if permission is denied
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        iPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

