package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};

    private PreviewView previewView;

    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private int cameraFacing;
    private ProcessCameraProvider cameraProvider;

    private ImageCapture imageCapture;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            // These flags cause the device screen to turn on (and bypass screen guard if possible) when launching.
            // This makes it easy for developers to test the app launch without needing to turn on the device
            // each time and without needing to enable the "Stay awake" option.
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
        setContentView(R.layout.activity_main);

        unpackIncomingData();
        setOnClickListeners();

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

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

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            user.get_id(); // result ignored
        } catch (NullPointerException e) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickListeners() {
        Button btnOpenChat = findViewById(R.id.btnOpenChat);
        Button btnCapture = findViewById(R.id.btnCapture);
        Button btnFlipCamera = findViewById(R.id.btnFlipCam);
        Button btnOpenProfile = findViewById(R.id.btnOpenProfile);
        previewView = findViewById(R.id.cameraPreview);

        btnOpenProfile.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
            intentProfile.putExtra("user", User.to_bundle(user));
            startActivity(intentProfile);
            finish();
            return false;
        });

        btnOpenChat.setOnTouchListener((view, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            startActivity(new Intent(this, ChatListActivity.class));
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            return false;
        });

        btnCapture.setOnTouchListener((view, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            capturePhoto();
            return false;
        });

        btnFlipCamera.setOnTouchListener((view, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            swapCameras();
            return false;
        });

        previewView.setOnTouchListener(
                new View.OnTouchListener() {
                    private final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            MainActivity.this.swapCameras();
                            return super.onDoubleTap(e);
                        }
                    });

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        gestureDetector.onTouchEvent(event);
                        return true;
                    }
                }
        );
    }

    private void unpackIncomingData() {
        Intent incoming = getIntent();
        Bundle userAsBundle = incoming.getBundleExtra("user");
        try {
            user = User.parse_bundle(userAsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void capturePhoto() {
        // app storage path
        File storageDir = this.getFilesDir();
        String photoFilePath = storageDir.getAbsolutePath() + "/image.jpg";
        File photo = new File(photoFilePath);

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photo).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        startActivity(new Intent(MainActivity.this, ImageViewerActivity.class));
                        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.none);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                    }
                }
        );
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
        cameraProvider.unbindAll();
        // camera selection use case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(direction)
                .build();

        cameraFacing = direction;

        // preview use case
        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX() {
        setCameraFacing(0);
    }

    private void requestAllPermissions() {
        int REQUEST_CODE_PERMISSIONS = 111;
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