package uk.ac.abertay.notsnapchat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionChecker {

    public static void RequestAllPermissions(Context context, String[] permissions) {
        try {
            boolean allGranted = true;
            for (String permission : permissions) {
                allGranted &= ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            }
            if (!allGranted) {
                ActivityCompat.requestPermissions((Activity) context, permissions, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
