package com.cinephile.app.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cinephile.app.ui.components.ErrorState
import com.cinephile.app.ui.components.LoadingState

@Composable
fun DetailsScreen(
    state: DetailsState,
    onRetry: () -> Unit,
    onMarkWatched: () -> Unit,
    onToggleWatchlist: () -> Unit,
    onToggleFavourite: () -> Unit,
    onRate: (Int) -> Unit,
    onReview: (String) -> Unit,
) {
    when (state) {
        is DetailsState.Loading -> LoadingState(Modifier.fillMaxSize())
        is DetailsState.Failed -> ErrorState(message = state.message, onRetry = onRetry)
        is DetailsState.Ready -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                DetailsHeader(d = state.details)
                DetailsOverview(d = state.details)
                DetailsActions(
                    tracked = state.tracked,
                    onMarkWatched = onMarkWatched,
                    onToggleWatchlist = onToggleWatchlist,
                    onToggleFavourite = onToggleFavourite,
                )
                DetailsCast(d = state.details)
                DetailsCrew(d = state.details)
            }
        }
    }
}
