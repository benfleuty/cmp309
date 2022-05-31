package uk.ac.abertay.notsnapchat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationRequest;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetLocation implements Runnable {

    private final Context context;
    FusedLocationProviderClient fusedLocationProviderClient;
    private volatile String city;

    @SuppressLint("MissingPermission")
    public GetLocation(Context context) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public void run() {
        String[] reqPerms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        PermissionChecker.RequestAllPermissions(context, reqPerms);

        fusedLocationProviderClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        });
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder
                        .getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                Log.i("city", e.toString());
            }

            try {
                String address = null;

                if (addresses != null) {
                    if (addresses.get(0).getLocality() == null)
                        address = addresses.get(0).getAddressLine(0);
                    else
                        address = addresses.get(0).getLocality();
                }

                if (address != null) {
                    if (address.contains("UK")) {
                        address = address.split(",")[1];
                        address = address.trim();
                        address = address.split(" ")[0];
                    }
                    city = address;
                    ImageViewerActivity x = (ImageViewerActivity) context;
                }

            } catch (Exception e) {
                Log.w("city", e.toString());
                Toast.makeText(context, "Location is probably borked on emulators! Try wiping emulator data!", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Could not get your location! Check your Internet connection!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
