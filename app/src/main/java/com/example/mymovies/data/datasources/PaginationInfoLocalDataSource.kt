package com.example.mymovies.data.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.mymovies.data.local.models.PaginationInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PaginationInfoLocalDataSource(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val currentPagePopularMovies: Preferences.Key<Int> = intPreferencesKey("current_page_popular_movies")
        val totalPagesPopularMovies: Preferences.Key<Int> = intPreferencesKey("total_pages_popular_movies")
        val totalPopularMovies: Preferences.Key<Int> = intPreferencesKey("total_popular_movies")
    }

    val popularMoviesPaginationInfo: Flow<PaginationInfo> = dataStore.data
        .catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }
        .map { preferences ->
            PaginationInfo(
                preferences[PreferencesKeys.currentPagePopularMovies] ?: 0,
                preferences[PreferencesKeys.totalPagesPopularMovies] ?: 0,
                preferences[PreferencesKeys.totalPopularMovies] ?: 0
            )
        }

    suspend fun updatePopularMoviesPaginationInfo(paginationInfo: PaginationInfo) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.currentPagePopularMovies] = paginationInfo.currentPage
            preferences[PreferencesKeys.totalPagesPopularMovies] = paginationInfo.totalPages
            preferences[PreferencesKeys.totalPopularMovies] = paginationInfo.totalItems
        }
    }
}
