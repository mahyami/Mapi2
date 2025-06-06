package com.example.mapi.data.remote.services

import android.net.Uri
import android.util.Log
import com.example.mapi.BuildConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject


/**
 * Copyright goes to Piyush 🚀
 * https://github.com/poush
 * */
class AuthenticationService @Inject constructor() {

    fun authenticate(
        uri: Uri,
        onAccessToken: (String) -> Unit
    ) {
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        Log.d("MainActivity", "Code: $code, State: $state")
        exchangeCodeForToken(code!!) { accessToken ->
            onAccessToken(accessToken!!)
        }
    }

    private fun exchangeCodeForToken(
        code: String,
        callback: (String?) -> Unit
    ) {
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("code", code)
            .add(
                "client_id",
                BuildConfig.OAUTH_CLIENT_ID
            )
            .add("client_secret", BuildConfig.OAUTH_CLIENT_SECRET)
            .add("redirect_uri", "https://g.dev/mahya")
            .add("grant_type", "authorization_code")
            .build()

        val request = Request.Builder()
            .url("https://oauth2.googleapis.com/token")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        response.body?.string()?.let {
                            val jsonObject = JSONObject(it)
                            val accessToken = jsonObject.getString("access_token")
                            callback(accessToken)
                        }
                    } else {
                        callback(null)
                    }
                }
            }
        )
    }
}