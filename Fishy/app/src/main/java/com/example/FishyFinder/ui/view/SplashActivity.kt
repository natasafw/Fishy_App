package com.example.FishyFinder.ui.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.FishyFinder.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        startBouncingAnimation(findViewById(R.id.ball1), 100)
//        startBouncingAnimation(findViewById(R.id.ball2), 300)
//        startBouncingAnimation(findViewById(R.id.ball3), 500)

        // Pindah ke MainActivity setelah 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish() // Menutup SplashActivity agar tidak bisa kembali dengan tombol "Back"
        }, 3000) // 3000 ms = 3 detik
    }

    private fun startBouncingAnimation(view: View, delay: Long) {
        val animator = ObjectAnimator.ofFloat(view, "translationY", 0f, -20f)
        animator.duration = 500
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = ValueAnimator.INFINITE

        Handler(Looper.getMainLooper()).postDelayed({
            animator.start()
        }, delay)
    }
}