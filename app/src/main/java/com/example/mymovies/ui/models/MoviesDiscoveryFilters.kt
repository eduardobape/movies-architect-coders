package com.example.mymovies.ui.models

import java.time.LocalDate
import java.util.Locale

data class MoviesDiscoveryFilters(
	var releaseYear: Int = LocalDate.now().year,
	var region: String = Locale.getDefault().country,
	var language: String = Locale.getDefault().language,
	var sortBy: String = SortOptionMoviesDiscovery.RELEASE_DATE_ASC.sortOption,
	var page: Int = 1
)

enum class SortOptionMoviesDiscovery(val sortOption: String) {
	POPULARITY_ASC("popularity.asc"),
	POPULARITY_DESC("popularity.desc"),
	RELEASE_DATE_ASC("release_date.asc"),
	RELEASE_DATE_DESC("release_date.desc"),
	REVENUE_ASC("revenue.asc"),
	REVENUE_DESC("revenue.desc"),
	PRIMARY_RELEASE_DATE_ASC("primary_release_date.asc"),
	PRIMARY_RELEASE_DATE_DESC("primary_release_date.desc"),
	ORIGINAL_TITLE_ASC("original_title.asc"),
	ORIGINAL_TITLE_DESC("original_title.desc"),
	VOTE_AVERAGE_ASC("vote_average.asc"),
	VOTE_AVERAGE_DESC("vote_average.desc"),
	VOTE_COUNT_ASC("vote_count.asc"),
	VOTE_COUNT_DESC("vote_count.desc")
}