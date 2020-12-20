package com.chris.thomson.midlandsriders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.chris.thomson.midlandsriders.Adapters.StoredNotification
import com.chris.thomson.midlandsriders.Adapters.StoredNotificationRoomDatabase
import com.chris.thomson.midlandsriders.Utils.AppPreferences
import com.chris.thomson.midlandsriders.activities.signin.LogInActivity
import com.firebase.ui.auth.ui.email.SignInActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private var mProgress: ProgressBar? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show the splash screen
        setContentView(R.layout.activity_splash)

        val msg = getIntent().toString()
        Log.d(TAG, msg)
        //saveNotification("Test 1", "Test 4 Data")

        val shouldShowNotifications = checkIntent(intent)

        // Start lengthy operation in a background thread
        Thread(Runnable {
            doWork()
            startApp(shouldShowNotifications)

        }).start()
    }

    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        //Log.d(TAG, "Splash onNewIntent")
        val msg = getIntent().toString()
        //Log.d(TAG, msg)
    }

    private fun saveNotification(title: String, body: String) {
        val newNotification = StoredNotification(title, body)
        val db = Room.databaseBuilder(this, StoredNotificationRoomDatabase::class.java, "word_database").build()

        GlobalScope.launch {
            db.storedNotificationDao().insert(newNotification)
            //db.storedNotificationDao().getAlphabetizedNotifications().value?.size
        }
    }

    private fun checkIntent(intent: Intent?): Boolean {
        if (intent?.extras != null) {
            //Log.d(TAG, "Has extras!")
            val contents = intent!!.extras!!.describeContents()
            val extras = intent.extras!!
            val keys = extras.keySet()
            //Log.d(TAG, keys.toString())
            val iterator = keys.iterator()
            iterator.forEach {
                Log.e(TAG, it)
            }
            //Log.e("Tag","Data : "+extras.get("title") +" : Value  "+extras.get("body") )
            //only show notifications if intent has message id
            if (keys.contains("google.message_id")) {
                //Log.e(TAG, "has message id")
                saveNotification(extras.get("title").toString(), extras.get("body").toString())
                return true
            } else {
                return false
            }
        } else {
            //Log.e(TAG, "Doesn't have extras")
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


        if (AppPreferences.getBool(AppPreferences.ISLOGIN)) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra("showNotifications", showNotifications)
            startActivity(intent)
        } else {

            val intent = Intent(this@SplashActivity, LogInActivity::class.java)
            startActivity(intent)
        }

        finish()

    }

    companion object {
        private const val TAG = "Tag SplashActivity"
    }
}
