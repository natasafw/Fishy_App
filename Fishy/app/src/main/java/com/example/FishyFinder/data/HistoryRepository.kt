package com.example.FishyFinder.data

import com.example.FishyFinder.data.lokal.HistoryItem
import com.example.FishyFinder.data.lokal.HistoryItemDao
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyItemDao: HistoryItemDao) {

    // Fungsi untuk menyimpan HistoryItem ke dalam database
    suspend fun insertHistoryItem(historyItem: HistoryItem) {
        historyItemDao.insertHistoryItem(historyItem)
    }

    // Fungsi untuk mengambil semua HistoryItem dari database
    fun getAllHistoryItems(): Flow<List<HistoryItem>> {
        return historyItemDao.getAllHistoryItems()
    }
}
