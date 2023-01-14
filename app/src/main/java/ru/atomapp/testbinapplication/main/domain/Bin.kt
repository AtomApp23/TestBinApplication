package ru.atomapp.testbinapplication.main.domain

data class Bin(
    private val id: String,
    private val info: String) {

    interface Mapper<T> {
        fun map(id: String, info: String):T
    }

    fun <T> map(mapper: Mapper<T>):T = mapper.map(id, info)
}

