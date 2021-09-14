package com.movieDB.movies.repository.networking.data

data class MovieDetailResponse(
    val id: String?,
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection:BelongsToCollection,
    val budget:String?,
    val genres:List<Genres>?,
    val homepage:String?,
    val imdb_id:String?,
    val original_language:String?,
    val original_title:String?,
    val overview:String?,
    val popularity:String?,
    val poster_path:String?,
    val production_companies:List<ProductionCompanies>?,
    val production_countries:List<ProductionCountries>?,
    val release_date:String?,
    val revenue:String?,
    val runtime:String?,
    val spoken_languages:List<SpokenLanguages>?,
    val status:String?,
    val tagline:String?,
    val title:String?,
    val video:String?,
    val vote_average:String?,
    val vote_count:String?,
)

data class BelongsToCollection(
    val id: String?,
    val name: String?,
    val poster_path: String?,
    val backdrop_path: String?
)

data class ProductionCompanies(
    val id: String?,
    val logo_path: String?,
    val name: String?,
    val origin_country: String?
)

data class ProductionCountries(
    val iso_3166_1: String?,
    val name: String?
)

data class SpokenLanguages(
    val english_name: String?,
    val iso_639_1: String?,
    val name:String?
)

data class Genres(
    val id: String?,
    val name: String?
)