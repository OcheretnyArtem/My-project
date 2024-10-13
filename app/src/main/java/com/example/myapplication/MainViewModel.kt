package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.BootRecord
import com.example.myapplication.data.BootRecordDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

interface MainViewModelContract {

    interface ViewModel {

        val viewState: StateFlow<ViewState>
    }

    data class ViewState(val displayedText: String)
}

class MainViewModel(private val dao: BootRecordDao) : ViewModel(), MainViewModelContract.ViewModel {

    init {
        collectBootRecords()
    }

    override val viewState = MutableStateFlow(emptyViewState())

    private fun emptyViewState(): MainViewModelContract.ViewState {
        return MainViewModelContract.ViewState(displayedText = "No boots detected" /*TODO Move to String resources*/)
    }

    private fun collectBootRecords() {
        viewModelScope.launch {
            dao.getAllBootRecords().collect { bootRecords ->
                viewState.update { it.copy(displayedText = bootRecords.toDisplayedText()) }
            }
        }
    }

    private fun List<BootRecord>.toDisplayedText(): String = if (isEmpty()) {
        "No boots detected" /*TODO Move to String resources*/
    } else {
        val events = map { it.bootTime.format() }
        events.joinToString("\n")
    }

}
