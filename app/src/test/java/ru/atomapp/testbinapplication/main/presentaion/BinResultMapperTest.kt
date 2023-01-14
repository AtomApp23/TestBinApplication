package ru.atomapp.testbinapplication.main.presentaion

import org.junit.Assert.*
import org.junit.Test
import ru.atomapp.testbinapplication.main.domain.Bin
import ru.atomapp.testbinapplication.main.domain.BinUiMapper

class BinResultMapperTest : BaseTest(){

    @Test
    fun test_error() {
        val communications = TestBinsCommunications()
        val mapper = BinResultMapper(communications, BinUiMapper())

        mapper.map(emptyList(), "not empty message")

        assertEquals(UiState.Error("not empty message"), communications.stateCalledList[0])
    }

    @Test
    fun test_success_no_list() {
        val communications = TestBinsCommunications()
        val mapper = BinResultMapper(communications, BinUiMapper())

        mapper.map(emptyList(), "")

        assertEquals(0, communications.timesShowList)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)

    }

    @Test
    fun test_success_with_list() {
        val communications = TestBinsCommunications()
        val mapper = BinResultMapper(communications, BinUiMapper())

        mapper.map(listOf(Bin("5", "info 5")), "")

        assertEquals(1, communications.timesShowList)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)
        assertEquals(BinUi("5", "info 5"), communications.binsList[0])
    }



}