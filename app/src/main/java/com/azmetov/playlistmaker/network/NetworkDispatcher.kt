package com.azmetov.playlistmaker.network

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
        ApiFactory.apiService.search(requestText).enqueue(
            object : Callback<PlaylistResponseDto> {
                override fun onResponse(
                    call: Call<PlaylistResponseDto>,
                    response: Response<PlaylistResponseDto>
                ) {
                    when {
                        response.code() == 200 -> {
                            if (response.body()?.resultCount != 0) {
                                response.body()
                                    ?.results
                                    ?.map { Converter.dtoToEntity(it) }
                                    ?.apply {
                                        resultCallback.invoke(SearchScreenState.Result(this))
                                    }
                            } else {
                                resultCallback.invoke(SearchScreenState.NotFound)
                            }

                        }
                        response.code() in 400..599 -> {
                            resultCallback.invoke(SearchScreenState.SearchError)
                        }
                    }
                    Log.d("TAG", "${response.body()}")
                }

                override fun onFailure(call: Call<PlaylistResponseDto>, t: Throwable) {
                    resultCallback.invoke(SearchScreenState.SearchError)
                    Log.d("TAG", "${t.stackTrace}")
                }
            })
    }
}