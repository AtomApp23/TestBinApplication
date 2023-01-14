package ru.atomapp.testbinapplication.main.presentaion

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface BinsCommunications: ObserveBins {

    fun showProgress(show: Boolean)

    fun showState(uiState: UiState)

    fun showList(list: List<BinUi>)

    class Base(
        private val progressCommunication: ProgressCommunication,
        private val binsStateCommunication: BinsStateCommunication,
        private val binsListCommunication: BinsListCommunication
    ) : BinsCommunications {
        override fun showProgress(show: Boolean) {
            progressCommunication.map(show)
        }

        override fun showState(state: UiState) {
            binsStateCommunication.map(state)
        }

        override fun showList(list: List<BinUi>) {
            binsListCommunication.map(list)
        }

        override fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) {
            progressCommunication.observe(owner, observer)
        }

        override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) {
            binsStateCommunication.observe(owner, observer)
        }

        override fun observeBinsList(owner: LifecycleOwner, observer: Observer<List<BinUi>>) {
            binsListCommunication.observe(owner, observer)
        }

    }
}

interface ObserveBins {
    fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>)
    fun observeState(owner: LifecycleOwner, observer: Observer<UiState>)
    fun observeBinsList(owner: LifecycleOwner, observer: Observer<List<BinUi>>)
}

interface ProgressCommunication : Communication.Mutable<Boolean> {
    class Base : Communication.Post<Boolean>(), ProgressCommunication
}

interface BinsStateCommunication : Communication.Mutable<UiState> {
    class Base : Communication.Post<UiState>(), BinsStateCommunication
}

interface BinsListCommunication : Communication.Mutable<List<BinUi>> {
    class Base : Communication.Post<List<BinUi>>(), BinsListCommunication
}