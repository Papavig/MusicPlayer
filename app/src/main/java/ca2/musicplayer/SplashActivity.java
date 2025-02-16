package ca2.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView appIcon = findViewById(R.id.appIcon);
        TextView appTitle = findViewById(R.id.appTitle);

        // Scale Animation (Zoom-in)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.0f, 1.0f,  // From 0 to full scale (X)
                0.0f, 1.0f,  // From 0 to full scale (Y)
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1000); // 1 second
        scaleAnimation.setFillAfter(true);

        // Fade-in Animation
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);

        // Start animations
        appIcon.startAnimation(scaleAnimation);
        appTitle.startAnimation(fadeIn);

        // Navigate to MainActivity after animation completes
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2500); // 2.5 seconds
    }
}
