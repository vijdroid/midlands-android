package com.chris.thomson.midlandsriders.activities.signin

import android.content.Context
import android.text.Html
import android.util.Log
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.Utils.AppPreferences
import com.chris.thomson.midlandsriders.Utils.ApliList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginPresenter(var mContext: Context,var view :LogInInterface) {

    fun doLogin(etEmail: AppCompatEditText, etPassword: AppCompatEditText) {
        if(etEmail.text.toString().isNullOrEmpty()){
            var error:String= "<font color='red'>".plus(mContext.resources.getString(R.string.err_email)).plus("</font>")
            etEmail.setError(Html.fromHtml(error))
            etEmail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
            var error:String= "<font color='red'>".plus(mContext.resources.getString(R.string.err_email_valid)).plus("</font>")
            etEmail.setError(Html.fromHtml(error))
            etEmail.requestFocus()
            return
        }
        if(etPassword.text.toString().isNullOrEmpty()){
            var error:String= "<font color='red'>".plus(mContext.resources.getString(R.string.err_password)).plus("</font>")
            etPassword.setError(Html.fromHtml(error))
            etPassword.requestFocus()
            return
        }
        if(etPassword.text.toString().length<8){
            var error:String= "<font color='red'>".plus(mContext.resources.getString(R.string.err_password_valid)).plus("</font>")
            etPassword.setError(Html.fromHtml(error))
            etPassword.requestFocus()
            return
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(AppPreferences.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(ApliList::class.java)
        val call = service.LogIn(etEmail.text.toString(),etPassword.text.toString())

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {

                    Log.d("TAG", "onResponse: "+response)

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAG", "onFailure: "+t.toString())
            }
        })

    }
}