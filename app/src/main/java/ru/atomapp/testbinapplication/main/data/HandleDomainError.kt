package ru.atomapp.testbinapplication.main.data

import ru.atomapp.testbinapplication.main.domain.HandleError
import ru.atomapp.testbinapplication.main.domain.NoInternetConnectionException
import ru.atomapp.testbinapplication.main.domain.ServiceUnavailableException
import java.net.UnknownHostException

class HandleDomainError : HandleError<Exception> {

    override fun handle(e: Exception): Exception {
        return when (e) {
            is UnknownHostException -> NoInternetConnectionException()
            else -> ServiceUnavailableException()
        }
    }
}