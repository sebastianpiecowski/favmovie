package util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import exception.ServiceCallException
import model.ApiErrorModel
import retrofit2.Response

fun okhttp3.ResponseBody.deserialize(): ApiErrorModel {
    return jacksonObjectMapper().readValue(this.string(), ApiErrorModel::class.java)
}

typealias ValidateCallback = ((ex: Exception) -> Unit)?

fun <T> Response<T>.validate(callback: ValidateCallback = null): T {
    if (!this.isSuccessful || this.body() == null) {
        val callException = ServiceCallException(this.errorBody()!!.deserialize())
        callback?.invoke(callException)
        throw callException
    } else {
        return this.body()!!
    }
}