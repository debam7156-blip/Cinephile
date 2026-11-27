package com.cinephile.app.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/** Formatting and small parsing helpers used across UI and data layers. */
object Formatters {

    const val IMAGE_BASE = "https://image.tmdb.org/t/p/"

    fun posterUrl(path: String?, width: String = "w342"): String? =
        path?.takeIf { it.isNotEmpty() }?.let { IMAGE_BASE + width + it }

    fun backdropUrl(path: String?, width: String = "w780"): String? =
        path?.takeIf { it.isNotEmpty() }?.let { IMAGE_BASE + width + it }

    fun profileUrl(path: String?): String? = posterUrl(path, "w185")

    /** "2019-07-26" -> "2019" (empty string when unknown). */
    fun yearOf(date: String?): String =
        date?.takeIf { it.length >= 4 }?.substring(0, 4)?.takeIf { it.all { c -> c.isDigit() } } ?: ""

    /** "2019-07-26" -> "26 Jul 2019" (returns raw value if unparseable). */
    fun formatDate(date: String?): String {
        if (date.isNullOrBlank() || date.length < 10) return date ?: ""
        return try {
            val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
            SimpleDateFormat("d MMM yyyy", Locale.US).format(parsed ?: Date())
        } catch (e: Exception) {
            date
        }
    }

    /** 134 -> "2h 14m" */
    fun formatRuntime(minutes: Int?): String {
        if (minutes == null || minutes <= 0) return "—"
        val h = minutes / 60
        val m = minutes % 60
        return when {
            h == 0 -> "${m}m"
            m == 0 -> "${h}h"
            else -> "${h}h ${m}m"
        }
    }

    /** 4823 minutes -> "80h 23m" */
    fun formatTotalWatchTime(minutes: Long): String {
        val h = minutes / 60
        val m = minutes % 60
        return when {
            h == 0 -> "${m}m"
            else -> "${h}h ${m}m"
        }
    }

    /** 6.7 -> "6.7" ; 6.0 -> "6.0" */
    fun rating(vote: Double?): String =
        if (vote == null) "—" else String.format(Locale.US, "%.1f", vote)

    fun epochToDateString(epochMillis: Long?): String {
        if (epochMillis == null) return ""
        return SimpleDateFormat("d MMM yyyy", Locale.US).format(Date(epochMillis))
    }

    fun minutesBetween(startMillis: Long, endMillis: Long): Long =
        TimeUnit.MILLISECONDS.toMinutes(endMillis - startMillis)
}

/**
 * Reads CSV entries from a zip archive (Letterboxd exports are zips containing
 * several CSV files). Returns the entry name and its decoded text for each
 * CSV found.
 */
object ZipCsvReader {

    data class CsvFile(val name: String, val content: String)

    fun readCsvFiles(bytes: ByteArray, maxEntries: Int = 12): List<CsvFile> {
        val files = mutableListOf<CsvFile>()
        ZipInputStream(bytes.inputStream()).use { zip ->
            var entry: ZipEntry? = zip.nextEntry
            var guard = 0
            while (entry != null && guard < maxEntries) {
                val name = entry.name
                if (!entry.isDirectory && name.endsWith(".csv", ignoreCase = true)) {
                    val text = zip.readBytes().toString(Charsets.UTF_8)
                    if (text.isNotBlank()) files.add(CsvFile(name, text))
                }
                zip.closeEntry()
                entry = zip.nextEntry
                guard++
            }
        }
        return files
    }
}
