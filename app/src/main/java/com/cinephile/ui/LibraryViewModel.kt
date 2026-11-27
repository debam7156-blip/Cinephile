package com.cinephile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cinephile.data.MovieEntry
import com.cinephile.data.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: MovieRepository) : ViewModel() {
    val movies: StateFlow<List<MovieEntry>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(entry: MovieEntry) = viewModelScope.launch {
        if (entry.id == 0L) repository.add(entry) else repository.update(entry)
    }
    fun remove(id: Long) = viewModelScope.launch { repository.remove(id) }

    companion object {
        fun factory(repository: MovieRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(LibraryViewModel::class.java))
                    return LibraryViewModel(repository) as T
                }
            }
    }
}
