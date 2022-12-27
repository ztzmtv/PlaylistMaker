package com.azmetov.playlistmaker.network

sealed class Result {
    /**
    1хх — Information носит уведомительный характер и обычно не требует реакции.
    2xx — Success сигнализирует об успешном завершении операции.
    3хх — Redirection указывает на необходимость сделать запрос ресурса по другому адресу.
    4xx — Client error показывает, что запрос не может быть выполнен и содержит ошибку на стороне клиента.
    5хх — Server error показывает, что запрос не может быть выполнен и содержит ошибку на стороне сервера.
     **/
    //TODO(do not forget)

    object Information : Result()
    object Success : Result()
    object Redirection : Result()
    object ClientError : Result()
    object ServerError : Result()
}