package ru.atomapp.testbinapplication.main.data

data class BinData(
    private val id: String,
    private val info: String
) {

    interface Mapper<T> {
        fun map(id: String, info: String): T

        class Matches(private val id: String) : Mapper<Boolean> {
            override fun map(id: String, info: String): Boolean {
                return this.id == id
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, info)
}


