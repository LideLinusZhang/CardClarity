package edu.card.clarity.presentation.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import edu.card.clarity.enums.PurchaseType
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

class ReceiptScanner @Inject constructor(
    private val client: HttpClient
) {

    private fun imageToBase64(context: Context, fileName: String): String {
        val file = File(fileName)
        if (!file.exists()) {
            throw FileNotFoundException("File not found: $fileName")
        }
        val bytes = file.readBytes()

        // for emulator testing purposes, this calls the scanner with a receipt image
//        val bytes = context.assets.open("receipt_1.jpg").readBytes()
        val base = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return String.format("data:image/jpeg;base64,%s", base)
    }

    suspend fun scanReceipt(context: Context, path: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            val clientId = "VERYFI_CLIENT_ID"
            val apiKey = "VERYFI_API_KEY"
            val base64Image = imageToBase64(context, path)

            val categories = JSONArray(PurchaseType.toList())
            val jsonBody = JSONObject()
                .put("file_data", base64Image)
                .put("categories", categories)
                .toString()

            Log.d("json", jsonBody.substring(0, 1000))

            client.post("https://api.veryfi.com/api/v8/partner/documents") {
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                    append("CLIENT-ID", clientId)
                    append("AUTHORIZATION", "apikey $apiKey")
                }
                setBody(jsonBody)
            }
        }
    }
}
