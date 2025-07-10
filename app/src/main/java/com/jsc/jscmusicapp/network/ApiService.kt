package com.jsc.jscmusicapp.network


import com.jsc.jscmusicapp.response.AlbumApiResponse
import com.jsc.jscmusicapp.response.ArtistSearchResponse
import com.jsc.jscmusicapp.response.SongResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface SaavnApiService {

    @Headers("Accept: */*")
    @GET("search/artists")
    suspend fun searchArtist(
        @Query("query") query: String,
        @Query("limit") limit: Int = 1,
        @Query("page") page: Int = 1
    ): ArtistSearchResponse

    @GET("artists/{id}/songs")
    suspend fun getSongsByArtist(
        @Path("id") artistId: String,
        @Header("Accept") accept: String = "*/*"
    ): SongResponse

    @GET("artists/{id}/albums")
    suspend fun getAlbumsByArtist(
        @Path("id") artistId: String,
        @Header("Accept") accept: String = "*/*"
    ): AlbumApiResponse

}
