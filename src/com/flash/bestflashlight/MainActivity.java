package com.flash.bestflashlight;

import android.app.Activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;

import android.hardware.Camera.Parameters;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageButton switchOnTheLight;
    private ImageView imageLight;
    private Camera camera;
    private StartAppAd startAppAd = new StartAppAd(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LockOrientation(this).lock();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        StartAppSDK.init(this, "106159305", "206304172", true);

        setContentView(R.layout.main);

        switchOnTheLight = (ImageButton) findViewById(R.id.switchOnTheLightButton);
        imageLight = (ImageView) findViewById(R.id.lightImageView);

        imageLight.setOnClickListener(this);
        switchOnTheLight.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startAppAd.onResume();
        if (camera == null) {
            camera = Camera.open();
        }
        turnOnFlashLight();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startAppAd.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.switchOnTheLightButton:
                    processFlashLight();
                break;
        }
    }

    private void processFlashLight() {
        if (isFlashLightTurnedOn()) {
            turnOffFlashLight();
        }
        else {
            turnOnFlashLight();
        }

    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }

    private Boolean isFlashLightTurnedOn() {
            Parameters p = camera.getParameters();

            if (p.getFlashMode().equals(Parameters.FLASH_MODE_OFF)) {
                return false;
            }
            else {
                return true;
            }

    }

    private void turnOnFlashLight() {
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        imageLight.setVisibility(View.VISIBLE);
        camera.setParameters(p);
        camera.startPreview();
    }

    private void turnOffFlashLight() {
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_OFF);
        imageLight.setVisibility(View.INVISIBLE);
        camera.setParameters(p);
        camera.startPreview();
    }
}
