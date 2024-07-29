package edu.card.clarity.receiptParsing

import android.icu.text.SimpleDateFormat
import android.util.Base64
import android.util.Log
import edu.card.clarity.BuildConfig
import edu.card.clarity.enums.PurchaseType
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptParser @Inject constructor(
    private val client: HttpClient
) {
    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private suspend fun readImageAsBase64String(imagePath: String): String {
        val imageFile = File(imagePath)

        if (!imageFile.exists()) {
            throw FileNotFoundException("File not found: $imagePath")
        }

        val imageInBytes = withContext(Dispatchers.IO) {
            imageFile.readBytes()
        }
        val imageInBase64: String = Base64.encodeToString(imageInBytes, Base64.NO_WRAP)

        return String.format("data:image/jpeg;base64,%s", imageInBase64)
    }

    private suspend fun requestParsing(imageInBase64: String): HttpResponse {
        val categories = JSONArray(purchaseTypesInString)
        val jsonBody = JSONObject()
            .put("file_data", imageInBase64)
            .put("categories", categories)
            .toString()

        Log.d("json", jsonBody.substring(0, 1000))

        return withContext(Dispatchers.IO) {
            client.post(PARSE_RECEIPT_ENDPOINT) {
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                    append("CLIENT-ID", CLIENT_ID)
                    append("AUTHORIZATION", "apikey $API_KEY")
                }
                setBody(jsonBody)
            }
        }
    }

    suspend fun parseReceiptImage(imagePath: String): ReceiptParsingResult {
        val imageInBase64 = readImageAsBase64String(imagePath)
        val parsingResponse = requestParsing(imageInBase64)

        val responseJson = JSONObject(parsingResponse.bodyAsText())

        return ReceiptParsingResult(
            time = dateFormatter.parse(responseJson.optString("date")),
            total = responseJson.optString("total").toFloat(),
            merchant = responseJson.optJSONObject("vendor")?.optString("name") ?: "",
            purchaseType = PurchaseType.valueOf(responseJson.optString("category"))
        )
    }

    private companion object {
        private const val CLIENT_ID = BuildConfig.VERYFI_CLIENT_ID
        private const val API_KEY = BuildConfig.VERYFI_API_KEY
        private const val PARSE_RECEIPT_ENDPOINT: String =
            "https://api.veryfi.com/api/v8/partner/documents"

        private val purchaseTypesInString: List<String> = PurchaseType.entries.map { it.name }
    }
}
