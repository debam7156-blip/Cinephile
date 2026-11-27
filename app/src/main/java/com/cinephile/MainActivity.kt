package com.cinephile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cinephile.data.MovieEntry
import com.cinephile.ui.CinephileTheme
import com.cinephile.ui.LibraryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as CinephileApplication).movieRepository
        setContent {
            CinephileTheme {
                val vm: LibraryViewModel = viewModel(factory = LibraryViewModel.factory(repository))
                CinephileApp(vm)
            }
        }
    }
}

private enum class Tab(val label: String) { DISCOVER("Discover"), LIBRARY("My library"), STATS("Stats") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CinephileApp(vm: LibraryViewModel) {
    val movies by vm.movies.collectAsState()
    var tab by remember { mutableStateOf(Tab.DISCOVER) }
    var editing by remember { mutableStateOf<MovieEntry?>(null) }
    var editorOpen by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("CINEPHILE", fontWeight = FontWeight.Black) }, actions = { Text("FILM JOURNAL", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(end = 16.dp)) }) },
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { item ->
                    val icon = when (item) { Tab.DISCOVER -> Icons.Default.Explore; Tab.LIBRARY -> Icons.Default.CollectionsBookmark; Tab.STATS -> Icons.Default.BarChart }
                    NavigationBarItem(selected = tab == item, onClick = { tab = item }, icon = { Icon(icon, null) }, label = { Text(item.label) })
                }
            }
        },
        floatingActionButton = { if (tab != Tab.STATS) FloatingActionButton(onClick = { editing = null; editorOpen = true }) { Icon(Icons.Default.Add, "Add movie") } }
    ) { insets ->
        when (tab) {
            Tab.DISCOVER -> Discover(movies, Modifier.padding(insets)) { editing = null; editorOpen = true }
            Tab.LIBRARY -> Library(movies, Modifier.padding(insets), { editing = it; editorOpen = true }, vm::remove)
            Tab.STATS -> Statistics(movies, Modifier.padding(insets))
        }
    }
    if (editorOpen) MovieEditor(editing, { editorOpen = false }, { vm.save(it); editorOpen = false })
}

@Composable
private fun Discover(movies: List<MovieEntry>, modifier: Modifier, add: () -> Unit) {
    Column(modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Spacer(Modifier.height(8.dp))
        Text("Your next great watch starts here.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("A personal film journal, ready when you are.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        ElevatedCard(shape = RoundedCornerShape(22.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("DISCOVERY", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text("Online catalogue isn't connected yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Movie search, posters, and live trending titles require a configured catalogue provider. Your personal log works offline.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedButton(onClick = add) { Text("Add a movie to your log") }
            }
        }
        Text("YOUR JOURNEY", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        if (movies.isEmpty()) Empty("Your film journal begins here", "Add a movie you've watched or want to see. It will be saved on this device.")
        else Text("${movies.size} movies in your journal", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Library(movies: List<MovieEntry>, modifier: Modifier, edit: (MovieEntry) -> Unit, remove: (Long) -> Unit) {
    var filter by remember { mutableStateOf("ALL") }
    val filtered = movies.filter { filter == "ALL" || it.status == filter }
    Column(modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text("My library", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL", MovieEntry.STATUS_WATCHED, MovieEntry.STATUS_WATCHLIST).forEach { value ->
                FilterChip(filter == value, { filter = value }, label = { Text(when (value) { "ALL" -> "All"; MovieEntry.STATUS_WATCHED -> "Watched"; else -> "Watchlist" }) })
            }
        }
        if (filtered.isEmpty()) Empty("Nothing here yet", "Use + to add films to your watched log or watchlist.")
        else LazyColumn(contentPadding = PaddingValues(bottom = 100.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered, key = { it.id }) { movie ->
                ElevatedCard(onClick = { edit(movie) }, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(movie.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text(listOfNotNull(movie.releaseYear?.toString(), if (movie.status == MovieEntry.STATUS_WATCHED) "Watched" else "Watchlist").joinToString(" · "), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            movie.rating?.let { Text("★ ${"%.1f".format(it)} / 5", color = MaterialTheme.colorScheme.primary) }
                            if (movie.review.isNotBlank()) Text(movie.review, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        TextButton(onClick = { remove(movie.id) }) { Text("Remove") }
                    }
                }
            }
        }
    }
}

@Composable
private fun Statistics(movies: List<MovieEntry>, modifier: Modifier) {
    val watched = movies.filter { it.status == MovieEntry.STATUS_WATCHED }
    val ratings = watched.mapNotNull { it.rating }
    val minutes = watched.sumOf { it.runtimeMinutes ?: 0 }
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Your stats", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Calculated from your on-device film journal", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Stat("WATCHED", watched.size.toString(), Modifier.weight(1f))
            Stat("WATCH TIME", if (minutes == 0) "—" else "${minutes / 60}h ${minutes % 60}m", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Stat("AVG. RATING", if (ratings.isEmpty()) "—" else "%.1f / 5".format(ratings.average()), Modifier.weight(1f))
            Stat("WATCHLIST", movies.count { it.status == MovieEntry.STATUS_WATCHLIST }.toString(), Modifier.weight(1f))
        }
        Text("Add runtime and ratings while editing entries to enrich your totals.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Stat(label: String, value: String, modifier: Modifier = Modifier) {
    ElevatedCard(modifier) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    } }
}

@Composable
private fun Empty(title: String, body: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 30.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieEditor(initial: MovieEntry?, dismiss: () -> Unit, save: (MovieEntry) -> Unit) {
    var title by remember(initial) { mutableStateOf(initial?.title.orEmpty()) }
    var year by remember(initial) { mutableStateOf(initial?.releaseYear?.toString().orEmpty()) }
    var runtime by remember(initial) { mutableStateOf(initial?.runtimeMinutes?.toString().orEmpty()) }
    var rating by remember(initial) { mutableStateOf(initial?.rating?.toString().orEmpty()) }
    var review by remember(initial) { mutableStateOf(initial?.review.orEmpty()) }
    var watched by remember(initial) { mutableStateOf(initial?.status == MovieEntry.STATUS_WATCHED) }
    var error by remember { mutableStateOf<String?>(null) }
    AlertDialog(onDismissRequest = dismiss, title = { Text(if (initial == null) "Add to your journal" else "Edit movie") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(title, { title = it; error = null }, label = { Text("Title") }, singleLine = true)
            OutlinedTextField(year, { year = it.filter(Char::isDigit).take(4) }, label = { Text("Release year (optional)") }, singleLine = true)
            OutlinedTextField(runtime, { runtime = it.filter(Char::isDigit).take(4) }, label = { Text("Runtime in minutes (optional)") }, singleLine = true)
            OutlinedTextField(rating, { rating = it.filter { c -> c.isDigit() || c == '.' }.take(4) }, label = { Text("Rating, 0–5 (optional)") }, singleLine = true)
            OutlinedTextField(review, { review = it }, label = { Text("Review (optional)") }, minLines = 2)
            Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(watched, { watched = it }); Text("I've watched this") }
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }, confirmButton = { TextButton(onClick = {
        val cleanTitle = title.trim()
        val parsedYear = year.toIntOrNull()
        val parsedRuntime = runtime.toIntOrNull()
        val parsedRating = rating.toFloatOrNull()
        when {
            cleanTitle.isBlank() -> error = "Enter a title."
            year.isNotBlank() && (parsedYear == null || parsedYear !in 1888..2100) -> error = "Enter a valid release year."
            runtime.isNotBlank() && (parsedRuntime == null || parsedRuntime <= 0) -> error = "Runtime must be positive."
            rating.isNotBlank() && (parsedRating == null || parsedRating !in 0f..5f) -> error = "Rating must be from 0 to 5."
            else -> save(MovieEntry(id = initial?.id ?: 0, title = cleanTitle, releaseYear = parsedYear, status = if (watched) MovieEntry.STATUS_WATCHED else MovieEntry.STATUS_WATCHLIST, rating = parsedRating, review = review.trim(), runtimeMinutes = parsedRuntime))
        }
    }) { Text("Save") } }, dismissButton = { TextButton(onClick = dismiss) { Text("Cancel") } })
}
