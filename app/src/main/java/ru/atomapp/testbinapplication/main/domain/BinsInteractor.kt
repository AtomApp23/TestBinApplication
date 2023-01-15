package ru.atomapp.testbinapplication.main.domain

interface BinsInteractor {

    suspend fun init(): BinsResult

    suspend fun infoAboutBinNumber(info: String): BinsResult

    class Base(
        private val repository: BinsRepository,
        private val handleError: HandleError<String>
    ) : BinsInteractor {

        override suspend fun init(): BinsResult {
            return BinsResult.Success(repository.allBinsInfo())
        }

        override suspend fun infoAboutBinNumber(info: String): BinsResult {
            return try {
                repository.binInfo(info)
                BinsResult.Success(repository.allBinsInfo())
            } catch (e: Exception) {
                BinsResult.Failure(handleError.handle(e))
            }
        }
    }
}