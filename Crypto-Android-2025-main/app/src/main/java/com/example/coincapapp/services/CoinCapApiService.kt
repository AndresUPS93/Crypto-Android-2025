package com.example.coincapapp.services

import com.example.coincapapp.models.AssetsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class CoinCapApiService @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getAssets(): AssetsResponse {
        val response: HttpResponse = client.get(urlString = "https://2467c6da-aaf8-42cf-a8d7-ff02ba0276f1.mock.pstmn.io/v3/assets")
        return response.body()
    }
}