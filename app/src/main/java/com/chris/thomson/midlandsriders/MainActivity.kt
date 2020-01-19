package com.chris.thomson.midlandsriders

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.chris.thomson.midlandsriders.Fragments.*
import android.text.Spannable
import android.text.SpannableString
import android.graphics.Typeface
import android.util.Log
import android.widget.Toast
import com.chris.thomson.midlandsriders.TypeFaces.CustomTypefaceSpan
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        ShopFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener,
        EventsFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener{

    private var mStorageRef: StorageReference? = null


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mStorageRef = FirebaseStorage.getInstance().getReference();

        setSupportActionBar(toolbar)

        supportActionBar!!.setTitle(null)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setInitialFragment()

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })



    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.


        menuInflater.inflate(R.menu.main, menu)
        var navView = findViewById(R.id.nav_view) as NavigationView
        val m = navView.getMenu()
        for (i in 0 until m.size()) {
            val mi = m.getItem(i)

            val subMenu = mi.getSubMenu()
            if (subMenu != null && subMenu!!.size() > 0) {
                for (j in 0 until subMenu!!.size()) {
                    val subMenuItem = subMenu!!.getItem(j)
                    applyFontToMenuItem(subMenuItem)
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      /*  when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }*/

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        var fragmentClass: Class<*> = HomeFragment:: class.java

        when (item.getItemId()) {
            R.id.nav_home -> fragmentClass = HomeFragment::class.java
            R.id.nav_events -> fragmentClass = EventsFragment::class.java
            R.id.nav_calendar -> fragmentClass = CalendarFragment::class.java
            R.id.nav_shop -> {

                val browserIntent : Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.midlandsriders.com/shop"))
                startActivity(browserIntent)
            }
            R.id.nav_aboutus -> fragmentClass = AboutFragment::class.java
            R.id.nav_notifications -> fragmentClass = NotificationsFragment::class.java
            else -> fragmentClass = HomeFragment::class.java
        }

        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment!!).commit()

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true)


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun setInitialFragment() {

        var fragment: Fragment? = null
        val fragmentClass: Class<*>
        fragmentClass = HomeFragment::class.java
        val fragmentManager = supportFragmentManager

        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment!!).commit()


    }

    private fun applyFontToMenuItem(mi: MenuItem) {
        val font = Typeface.createFromAsset(assets, "font/shadowcard.ttf")
        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(CustomTypefaceSpan("", font), 0, mNewTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        mi.title = mNewTitle
    }

    companion object {

        private const val TAG = "MainActivity"
    }
}
