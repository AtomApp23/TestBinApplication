package ru.atomapp.testbinapplication.main.domain

interface BinsRepository {

    suspend fun allBinsInfo(): List<Bin>
    suspend fun binInfo(bin: String): Bin
}