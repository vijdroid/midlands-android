package com.chris.thomson.midlandsriders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar


class SplashActivity : AppCompatActivity() {
    private var mProgress: ProgressBar? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show the splash screen
        setContentView(R.layout.activity_splash)

        Log.d(TAG, "Splash onCreate")
        val msg = getIntent().toString()
        Log.d(TAG, msg)

        val shouldShowNotifications = checkIntent(intent)

        // Start lengthy operation in a background thread
        Thread(Runnable {
            doWork()
            startApp(shouldShowNotifications)
            finish()
        }).start()
    }

    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d(TAG, "Splash onNewIntent")
        val msg = getIntent().toString()
        Log.d(TAG, msg)

        checkIntent(intent)
    }

    private fun checkIntent(intent: Intent?): Boolean {
        if (intent?.extras != null) {
            Log.d(TAG, "Has extras!")
            return true
        } else {
            Log.d(TAG, "Doesn't have extras")
            return false
        }
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

    private fun startApp(showNotifications: Boolean) {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtra("showNotifications", showNotifications)
        startActivity(intent)
    }

    companion object {

        private const val TAG = "SplashActivity"
    }
}
