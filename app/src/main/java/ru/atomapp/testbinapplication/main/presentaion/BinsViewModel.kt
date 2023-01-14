package ru.atomapp.testbinapplication.main.presentaion

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.atomapp.testbinapplication.R
import ru.atomapp.testbinapplication.main.domain.BinsInteractor
import ru.atomapp.testbinapplication.main.domain.BinsResult

class BinsViewModel(
    private val dispatchers:DispatchersList,
    private val manageResources: ManageResources,
    private val communications: BinsCommunications,
    private val interactor: BinsInteractor,
    private val binsResultMapper: BinsResult.Mapper<Unit>
) : ObserveBins, FetchBins, ViewModel() {

    override fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) {
        communications.observeProgress(owner, observer)
    }

    override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) {
        communications.observeState(owner, observer)
    }

    override fun observeBinsList(owner: LifecycleOwner, observer: Observer<List<BinUi>>) {
        communications.observeBinsList(owner, observer)
    }

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communications.showProgress(true)
            viewModelScope.launch(dispatchers.io()) {
                val result = interactor.init()
                communications.showProgress(false)
                result.map(binsResultMapper)
            }
        }
    }

    override fun fetchBinInfo(bin: String) {
        if (bin.isEmpty()) {
            communications.showState(UiState.Error(manageResources.string(R.string.empty_bin_error_massage)))
        } else {
            communications.showProgress(true)
            viewModelScope.launch(dispatchers.io()) {
                val result = interactor.infoAboutBinNumber(bin)
                communications.showProgress(false)
                result.map(binsResultMapper)
            }
        }
    }
}

interface FetchBins {
    fun init(isFirstRun: Boolean)
    fun fetchBinInfo(bin: String)
}

