package com.github.afezeria.hymn.common.util

import okhttp3.ConnectionPool
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * @author afezeria
 */

var httpClient: OkHttpClient = OkHttpClient.Builder()
    .connectionPool(ConnectionPool(200, 5L, TimeUnit.MINUTES))
    .callTimeout(10, TimeUnit.SECONDS)
    .build()

fun OkHttpClient.get(url: String, params: Map<String, String?>? = null): String? {
    val httpUrl = url.toHttpUrl().newBuilder().apply {
        params?.forEach { (k, v) ->
            addQueryParameter(k, v)
        }
    }.build()
    val request = Request.Builder()
        .url(httpUrl)
        .get().build()
    newCall(request).execute()
        .use { response: Response -> return response.body?.string() }
}

fun OkHttpClient.put(url: String, content: String): String? {
    val request: Request = Request.Builder()
        .url(url)
        .put(content.toRequestBody())
        .build()
    newCall(request).execute().use { response -> return response.body?.string() }
}

fun OkHttpClient.post(url: String, content: String): String? {
    val request: Request = Request.Builder()
        .url(url)
        .post(content.toRequestBody())
        .build()
    newCall(request).execute().use { response -> return response.body?.string() }
}

fun OkHttpClient.delete(
    url: String,
    content: String? = null,
    params: Map<String, String?>? = null
): String? {
    val httpUrl = url.toHttpUrl().newBuilder().apply {
        params?.forEach { (k, v) ->
            addQueryParameter(k, v)
        }
    }.build()
    val request: Request = Request.Builder()
        .url(httpUrl)
        .delete(content?.toRequestBody())
        .build()
    newCall(request).execute().use { response -> return response.body?.string() }
}