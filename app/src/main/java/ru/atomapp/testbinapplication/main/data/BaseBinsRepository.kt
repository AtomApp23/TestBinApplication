package ru.atomapp.testbinapplication.main.data

import ru.atomapp.testbinapplication.main.domain.Bin
import ru.atomapp.testbinapplication.main.domain.BinsRepository

class BaseBinsRepository(
    private val cloudDataSource: BinsCloudDataSource,
    private val cacheDataSource:BinsCacheDataSource
): BinsRepository {

    override suspend fun allBinsInfo(): List<Bin> {
        TODO("Not yet implemented")
    }

    override suspend fun binInfo(bin: String): Bin {
        TODO("Not yet implemented")
    }
}