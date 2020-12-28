package com.chris.thomson.midlandsriders.activities.signin

import android.content.Context
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.api.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPresenter(var mContext: Context, var view: LogInInterface) {
    fun doLogin(etEmail: AppCompatEditText, etPassword: AppCompatEditText) {


        if (etEmail.text.toString().isNullOrEmpty()) {
            var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_email)).plus("</font>")
            etEmail.setError(Html.fromHtml(error))
            etEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_email_valid)).plus("</font>")
            etEmail.setError(Html.fromHtml(error))
            etEmail.requestFocus()
            return
        }
        if (etPassword.text.toString().isNullOrEmpty()) {
            var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_password)).plus("</font>")
            etPassword.setError(Html.fromHtml(error))
            etPassword.requestFocus()
            return
        }
        if (etPassword.text.toString().length < 8) {
            var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_password_valid)).plus("</font>")
            etPassword.setError(Html.fromHtml(error))
            etPassword.requestFocus()
            return
        }

        view.showLoading()
        RetrofitClient.instance.LogIn(etEmail.text.toString(), etPassword.text.toString())
                .enqueue(object: Callback<ResponseBody>{
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("TAG", "onResponse: " + response.body()?.string())

                            var response=response.body()?.string().toString()

                        }



                    }
                })



    }
}