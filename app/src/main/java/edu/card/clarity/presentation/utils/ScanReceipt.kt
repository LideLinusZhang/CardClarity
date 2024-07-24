package edu.card.clarity.presentation.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import org.json.JSONObject
import java.io.FileNotFoundException

fun Context.imageToBase64(fileName: String): String {
    val file = File(fileName)
    if (!file.exists()) {
        throw FileNotFoundException("File not found: $fileName")
    }
    val bytes = file.readBytes()
    val base = Base64.encodeToString(bytes, Base64.NO_WRAP)
    return String.format("data:image/jpeg;base64,%s", base)
}

suspend fun scanReceipt(context: Context, path: String) {
    try {
        val response = postDocument(context, path)
        Log.d("response", "response $response")

        if (response.isSuccessful) {
            // Get the response body as a string
            val responseBody = response.body?.string()

            if (responseBody != null) {
                // Parse the JSON string
                val jsonObject = JSONObject(responseBody)

                // Extract the "total" field
                val total = jsonObject.optDouble("total", 0.0)

                // Print the total
                Log.d("response","Total: $total")
            } else {
                Log.d("response","Response body is null")
            }
        } else {
            Log.d("response","Error: ${response.code}")
            Log.d("header","Error: ${response.headers}")
            Log.d("response","Error: ${response.message}")
            Log.d("req","Error: ${response.request}")
            val responseBody = response.body?.string() ?: ""
            val errorJson = JSONObject(responseBody)
            val status = errorJson.optString("status", "Unknown status")
            val error = errorJson.optString("error", "Unknown error")
            val details = errorJson.optJSONArray("details")?.toString() ?: "No details"
            Log.d("response","Error: ${status}")
            Log.d("response","Error: ${error}")
            Log.d("response","Error: ${details}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


suspend fun postDocument(context: Context, path: String): Response {
    return withContext(Dispatchers.IO) {
        val clientId = "vrfVPmJinsUPSkByAN3i0fYRlLHDinZwr7LFvlR"
        val apiKey = "elxctrowave:bf1f8c156cbbd85ce46386853dedead5"
        val client = OkHttpClient()
        val base64Image = context.imageToBase64(path)

        // MediaType for JSON
        val mediaTypeJson = "application/json; charset=utf-8".toMediaTypeOrNull()
        val jsonBody = JSONObject().put("file_data", base64Image).toString()

        Log.d("json", jsonBody.substring(0, 1000))

        // RequestBody
        val body = RequestBody.create(mediaTypeJson, jsonBody)

        // Request
        val request = Request.Builder()
            .url("https://api.veryfi.com/api/v8/partner/documents")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("CLIENT-ID", clientId)
            .addHeader("AUTHORIZATION", "apikey $apiKey")
            .build()

        // Execute Request
        client.newCall(request).execute()
    }
}