package com.cinephile.data

import org.junit.Assert.assertEquals
import org.junit.Test

class MovieEntryTest {
    @Test fun defaultsToWatchlistAndNoRating() {
        val entry = MovieEntry(title = "Arrival")
        assertEquals(MovieEntry.STATUS_WATCHLIST, entry.status)
        assertEquals(null, entry.rating)
    }

    @Test fun keepsWatchedRatingAndReview() {
        val entry = MovieEntry(title = "Arrival", releaseYear = 2016, status = MovieEntry.STATUS_WATCHED, rating = 4.5f, review = "Beautiful science fiction.", runtimeMinutes = 116)
        assertEquals(MovieEntry.STATUS_WATCHED, entry.status)
        assertEquals(4.5f, entry.rating!!, 0f)
        assertEquals("Beautiful science fiction.", entry.review)
    }
}
