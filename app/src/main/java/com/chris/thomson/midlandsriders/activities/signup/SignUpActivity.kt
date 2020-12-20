package com.chris.thomson.midlandsriders.activities.signin.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.activities.signin.LogInActivity

class SignUpActivity : AppCompatActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
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
}