package com.example.mymovies.ui.models

data class MoviesDiscoveryFilters(
	var releaseYear: Int,
	var region: String,
	var language: String,
	var sortBy: String,
	var page: Int
)
