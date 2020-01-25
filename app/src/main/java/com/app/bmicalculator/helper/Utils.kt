package com.app.bmicalculator.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @JvmStatic
    fun takeScreenshotOfView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }


    @JvmStatic
    fun storeImage(context: Context, image: Bitmap) {
        val pictureFile = getOutputMediaFile(context)
        if (pictureFile == null) {
            Log.d(
                "ERROR",
                "Error creating media file, check storage permissions: "
            ) // e.getMessage());
            return
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()

            val imageUri = FileProvider.getUriForFile(
                context,
                context.packageName+".provider",  //(use your app signature + ".provider" )
                pictureFile
            )

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Checkout your BMI on \nhttps://play.google.com/store/apps/details?id="+context.packageName
                )
                type = "image/jpeg"
            }
            context.startActivity(Intent.createChooser(intent, "Share via..."))

        } catch (e: FileNotFoundException) {
            Log.d("ERROR", "File not found: " + e.localizedMessage)
        } catch (e: IOException) {
            Log.d("ERROR", "Error accessing file: " + e.message)
        }
    }

    /** Create a File for saving an image */
    private fun getOutputMediaFile(context: Context): File? { // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/"
                    + context.packageName
                    + "/Files"
        )
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mediaFile: File
        val mImageName = "MI_$timeStamp.jpg"
        mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
        return mediaFile
    }

    @JvmStatic
    fun getCategory(result: Double): String? {
        val category: String
        category = if (result < 15) {
            "you are very severely underweight"
        } else if (result >= 15 && result <= 16) {
            "you are severely underweight"
        } else if (result > 16 && result <= 18.5) {
            "you are underweight"
        } else if (result > 18.5 && result <= 25) {
            "you are normal (healthy weight)"
        } else if (result > 25 && result <= 30) {
            "you are overweight"
        } else if (result > 30 && result <= 35) {
            "you are moderately obese"
        } else if (result > 35 && result <= 40) {
            "you are severely obese"
        } else {
            "you are very severely obese"
        }
        return category
    }

    @JvmStatic
    fun openAppPlayStore(context: Context) {

        val appPackageName =
            context.packageName // getPackageName() from Context or Activity object

        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }
}