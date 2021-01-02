package com.chris.thomson.midlandsriders.activities.resetpassword

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


class ResetPwdPresenter(var mContext: Context, var view: ResetPwsInterface) {
    fun doReset(etEmailReset: AppCompatEditText, etPwdReset: AppCompatEditText, etCpwdReset: AppCompatEditText) {
        if (AppPreferences.isNetworkAvailable(mContext)) {

            if (etEmailReset.text.toString().isNullOrEmpty()) {
                var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_email)).plus("</font>")
                etEmailReset.setError(Html.fromHtml(error))
                etEmailReset.requestFocus()
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(etEmailReset.text.toString()).matches()) {
                var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_email_valid)).plus("</font>")
                etEmailReset.setError(Html.fromHtml(error))
                etEmailReset.requestFocus()
                return
            }
            if (etPwdReset.text.toString().isNullOrEmpty()) {
                var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_password)).plus("</font>")
                etPwdReset.setError(Html.fromHtml(error))
                etPwdReset.requestFocus()
                return
            }
            if (etPwdReset.text.toString().length < 8) {
                var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_password_valid)).plus("</font>")
                etPwdReset.setError(Html.fromHtml(error))
                etPwdReset.requestFocus()
                return
            }
            if (etCpwdReset.text.toString().isNullOrEmpty()) {
                var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_password)).plus("</font>")
                etCpwdReset.setError(Html.fromHtml(error))
                etCpwdReset.requestFocus()
                return
            }
            if (!etPwdReset.text.toString().equals(etCpwdReset.text.toString())) {
                var error: String = "<font color='red'>".plus(mContext.resources.getString(R.string.err_cpassword_valid)).plus("</font>")
                etCpwdReset.setError(Html.fromHtml(error))
                etCpwdReset.requestFocus()
                return
            }

            view.showLoading()
            RetrofitClient.instance.ResetPassword(etEmailReset.text.toString(), etPwdReset.text.toString())
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