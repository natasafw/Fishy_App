package com.example.FishyFinder.data.lokal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageRes: Int, // Path gambar yang disimpan
    val namaIkan: String, // Nama Ikan
    val habitat: String,  // Habitat Ikan
    val date: String     // tanggal upload
)


