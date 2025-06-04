package com.example.FishyFinder.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.FishyFinder.data.HistoryRepository
import com.example.FishyFinder.data.lokal.HistoryItem
import com.example.FishyFinder.utils.Resource
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {

    // LiveData untuk status pembuatan HistoryItem
    private val _createLocalStatus = MutableLiveData<Resource<Unit>>() // Status create
    val createLocalStatus: LiveData<Resource<Unit>> = _createLocalStatus

    // Fungsi untuk menyimpan HistoryItem ke database
    fun createHistoryItem(historyItem: HistoryItem) {
        viewModelScope.launch {
            try {
                _createLocalStatus.value = Resource.Loading()

                // Menyimpan HistoryItem ke Room
                historyRepository.insertHistoryItem(historyItem)

                _createLocalStatus.postValue(Resource.Success(Unit))
            } catch (e: Exception) {
                _createLocalStatus.postValue(Resource.Error("Unknown error: ${e.message}"))
            }
        }
    }

    // LiveData untuk mengambil data riwayat dari database
    private val _dataLocal = MutableLiveData<Resource<List<HistoryItem>>>()
    val dataLocal: LiveData<Resource<List<HistoryItem>>> get() = _dataLocal

    // Fungsi untuk mengambil semua HistoryItem dari Room
    fun getHistoryItemsLocal(forceRefresh: Boolean = false) {
        if (_dataLocal.value == null || forceRefresh) {
            viewModelScope.launch {
                try {
                    _dataLocal.value = Resource.Loading()

                    // Mengambil data HistoryItem dari Room
                    historyRepository.getAllHistoryItems().collect { historyItems ->
                        // Sekarang historyItems adalah List<HistoryItem>, kita bisa memeriksa isEmpty()
                        if (historyItems.isEmpty()) {
                            _dataLocal.postValue(Resource.Empty("No history found"))
                        } else {
                            _dataLocal.postValue(Resource.Success(historyItems))
                        }
                    }
                } catch (e: Exception) {
                    _dataLocal.postValue(Resource.Error("Unknown error: ${e.message}"))
                }
            }
        }
    }
}
