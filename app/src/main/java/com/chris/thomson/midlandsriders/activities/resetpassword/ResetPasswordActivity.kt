package com.chris.thomson.midlandsriders.activities.resetpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.chris.thomson.midlandsriders.Helpers.LoaderHelper
import com.chris.thomson.midlandsriders.R
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity(),ResetPwsInterface ,View.OnClickListener{
    private var loader: LoaderHelper? = null
    lateinit var mPresenter: ResetPwdPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_reset_password)
        IntiView()
    }
    private fun IntiView() {

        loader = LoaderHelper(this)
        mPresenter=ResetPwdPresenter(this,this)
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

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnResetpd->{
                mPresenter.doReset(etEmailReset,etPwdReset,etCpwdReset)
            }
            R.id.tvBack->{
                onBackPressed()
            }
        }
    }
}