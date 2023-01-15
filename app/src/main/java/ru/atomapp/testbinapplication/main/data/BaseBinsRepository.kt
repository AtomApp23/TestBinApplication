package ru.atomapp.testbinapplication.main.data

import ru.atomapp.testbinapplication.main.domain.Bin
import ru.atomapp.testbinapplication.main.domain.BinsRepository
import ru.atomapp.testbinapplication.main.domain.HandleError

class BaseBinsRepository(
    private val cloudDataSource: BinsCloudDataSource,
    private val cacheDataSource: BinsCacheDataSource,
    private val mapperToDomain: BinData.Mapper<Bin>,
    private val handleError: HandleError<Exception>
) : BinsRepository {

    override suspend fun allBinsInfo(): List<Bin> {
        val data = cacheDataSource.allBinsInfo()
        return data.map { it.map(mapperToDomain) }
    }

    override suspend fun binInfo(bin: String) = try {
        val dataSource = if (cacheDataSource.contains(bin))
            cacheDataSource
        else
            cloudDataSource
        val result = dataSource.binInfo(bin)
        cacheDataSource.saveBinInfo(result)
        result.map(mapperToDomain)
    } catch (e: Exception) {
        throw handleError.handle(e)
    }
}
