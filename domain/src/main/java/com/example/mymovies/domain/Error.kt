package com.example.mymovies.domain


sealed interface Error {
    object NoInternetConnection : Error
    class RemoteWebService(val httpErrorCode: Int) : Error
    object LocalDatabase : Error
    object FavouriteMovieDatabase : Error
    class Unknown(val errorMessage: String) : Error
}
