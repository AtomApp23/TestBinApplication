package ru.atomapp.testbinapplication.main.domain

import ru.atomapp.testbinapplication.R
import ru.atomapp.testbinapplication.main.presentaion.ManageResources

interface HandleError<T> {

    fun handle(e: Exception): T

    class Base(private val manageResources: ManageResources) : HandleError<String> {

        override fun handle(e: Exception): String {
            return manageResources.string(
                when (e) {
                    is NoInternetConnectionException -> R.string.no_connection_message
                    else -> R.string.service_unavalable_message
                }
            )
        }
    }
}