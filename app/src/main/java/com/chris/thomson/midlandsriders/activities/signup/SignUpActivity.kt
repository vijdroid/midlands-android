package com.chris.thomson.midlandsriders.activities.signin.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.billingclient.api.*
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.activities.signin.LogInActivity
import kotlin.math.log

class SignUpActivity : AppCompatActivity(),View.OnClickListener,PurchasesUpdatedListener {
    lateinit var billingClient:BillingClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setBillingClient()
    }

    private fun setBillingClient() {
        billingClient=BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener{
            override fun onBillingServiceDisconnected() {

                Log.d("TAG", "onBillingServiceDisconnected: ")
            }

            override fun onBillingSetupFinished(p0: BillingResult) {
                Log.d("TAG", "onBillingSetupFinished: "+p0.debugMessage)
            }

        })
    }

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.llSignUp->{
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }  R.id.btnSignUp->{

        }
        }
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {


    }
}