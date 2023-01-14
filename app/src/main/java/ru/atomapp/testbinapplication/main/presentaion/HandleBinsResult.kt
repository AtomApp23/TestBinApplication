package ru.atomapp.testbinapplication.main.presentaion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.atomapp.testbinapplication.main.domain.BinsResult

interface HandleBinsResult {

    fun handle (
        coroutineScope: CoroutineScope,
        block: suspend ()-> BinsResult
    )

    class Base(
        private val dispatchers: DispatchersList,
        private val communications: BinsCommunications,
        private val binsResultMapper: BinsResult.Mapper<Unit>
    ): HandleBinsResult {

        override fun handle(coroutineScope: CoroutineScope, block: suspend () -> BinsResult) {
            communications.showProgress(true)
            coroutineScope.launch(dispatchers.io()) {
                val result = block.invoke()
                communications.showProgress(false)
                result.map(binsResultMapper)
            }
        }

    }
}