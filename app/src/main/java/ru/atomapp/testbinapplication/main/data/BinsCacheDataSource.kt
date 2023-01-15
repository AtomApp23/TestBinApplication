package ru.atomapp.testbinapplication.main.data

interface BinsCacheDataSource: FetchBinInfo {

    suspend fun allBinsInfo(): List<BinData>
    suspend fun contains(bin:String) :Boolean
    suspend fun saveBinInfo(binData: BinData)
}

