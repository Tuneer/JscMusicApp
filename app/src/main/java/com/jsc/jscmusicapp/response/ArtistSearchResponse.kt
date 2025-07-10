package com.jsc.jscmusicapp.response

data class ArtistSearchResponse(
    val success: Boolean,
    val data: ArtistSearchData
)

data class ArtistSearchData(
    val total: Int,
    val start: Int,
    val results: List<ArtistResult>
)

data class ArtistResult(
    val id: String,
    val name: String,
    val role: String,
    val image: List<ArtistImage>,
    val type: String,
    val url: String
)

data class ArtistImage(
    val quality: String,
    val url: String
)