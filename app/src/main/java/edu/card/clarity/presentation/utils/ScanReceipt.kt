package edu.card.clarity.presentation.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import edu.card.clarity.enums.PurchaseType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import java.io.File
import org.json.JSONObject
import java.io.FileNotFoundException

fun Context.imageToBase64(fileName: String): String {
    val file = File(fileName)
    if (!file.exists()) {
        throw FileNotFoundException("File not found: $fileName")
    }
    val bytes = file.readBytes()

    // for emulator testing purposes, this calls the scanner with a receipt image
//    val bytes = assets.open("receipt_1.jpg").readBytes()
    val base = Base64.encodeToString(bytes, Base64.NO_WRAP)
    return String.format("data:image/jpeg;base64,%s", base)
}

suspend fun scanReceipt(context: Context, path: String): Response {
    return withContext(Dispatchers.IO) {
        val clientId = "vrfVPmJinsUPSkByAN3i0fYRlLHDinZwr7LFvlR"
        val apiKey = "elxctrowave:bf1f8c156cbbd85ce46386853dedead5"
        val client = OkHttpClient()
        val base64Image = context.imageToBase64(path)

        val mediaTypeJson = "application/json; charset=utf-8".toMediaTypeOrNull()
        val categories = JSONArray(PurchaseType.toList())
        val jsonBody = JSONObject()
            .put("file_data", base64Image)
            .put("categories", categories)
            .toString()

        Log.d("json", jsonBody.substring(0, 1000))

        val body = RequestBody.create(mediaTypeJson, jsonBody)

        val request = Request.Builder()
            .url("https://api.veryfi.com/api/v8/partner/documents")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("CLIENT-ID", clientId)
            .addHeader("AUTHORIZATION", "apikey $apiKey")
            .build()

        client.newCall(request).execute()
    }
}