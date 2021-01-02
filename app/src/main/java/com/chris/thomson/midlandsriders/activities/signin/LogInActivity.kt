package com.chris.thomson.midlandsriders.activities.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chris.thomson.midlandsriders.Helpers.LoaderHelper
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.activities.resetpassword.ResetPasswordActivity
import com.chris.thomson.midlandsriders.activities.signin.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*

class LogInActivity : AppCompatActivity(), View.OnClickListener, LogInInterface {
    lateinit var loginPresenter: LoginPresenter
    private var loader: LoaderHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_login)
        IntiView()
    }

    private fun IntiView() {

        loader = LoaderHelper(this)
        loginPresenter = LoginPresenter(this, this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.llSignUp -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.btnSignIn -> {
                loginPresenter.doLogin(etEmail, etPassword)
            }
            R.id.tvForgot->{
                val intent = Intent(this, ResetPasswordActivity::class.java)
                startActivity(intent)
            }

        }
    }


    override fun OnSuccess() {


    }

    override fun OnError() {

    }

    override fun showLoading() {
        loader!!.showDialog()
    }

    override fun hideLoading() {

        loader!!.hideDialog()
    }
}