package com.app.bmicalculator.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.bmicalculator.R
import com.app.bmicalculator.adapter.WPGenderPickerAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradient()
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        //throw RuntimeException("This is a crash") // to force a crash

        tryToLoadAdOnceAgain()
        initBmiPicker()
        clickListener()
    }

    private fun clickListener() {
        btnCalculate.setOnClickListener{
            if (TextUtils.isEmpty(edName.text)){
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
            }else{
                showInterstitial() // first load ad later open next screen
            }

        }
    }

    private fun initBmiPicker() {

        pickerGender.setAdapter(WPGenderPickerAdapter()) // male/female adapter

        pickerHeight.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE && v.parent != null) {
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            v.onTouchEvent(event)
            true
        })

        pickerWeight.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE && v.parent != null) {
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            v.onTouchEvent(event)
            true
        })

        pickerGender.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE && v.parent != null) {
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            v.onTouchEvent(event)
            true
        })
    }

    /*Sets the gradient to the status bar above toolbar*/
    private fun setStatusBarGradient() {
        val window: Window = window
        val background = ContextCompat.getDrawable(this, R.drawable.bg_gradient_status_bar)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawable(background)
    }


    /*Load a new fullscreen ad*/
    private fun newInterstitialAd(): InterstitialAd {
        val interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.admob_interstitial_ad_id)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Toast.makeText(applicationContext, "Ad Failed To Load", Toast.LENGTH_SHORT).show()
                openDetailActivity()
            }

            override fun onAdClosed() {
                tryToLoadAdOnceAgain()
                openDetailActivity() // open next screen
            }
        }
        return interstitialAd
    }

    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        mInterstitialAd!!.loadAd(adRequest)
    }

    private fun showInterstitial() {
        // Show the ad if it is ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd!!.isLoaded) {
            mInterstitialAd!!.show()
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
            tryToLoadAdOnceAgain()
            openDetailActivity()
        }
    }


    private fun openDetailActivity() {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(HEIGHT, pickerHeight.getCurrentItem())
        intent.putExtra(WEIGHT, pickerWeight.getCurrentItem())
        intent.putExtra(GENDER, pickerGender.getCurrentItem())
        intent.putExtra(NAME, edName.text.toString())
        startActivity(intent)
    }

    /*
        Load fullscreen ad
    */
    private fun tryToLoadAdOnceAgain() {
        mInterstitialAd = newInterstitialAd()
        loadInterstitial()
    }
}
