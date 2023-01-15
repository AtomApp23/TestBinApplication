package ru.atomapp.testbinapplication.main.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.atomapp.testbinapplication.main.domain.Bin
import ru.atomapp.testbinapplication.main.domain.BinsRepository
import ru.atomapp.testbinapplication.main.domain.NoInternetConnectionException
import java.net.UnknownHostException

class BaseBinsRepositoryTest {

    private lateinit var repository: BinsRepository
    private lateinit var cloudDataSource: TestBinsCloudDataSource
    private lateinit var cacheDataSource: TestBinsCacheDataSource

    @Before
    fun setUp() {
        cloudDataSource = TestBinsCloudDataSource()
        cacheDataSource = TestBinsCacheDataSource()
        repository = BaseBinsRepository(cloudDataSource, cacheDataSource)
    }

    @Test
    fun `test all bins info`() = runBlocking {
        cacheDataSource.replaceData(
            listOf(
                BinData("12345678", "info test"),
                BinData("01234567", "info test 2")
            )
        )
        val actual = repository.allBinsInfo()
        val expected = listOf(
            Bin("12345678", "info test"),
            Bin("01234567", "info test 2")
        )
        actual.forEachIndexed { index, item ->
            assertEquals(expected[index], item)

        }
        assertEquals(1, cacheDataSource.allBinsInfoCalledCount)
    }

    @Test
    fun `test bin info not cached success`() = runBlocking {
        cloudDataSource.makeExpected(BinData("12345678", "info 12345678"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.binInfo("12345678")
        val expected = BinData("12345678", "info 12345678")

        assertEquals(expected, actual)
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(1, cloudDataSource.binInfoCAlledCount)
        //assertEquals("12345678", cacheDataSource.binInfoCalled[0])
        assertEquals(0, cacheDataSource.binInfoCalled.size)
        assertEquals(1, cacheDataSource.saveBinInfoCalledCount)
        assertEquals(expected, cacheDataSource.data[0])
    }

    @Test(expected = NoInternetConnectionException::class)
    fun `test bin info not cached failure`() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.binInfo("22222222")
        assertEquals(1, cloudDataSource.binInfoCAlledCount)
        assertEquals(0, cacheDataSource.binInfoCalled.size)
        assertEquals(0, cacheDataSource.saveBinInfoCalledCount)

    }

    @Test
    fun `test bin info cached`() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(BinData("22222222", "cloud info 22222222"))
        cacheDataSource.replaceData(listOf(BinData("22222222", "cache info 22222222")))

        val actual = repository.binInfo("22222222")
        val expected = BinData("22222222", "cache info 22222222")

        assertEquals(expected, actual)
        assertEquals(true, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cloudDataSource.binInfoCAlledCount)
        assertEquals(1, cacheDataSource.binInfoCalled.size)
        assertEquals(expected, cacheDataSource.binInfoCalled[0])
        assertEquals(1, cacheDataSource.saveBinInfoCalledCount)
    }


    private class TestBinsCloudDataSource : BinsCloudDataSource {

        private var thereIsConnection = true
        private var binData = BinData("", "")
        var binInfoCAlledCount = 0

        fun changeConnection(connected: Boolean) {
            thereIsConnection = connected
        }

        fun makeExpected(bin: BinData) {
            binData = bin
        }

        override suspend fun binInfo(bin: String): BinData {
            binInfoCAlledCount++
            if (thereIsConnection) {
                return binData
            } else {
                throw UnknownHostException()
            }
        }
    }

    private class TestBinsCacheDataSource : BinsCacheDataSource {

        var allBinsInfoCalledCount = 0
        var saveBinInfoCalledCount = 0
        var containsCalledList = mutableListOf<Boolean>()
        var binInfoCalled = mutableListOf<String>()
        val data = mutableListOf<BinData>()

        fun replaceData(newData: List<BinData>) {
            data.clear()
            data.addAll(newData)
        }

        override suspend fun allBinsInfo(): List<BinData> {
            allBinsInfoCalledCount++
            return data
        }

        override fun contains(bin:String) :Boolean{
            val result =  data.find { it.matches(bin) } != null
            containsCalledList.add(result)
            return result
        }

        override suspend fun binInfo(bin: String): BinData {
            binInfoCalled.add(bin)
            return data[0]
        }

        override suspend fun saveBinInfo(binData: BinData) {
            saveBinInfoCalledCount++
            data.add(binData)
        }

    }
}