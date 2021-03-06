package com.chris.thomson.midlandsriders.activities.signin

import android.content.Context
import android.text.Html
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.Utils.AppPreferences
import com.chris.thomson.midlandsriders.Utils.AppPreferences.Companion.showCstomDailog
import com.chris.thomson.midlandsriders.api.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPresenter(var mContext: Context, var view: LogInInterface) {
    fun doLogin(etEmail: AppCompatEditText, etPassword: AppCompatEditText) {
        if (AppPreferences.isNetworkAvailable(mContext)) {
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
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            view.hideLoading()
                            showCstomDailog(mContext, t.message.toString(), false, "Ok", positiveClick = {
                            }, nagativeClick = {

                            })
                        }

                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            view.hideLoading()
                            if (response.code() == 200 || response.code() == 201) {
                                var response = response.body()?.string().toString()
                                try {
                                    var jsonObject = JSONObject(response)
                                    if (jsonObject.getBoolean("success")) {
                                    } else {
                                        showCstomDailog(mContext, jsonObject.getString("message"), false, "Ok", positiveClick = {
                                        }, nagativeClick = {

                                        })
                                    }
                                } catch (e: Exception) {
                                    showCstomDailog(mContext, e.message.toString(), false, "Ok", positiveClick = {

                                    }, nagativeClick = {

                                    })
                                }
                            }


                        }
                    })
        } else {
            showCstomDailog(mContext, mContext.resources.getString(R.string.lbl_no_intetnet), false, "Ok", positiveClick = {

            }, nagativeClick = {

            })
        }


    }


}