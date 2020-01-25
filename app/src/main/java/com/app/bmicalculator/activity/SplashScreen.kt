package com.app.bmicalculator.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.app.bmicalculator.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private var isFirstAnimation = false
    private var isSecondAnimation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /*Simple hold animation to hold ImageView in the centre of the screen at a slightly larger
        scale than the ImageView's original one.*/
        val hold = AnimationUtils.loadAnimation(this, R.anim.hold)

        /* Translates ImageView from current position to its original position, scales it from
        larger scale to its original scale.*/
        val translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale)
        val fadeIn = AnimationUtils.loadAnimation(this@SplashScreen, R.anim.fade_in)
        val imageView = findViewById<ImageView>(R.id.header_icon)
        translateScale.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation()
                    tvAppName.visibility = VISIBLE
                    tvAppName.startAnimation(fadeIn)
                }
                isFirstAnimation = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        hold.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                imageView.clearAnimation()
                imageView.startAnimation(translateScale)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        imageView.startAnimation(hold)


        fadeIn.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (!isSecondAnimation) {
                    tvAppName.clearAnimation()

                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                isSecondAnimation = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }
}