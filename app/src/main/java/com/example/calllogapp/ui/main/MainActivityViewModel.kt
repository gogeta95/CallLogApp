package com.example.calllogapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calllogapp.repository.DataRepository
import com.example.calllogapp.repository.ServerInfoProvider

class MainActivityViewModel(
    private val serverInfoProvider: ServerInfoProvider,
    private val dataRepository: DataRepository
) : ViewModel() {

    private val uiLiveData =
        MutableLiveData(
            UIModel(
                startService = null,
                serverRunning = false,
                serverUrl = null,
                records = dataRepository.getCallRecords()
            )
        )

    fun observeUIModel(): LiveData<UIModel> = uiLiveData

    fun onServiceButtonClicked(serviceRunning: Boolean) {
        updateState {
            copy(
                startService = serviceRunning.not(),
                serverRunning = serviceRunning.not(),
                serverUrl = if (serviceRunning.not()) serverInfoProvider.getServerURl() else null
            )
        }
    }

    fun updateServiceStatus(serviceRunning: Boolean) {
        uiLiveData.value = UIModel(
            startService = null,
            serverRunning = serviceRunning,
            serverUrl = if (serviceRunning) serverInfoProvider.getServerURl() else null,
            records = dataRepository.getCallRecords()
        )
    }

    fun onResume() {
        updateState {
            copy(
                records = dataRepository.getCallRecords()
            )
        }
    }

    private fun updateState(block: UIModel.() -> UIModel) {
        uiLiveData.postValue(uiLiveData.value?.block())
    }

}