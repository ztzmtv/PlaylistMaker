package com.azmetov.playlistmaker.data.network

import android.util.Log
import com.azmetov.playlistmaker.other.Converter
import com.azmetov.playlistmaker.ui.SearchScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkDispatcher {

    /**
    1хх — Information носит уведомительный характер и обычно не требует реакции.
    2xx — Success сигнализирует об успешном завершении операции.
    3хх — Redirection указывает на необходимость сделать запрос ресурса по другому адресу.
    4xx — Client error показывает, что запрос не может быть выполнен и содержит ошибку на стороне клиента.
    5хх — Server error показывает, что запрос не может быть выполнен и содержит ошибку на стороне сервера.
     **/

    fun sendRequest(
        requestText: String,
        resultCallback: ((SearchScreenState) -> Unit)
    ) {
        val callback = object : Callback<PlaylistResponseDto> {
            override fun onResponse(
                call: Call<PlaylistResponseDto>,
                response: Response<PlaylistResponseDto>
            ) {
                when (response.code()) {
                    in 100..199 -> TODO()
                    in 200..299 -> responseSuccess(response)
                    in 300..399 -> TODO()
                    in 400..499 -> responseFailure()
                    in 500..599 -> responseFailure()
                }
                Log.d("TAG", "${response.body()}")
            }

            override fun onFailure(call: Call<PlaylistResponseDto>, t: Throwable) {
                responseFailure()
                Log.d("TAG", "${t.stackTrace}")
            }

            private fun responseSuccess(response: Response<PlaylistResponseDto>) {
                if (response.body()?.resultCount == 0)
                    invokeEmptyResult()
                else
                    invokeSuccessResult(response)
            }

            private fun responseFailure() {
                resultCallback(SearchScreenState.SearchError)
            }

            private fun invokeEmptyResult() {
                resultCallback(SearchScreenState.NotFound)
            }

            private fun invokeSuccessResult(response: Response<PlaylistResponseDto>) {
                response.body()?.results?.map { Converter.dtoToEntity(it) }?.apply {
                    resultCallback.invoke(SearchScreenState.Result(this))
                }
            }
        }
        ApiFactory.apiService.search(requestText).enqueue(callback)
    }
}