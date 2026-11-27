package com.cinephile.app.data.repository

import com.cinephile.app.data.local.FakeTrackedMediaDao
import com.cinephile.app.domain.model.Genre
import com.cinephile.app.domain.model.MediaDetails
import com.cinephile.app.domain.model.MediaType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

private fun movie(id: Int = 1) = MediaDetails(
    id = id,
    type = MediaType.MOVIE,
    title = "Test Movie",
    tagline = "",
    overview = "Overview",
    status = "Released",
    runtimeMinutes = 120,
    posterPath = null,
    backdropPath = null,
    releaseDate = "2019-07-26",
    voteAverage = 7.5,
    voteCount = 100,
    genres = listOf(Genre(35, "Comedy")),
    cast = emptyList(),
    crew = emptyList(),
    trailerKey = null,
)

class TrackingRepositoryTest {

    private lateinit var dao: FakeTrackedMediaDao
    private lateinit var repo: TrackingRepository

    @Before
    fun setUp() {
        dao = FakeTrackedMediaDao()
        repo = TrackingRepository(dao, clock = { 1_000L })
    }

    @Test
    fun `markWatched creates one row and no duplicates`() = runTest {
        repo.markWatched(movie())
        repo.markWatched(movie())
        assertThat(dao.rows.value).hasSize(1)
        assertThat(dao.rows.value[0].watched).isTrue()
    }

    @Test
    fun `markWatched removes item from watchlist`() = runTest {
        repo.addToWatchlist(movie())
        repo.markWatched(movie())
        val row = dao.rows.value[0]
        assertThat(row.watched).isTrue()
        assertThat(row.onWatchlist).isFalse()
    }

    @Test
    fun `rating is clamped and editable`() = runTest {
        repo.setRating(movie(), 15)
        assertThat(dao.rows.value[0].rating).isEqualTo(10)
        repo.setRating(movie(), 7)
        assertThat(dao.rows.value[0].rating).isEqualTo(7)
    }

    @Test
    fun `addToWatchlist twice yields single entry`() = runTest {
        repo.addToWatchlist(movie())
        repo.addToWatchlist(movie())
        assertThat(dao.rows.value).hasSize(1)
        assertThat(dao.rows.value[0].onWatchlist).isTrue()
    }

    @Test
    fun `toggleFavourite flips and returns state`() = runTest {
        assertThat(repo.toggleFavourite(movie())).isTrue()
        assertThat(repo.toggleFavourite(movie())).isFalse()
        assertThat(dao.rows.value).hasSize(1)
    }

    @Test
    fun `watched date uses provided date`() = runTest {
        val date = LocalDate.of(2024, 1, 15)
        repo.markWatched(movie(), on = date)
        assertThat(dao.rows.value[0].watchedDateEpochDay).isEqualTo(date.toEpochDay())
    }

    @Test
    fun `remove deletes the row`() = runTest {
        repo.markWatched(movie())
        repo.remove(movie())
        assertThat(dao.rows.value).isEmpty()
    }

    @Test
    fun `revision increments on every write`() = runTest {
        repo.markWatched(movie())
        val r1 = dao.rows.value[0].revision
        repo.setRating(movie(), 8)
        assertThat(dao.rows.value[0].revision).isGreaterThan(r1)
    }
}
