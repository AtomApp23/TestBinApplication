package ru.atomapp.testbinapplication.main.domain

sealed class BinsResult {

    interface Mapper<T> {
        fun map(list:List<Bin>, errorMessage: String):T
    }

    abstract fun <T> map(mapper: Mapper<T>):T

    data class Success(private val list: List<Bin> = emptyList()): BinsResult() {
        override fun <T> map(mapper: Mapper<T>): T {
            return mapper.map(list, "")
        }

    }
    data class Failure(private val message: String): BinsResult() {
        override fun <T> map(mapper: Mapper<T>): T {
            return mapper.map(emptyList(), message)
        }

    }
}
