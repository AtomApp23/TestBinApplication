package ru.atomapp.testbinapplication.main.domain

import ru.atomapp.testbinapplication.main.presentaion.BinUi

class BinUiMapper: Bin.Mapper<BinUi> {
    override fun map(id: String, info: String): BinUi {
        return BinUi(id, info)
    }
}