package ru.atomapp.testbinapplication.main.presentaion

import org.junit.Assert.*
import org.junit.Test

class BinsViewModelTest {

    /**
     * Initial test
     * At start fetch data and show it
     * then try to get some data successfully
     * then re-init and check the result
     */
    @Test
    fun test_init_and_re_init() {
        val communications = TestBinsCommunications()
        val interactor = TestBinsInteractor()
        // 1. initialize
        val viewModel = BinsViewModel(communications, interactor)
        interactor.changeExpectedResult(BinsResult.Success())

        // 2. action
        viewModel.init(isFirstRun = true)

        // 3. check
        // показываем прогресс
        assertEquals(true, communications.progressCalledList[0])
        assertEquals(1, communications.progressCalledList.size)

        // скрываем прогресс
        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Success(emptyList<BinInfo>()), communications.stateCalledList[0])

        assertEquals(0, communications.binsList.size)
        assertEquals(1, communications.timesShowList)

        // 4. get some data re-init
        interactor.changeExpectedResult(BinsResult.Failure("no internet connection"))
        viewModel.fetchBinData("12345678")
        assertEquals(3, communications.progressCalledList.size)
        assertEquals(true, communications.progressCalledList[2])

        assertEquals(1, interactor.infoAboutBinCalledList.size)

        assertEquals(4, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[3])

        assertEquals(2, communications.stateCalledList.size)
        assertEquals(UiState.Error("no internet connection"), communications.stateCalledList[1])

        assertEquals(1, communications.timesShowList)

        viewModel.init(isFirstRun = false)
        assertEquals(4, communications.progressCalledList.size)
        assertEquals(2, communications.stateCalledList.size)
        assertEquals(1, communications.timesShowList)

    }

    /**
     * Try to get info with empty input
     */
    @Test
    fun empty_input() {
        val communications = TestBinsCommunications()
        val interactor = TestBinsInteractor()

        val viewModel = BinsViewModel(communications, interactor)

        viewModel.fetchBinData("")
        assertEquals(0, communications.progressCalledList.size)

        // не трогаем интерактор, если ввод пустой
        assertEquals(0, interactor.infoAboutBinCalledList.size)

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Error("BIN number is empty"), communications.stateCalledList[0])

        assertEquals(0, communications.timesShowList)

    }

    /**
     * Try to get info with correct bin number
     */
    @Test
    fun correct_bin_input() {
        val communications = TestBinsCommunications()
        val interactor = TestBinsInteractor()

        val viewModel = BinsViewModel(communications, interactor)

        interactor.changeExpectedResult(BinsResult.Success(listOf(Bin("12345678", "info"))))
        viewModel.fetchBinData("12345678")
        // показываем прогресс
        assertEquals(1, communications.progressCalledList.size)
        assertEquals(true, communications.progressCalledList[0])

        assertEquals(1, interactor.infoAboutBinCalledList.size)
        assertEquals(Bin("12345678", "info"), interactor.infoAboutBinCalledList[0])
        // скрываем прогресс
        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Success(), communications.stateCalledList[0])

        assertEquals(1, communications.timesShowList)
        assertEquals(BinUi("12345678", "info"), communications.binsList[0])

    }

    private class TestBinsCommunications : BinsCommunications {

        // подсчитываем сколько раз был вызван метод показать прогресс
        val progressCalledList = mutableListOf<Boolean>()
        val stateCalledList = mutableListOf<Boolean>()
        var timesShowList = 0
        val binsList = mutableListOf<BinUi>()

        override fun showProgress(show: Boolean) {
            progressCalledList.add(show)
        }

        override fun showState(state: UiState) {
            stateCalledList.add(state)
        }

        override fun showList(list: List<BinUi>) {
            timesShowList++
            binsList.addAll(list)
        }

    }

    private class TestBinsInteractor : BinsInteractor {

        val initCalledList = mutableListOf<BinsResult>()
        val infoAboutBinCalledList = mutableListOf<BinsResult>()

        private var result: BinsResult = BinsResult.Success()

        fun changeExpectedResult(newResult: BinsResult) {
            result = newResult
        }

        override suspend fun init(): BinsResult {
            return result
        }

        override suspend fun infoAboutBinNumber(info: String): BinsResult {
            infoAboutBinCalledList.add(result)
            return result
        }

    }
}