package ru.atomapp.testbinapplication.main.data

interface FetchBinInfo {
    suspend fun binInfo(bin: String): BinData
}