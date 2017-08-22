package ar.valentinholgado.template.backend.artsy

import ar.valentinholgado.template.backend.artsy.artwork.ArtworkResponse
import ar.valentinholgado.template.backend.artsy.artist.ArtistResponse
import ar.valentinholgado.template.backend.artsy.search.SearchResponse
import ar.valentinholgado.template.backend.artsy.show.ShowResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * The Artsy API as a Retrofit interface.
 */
interface ArtsyApi {

    /**
     * Artwork endpoint. {@see ArtworkResponse}
     */
    @GET("artworks")
    fun artworks(): Observable<ArtworkResponse>

    /**
     * Search endpoint. {@see SearchResponse}
     */
    @GET("search")
    fun search(@Query("q") query: String): Observable<SearchResponse>

    /**
     * Artist endpoint. {@see ArtistResponse}
     */
    @GET("artists/{id}")
    fun artist(@Path("id") id: String): Observable<ArtistResponse>

    /**
     * Show endpoint. {@see ShowResponse}
     */
    @GET("shows/{id}")
    fun show(@Path("id") id: String): Observable<ShowResponse>
}