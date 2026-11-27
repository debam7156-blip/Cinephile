package com.cinephile.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.ui.home.HomeScreen
import com.cinephile.app.ui.home.HomeViewModel
import com.cinephile.app.ui.home.HomeViewModelFactory

@Composable
fun HomeRoute(onMediaClick: (MediaItem) -> Unit = {}) {
    val vm: HomeViewModel = viewModel(factory = remember { HomeViewModelFactory() })
    val state = vm.state.collectAsState().value
    HomeScreen(state = state, onRetry = { vm.refresh() }, onMediaClick = onMediaClick)
}
