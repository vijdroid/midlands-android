package com.chris.thomson.midlandsriders.activities.signin

interface LogInInterface {
    fun OnSuccess()
    fun OnError()
    fun showLoading()
    fun hideLoading()

}