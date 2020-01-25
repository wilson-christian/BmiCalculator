package com.app.bmicalculator.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.bmicalculator.R
import com.app.bmicalculator.helper.Utils
import com.app.bmicalculator.helper.Utils.openAppPlayStore
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_detail.*


const val NAME = "NAME"
const val HEIGHT = "HEIGHT"
const val WEIGHT = "WEIGHT"
const val GENDER = "GENDER"

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        initBannerAd()
        initToolbar()
        calculateBMI()
        initOptions()
    }

    private fun initOptions() {
        llShare.setOnClickListener {
            checkPermissionAndShare() // check storage permission to share screenshot
        }

        llRate.setOnClickListener {
            Toast.makeText(this, "Please rate app on playstore", Toast.LENGTH_SHORT).show()
            openAppPlayStore(this)
        }
    }

    /*Checks whether app has storage permission to store Image - YES > would share it to social apps*/
    private fun checkPermissionAndShare() {
        askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE){
            //all permissions already granted or just granted

            val screenShot = Utils.takeScreenshotOfView(clBmi)
            Utils.storeImage(this, screenShot)

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain()
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }

            if(e.hasForeverDenied()) {
                Toast.makeText(this, "Forever permission denied, please give it via setting", Toast.LENGTH_SHORT).show()

                // you need to open setting manually if you really need it
                e.goToSettings()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateBMI() {

        val height = intent.getStringExtra(HEIGHT)
        val weight = intent.getStringExtra(WEIGHT)
        val gender = intent.getStringExtra(GENDER)
        val name = intent.getStringExtra(NAME)

        val heightValue = height.toFloat() / 100
        val weightValue = weight.toFloat()
        val bmi = weightValue / (heightValue * heightValue) // BMI calculate formula

        tvBmi.text =  "%.2f".format(bmi)
        tvResult.text = "Hi $name, ${Utils.getCategory(bmi.toDouble())}"
    }

    private fun initToolbar() {
        toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    // Initialize the banner ad
    private fun initBannerAd() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}
