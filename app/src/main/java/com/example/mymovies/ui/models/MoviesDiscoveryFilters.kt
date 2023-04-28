package com.example.mymovies.ui.models

import java.time.LocalDate
import java.util.Locale

data class MoviesDiscoveryFilters(
	var releaseYear: Int = LocalDate.now().year,
	var region: String = Locale.getDefault().country,
	var language: String = Locale.getDefault().language,
	var sortBy: String = "release_date.asc",
	var page: Int = 1
)
