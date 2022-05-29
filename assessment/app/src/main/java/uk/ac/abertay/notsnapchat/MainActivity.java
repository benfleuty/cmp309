package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSIONS = 111;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};

    private PreviewView previewView;
    private Button overlayButton;

    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private CameraSelector cameraSelector;
    private int cameraFacing;
    private ProcessCameraProvider cameraProvider;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        previewView = findViewById(R.id.cameraPreview);
        overlayButton = findViewById(R.id.overlayButton);

        previewView.setOnTouchListener(
                new View.OnTouchListener() {
                    private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            MainActivity.this.swapCameras();
                            return super.onDoubleTap(e);
                        }
                    });

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        gestureDetector.onTouchEvent(motionEvent);
                        return true;
                    }
                }
        );

        if (!allPermissionsGranted()) {
            requestAllPermissions();
        }


        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderListenableFuture.get();
                startCameraX();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private void swapCameras() {
        switch (cameraFacing) {
            case 0:
                setCameraFacing(1);
                break;
            case 1:
                setCameraFacing(0);
                break;
            default:
                // error}
        }
    }

    private void setCameraFacing(int direction) {
        Log.i("direction","direction is " + String.valueOf(direction));
        cameraProvider.unbindAll();
        // camera selection use case
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(direction)
                .build();

        cameraFacing = direction;

        // preview use case
        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX() {
        setCameraFacing(0);
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}