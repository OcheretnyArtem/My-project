package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MainViewModelContract {

    interface ViewModel {

        val viewState: StateFlow<ViewState>
    }

    data class ViewState(val title: String)
}

class MainViewModel : ViewModel(), MainViewModelContract.ViewModel {

    override val viewState= MutableStateFlow(MainViewModelContract.ViewState("STATE FLOW"))
}
