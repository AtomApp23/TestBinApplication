package ru.atomapp.testbinapplication.main.presentaion

import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.atomapp.testbinapplication.main.domain.Bin
import ru.atomapp.testbinapplication.main.domain.BinUiMapper
import ru.atomapp.testbinapplication.main.domain.BinsInteractor
import ru.atomapp.testbinapplication.main.domain.BinsResult

class BinsViewModelTest : BaseTest() {

    private lateinit var communications: TestBinsCommunications
    private lateinit var interactor: TestBinsInteractor
    private lateinit var manageResources: TestManageResources
    private lateinit var viewModel: BinsViewModel

    @Before
    fun init() {
        communications = TestBinsCommunications()
        interactor = TestBinsInteractor()
        manageResources = TestManageResources()
        // 1. initialize
        viewModel = BinsViewModel(
            HandleBinsResult.Base(
                TestDispatchersList(),
                communications,
                BinResultMapper(communications, BinUiMapper())
            ),
            manageResources,
            communications,
            interactor,

            )
    }

    /**
     * Initial test
     * At start fetch data and show it
     * then try to get some data successfully
     * then re-init and check the result
     */
    @Test
    fun test_init_and_re_init() = runBlocking {

        interactor.changeExpectedResult(BinsResult.Success())

        // 2. action
        viewModel.init(isFirstRun = true)

        // 3. check
        // показываем прогресс
        assertEquals(true, communications.progressCalledList[0])
        assertEquals(1, interactor.initCalledList.size)

        // скрываем прогресс
        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Success(), communications.stateCalledList[0])

        assertEquals(0, communications.binsList.size)
        assertEquals(0, communications.timesShowList)

        // 4. get some data re-init
        interactor.changeExpectedResult(BinsResult.Failure("no internet connection"))
        viewModel.fetchBinInfo("12345678")
        //assertEquals(4, communications.progressCalledList.size)
        assertEquals(true, communications.progressCalledList[2])





        assertEquals(1, interactor.infoAboutBinCalledList.size)

        assertEquals(4, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[3])

        assertEquals(2, communications.stateCalledList.size)
        assertEquals(UiState.Error("no internet connection"), communications.stateCalledList[1])

        assertEquals(0, communications.timesShowList)

        viewModel.init(isFirstRun = false)
        assertEquals(4, communications.progressCalledList.size)
        assertEquals(2, communications.stateCalledList.size)
        assertEquals(0, communications.timesShowList)

    }

    /**
     * Try to get info with empty input
     */
    @Test
    fun empty_input() = runBlocking {
        manageResources.makeExpectedAnswer("BIN number is empty")
        viewModel.fetchBinInfo("")

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
    fun correct_bin_input() = runBlocking {
        val bin = Bin("12345678", "info")

        interactor.changeExpectedResult(
            BinsResult.Success(
                listOf(bin)
            )
        )
        viewModel.fetchBinInfo("12345678")
        assertEquals(true, communications.progressCalledList[0])


        assertEquals(1, interactor.infoAboutBinCalledList.size)
        /*assertEquals(
            BinsResult.Success(listOf(bin)),
            interactor.infoAboutBinCalledList[0]
        )*/
        // скрываем прогресс
        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)

        assertEquals(1, communications.timesShowList)
        assertEquals(BinUi("12345678", "info"), communications.binsList[0])

    }

    private class TestManageResources : ManageResources {

        private var string: String = ""

        fun makeExpectedAnswer(expected: String) {
            string = expected
        }

        override fun string(id: Int): String {
            return string
        }

    }

    private class TestBinsInteractor : BinsInteractor {

        private var result: BinsResult = BinsResult.Success()

        val initCalledList = mutableListOf<BinsResult>()
        val infoAboutBinCalledList = mutableListOf<BinsResult>()


        fun changeExpectedResult(newResult: BinsResult) {
            result = newResult
        }

        override suspend fun init(): BinsResult {
            initCalledList.add(result)
            return result
        }

        override suspend fun infoAboutBinNumber(info: String): BinsResult {
            infoAboutBinCalledList.add(result)
            return result
        }

    }

    private class TestDispatchersList(private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()) :
        DispatchersList {
        override fun io(): CoroutineDispatcher {
            return dispatcher
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun ui(): CoroutineDispatcher {
            return dispatcher
        }
    }
}