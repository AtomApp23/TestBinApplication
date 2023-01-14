package ru.atomapp.testbinapplication.main.presentaion

import ru.atomapp.testbinapplication.main.domain.Bin
import ru.atomapp.testbinapplication.main.domain.BinsResult

class BinResultMapper(
    private val communications: BinsCommunications,
    private val mapper: Bin.Mapper<BinUi>
) : BinsResult.Mapper<Unit> {
    override fun map(list: List<Bin>, errorMessage: String) {

        communications.showState(
            if (errorMessage.isEmpty()) {
                if (list.isNotEmpty()) {
                    val binsList = list.map { it.map(mapper) }
                    communications.showList(binsList)
                }
                UiState.Success()
            } else
                UiState.Error(errorMessage)
        )
    }

}

