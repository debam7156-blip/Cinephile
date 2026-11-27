package com.cinephile.data

import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: MovieDao) {
    fun observeAll(): Flow<List<MovieEntry>> = dao.observeAll()
    fun observeByStatus(status: String): Flow<List<MovieEntry>> = dao.observeByStatus(status)
    suspend fun add(entry: MovieEntry) { dao.insert(entry) }
    suspend fun update(entry: MovieEntry) { dao.update(entry.copy(updatedAt = System.currentTimeMillis())) }
    suspend fun remove(id: Long) { dao.delete(id) }
}
