package ru.atomapp.testbinapplication.main.domain

interface BinsInteractor {

    suspend fun init(): BinsResult

    suspend fun infoAboutBinNumber(info: String): BinsResult


}