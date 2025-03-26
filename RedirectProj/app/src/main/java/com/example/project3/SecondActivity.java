package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {
    ImageView imageView;
    boolean isAnimating = false;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.second), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageView = findViewById(R.id.imageView);
        Button selfieButton = findViewById(R.id.selfieButton);
        SwitchCompat animationSwitch = findViewById(R.id.animationSwitch);

        // Launch camera when button is clicked
        selfieButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = createImageFile();
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.example.project3.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraLauncher.launch(cameraIntent);
            }
        });

        // Handle animation switch
        animationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startAnimation();
            } else {
                stopAnimation();
            }
        });
    }


    protected final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                if (photoURI != null) {
                    try {
                        // Load full-size image
                        Bitmap fullImage = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        imageView.setImageBitmap(fullImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        }
    );

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile("IMG_" + timeStamp, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void startAnimation() {

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new android.view.animation.LinearInterpolator());
        animationSet.setRepeatCount(Animation.INFINITE);
        animationSet.setRepeatMode(Animation.REVERSE);

        RotateAnimation rotate = new RotateAnimation(-90, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.REVERSE);

        ScaleAnimation scale = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(2000);
        scale.setRepeatCount(Animation.INFINITE);
        scale.setRepeatMode(Animation.REVERSE);

        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        alpha.setDuration(2000);
        alpha.setRepeatCount(Animation.INFINITE);
        alpha.setRepeatMode(Animation.REVERSE);

        animationSet.addAnimation(rotate);
        animationSet.addAnimation(scale);
        animationSet.addAnimation(alpha);

        imageView.startAnimation(animationSet);
        isAnimating = true;
    }

    private void stopAnimation() {
        imageView.clearAnimation();
        isAnimating = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (photoURI != null) {
            outState.putString("photo_uri", photoURI.toString());
        }
    }
}
