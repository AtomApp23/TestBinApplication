package ru.atomapp.testbinapplication.main.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BinsInteractorTest {

    private lateinit var interactor: BinsInteractor
    private lateinit var repository: TestBinsRepository

    @Before
    fun setUp() {
        repository = TestBinsRepository()
        interactor = BinsInteractor.Base(repository)

    }


    @Test
    fun `test init success`() = runBlocking {
        repository.changeExpectedList(listOf(Bin("12345678", "info")))
        val actual = interactor.init()
        val expected = BinsResult.Success(listOf(Bin("12345678", "info")))
        assertEquals(expected, actual)
        assertEquals(1, repository.allBinsInfoCalledCount)
    }

    @Test
    fun `test info about bin success`() = runBlocking {
        repository.changeExpectedInfoOfBin(Bin("23456789", "info2"))

        val actual = interactor.infoAboutBinNumber("1111111")
        val expected = BinsResult.Success(listOf(Bin("23456789", "info2")))
        assertEquals(expected, actual)
        assertEquals("1111111", repository.binInfoCalledList[0])
        assertEquals(1, repository.binInfoCalledList.size)

    }

    @Test
    fun `test info about number error`() = runBlocking {
        repository.expectingErrorGetInfo(true)
        val actual = interactor.infoAboutBinNumber("11111111")
        val expected = BinsResult.Failure("no internet connection")

        assertEquals(expected, actual)
        assertEquals("1111111", repository.binInfoCalledList[0])
        assertEquals(1, repository.binInfoCalledList.size)
    }

    private class TestBinsRepository: BinsRepository {

        private val allBinsInfoList = mutableListOf<Bin>()
        private var binInfo = Bin("", "")
        private var errorWhileBinInfo = false

        var allBinsInfoCalledCount = 0
        var binInfoCalledList = mutableListOf<String>()

        fun changeExpectedList(list:List<Bin>) {
            allBinsInfoList.clear()
            allBinsInfoList.addAll(list)
        }

        fun changeExpectedInfoOfBin(bin: Bin) {
            this.binInfo = bin
        }

        fun expectingErrorGetInfo(error: Boolean) {
            errorWhileBinInfo = error
        }

        override fun allBinsInfo(): List<Bin> {
            allBinsInfoCalledCount++
            return allBinsInfoList
        }

        override fun binInfo(bin: String): Bin {
            binInfoCalledList.add(bin)
            if(errorWhileBinInfo)
                throw NoInternetConnectionException()
            return binInfo
        }

    }
}