package ru.atomapp.testbinapplication.main.data

import ru.atomapp.testbinapplication.main.domain.Bin

class BinDataToDomainMapper: BinData.Mapper<Bin> {

    override fun map(id: String, info: String): Bin {
        return Bin(id, info)
    }
}