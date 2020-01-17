package com.chris.thomson.midlandsriders

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar


class SplashActivity : AppCompatActivity() {
    private var mProgress: ProgressBar? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show the splash screen
        setContentView(R.layout.activity_splash)
        // Start lengthy operation in a background thread
        Thread(Runnable {
            doWork()
            startApp()
            finish()
        }).start()
    }

    private fun doWork() {
        var progress = 0
        while (progress < 100) {
            try {
                Thread.sleep(200)
                mProgress!!.progress = progress
            } catch (e: Exception) {
                e.printStackTrace()
//                Timber.e(e.message)
            }

            progress += 10
        }
    }

    private fun startApp() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
