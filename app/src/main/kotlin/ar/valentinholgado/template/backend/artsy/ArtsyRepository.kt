package ar.valentinholgado.template.backend.artsy

import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.StreamCache
import ar.valentinholgado.template.backend.Result.Status.*
import ar.valentinholgado.template.backend.artsy.artwork.ArtworkAction
import ar.valentinholgado.template.backend.artsy.artwork.ArtworkResponse
import ar.valentinholgado.template.backend.artsy.artwork.ArtworkResult
import ar.valentinholgado.template.backend.artsy.artist.Artist
import ar.valentinholgado.template.backend.artsy.artist.ArtistAction
import ar.valentinholgado.template.backend.artsy.artist.ArtistResponse
import ar.valentinholgado.template.backend.artsy.artist.ArtistResult
import ar.valentinholgado.template.backend.artsy.search.SearchAction
import ar.valentinholgado.template.backend.artsy.search.SearchResponse
import ar.valentinholgado.template.backend.artsy.search.SearchResult
import ar.valentinholgado.template.backend.artsy.show.Show
import ar.valentinholgado.template.backend.artsy.show.ShowAction
import ar.valentinholgado.template.backend.artsy.show.ShowResponse
import ar.valentinholgado.template.backend.artsy.show.ShowResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class ArtsyRepository(private val artsyApi: ArtsyApi,
                      private val cache: StreamCache) {

    /** Artwork **/
    fun artwork(): Observable<ArtworkResponse> {
        // FIXME Remove hardcoded query key
        return cache.cacheObservable("page1",
                artsyApi.artworks()
                        .subscribeOn(io())
                        .cache())
    }

    val artwork = { actions: Observable<ArtworkAction> ->
        actions.flatMap { _ ->
            artwork()
                    .map { response ->
                        ArtworkResult(
                                status = SUCCESS,
                                successMessage = response.toString(),
                                body = response.embedded.artworks)
                    }
                    .onErrorReturn { error ->
                        ArtworkResult(
                                status = ERROR,
                                errorMessage = error.message)
                    }
                    .observeOn(mainThread())
                    .startWith(ArtworkResult(status = IN_FLIGHT))
        }
    }

    /** Search **/
    fun search(query: String): Observable<SearchResponse> {
        return cache.cacheObservable(query,
                artsyApi.search(query)
                        .subscribeOn(io())
                        .cache())
    }

    val search = { actions: Observable<Action> ->
        actions.filter { action -> action is SearchAction || action is ArtworkAction }
                .flatMap { action ->
                    when (action) {
                        is SearchAction -> search(action.query)
                                .map { (embedded) ->
                                    SearchResult(
                                            status = SUCCESS,
                                            body = embedded.results)
                                }
                                .onErrorReturn { error ->
                                    SearchResult(
                                            status = ERROR,
                                            errorMessage = error.message)
                                }
                                .observeOn(mainThread())
                                .startWith(SearchResult(status = IN_FLIGHT))
                        is ArtworkAction -> artwork()
                                .map { (embedded) ->
                                    ArtworkResult(
                                            status = SUCCESS,
                                            body = embedded.artworks)
                                }
                                .onErrorReturn { error ->
                                    ArtworkResult(
                                            status = ERROR,
                                            errorMessage = error.message)
                                }
                                .observeOn(mainThread())
                                .startWith(ArtworkResult(status = IN_FLIGHT))
                        is ArtistAction -> TODO()
                        else -> TODO()
                    }
                }
    }

    /** Artist **/
    fun artist(id: String): Observable<ArtistResponse> {
        return cache.cacheObservable(id,
                artsyApi.artist(id)
                        .subscribeOn(io())
                        .cache())
    }

    val artist = { actions: Observable<Action> ->
        actions.filter { action -> action is ArtistAction }
                .flatMap { action ->
                    when (action) {
                        is ArtistAction -> artist(id = action.id)
                                .map { response ->
                                    ArtistResult(
                                            body = Artist(
                                                    id = response.id,
                                                    name = response.name,
                                                    nationality = response.nationality,
                                                    location = response.location,
                                                    birthday = response.birthday,
                                                    imageUri = unwrapImageUri(response.links, response.imageVersions)),

                                            status = SUCCESS)
                                }
                                .onErrorReturn { error ->
                                    ArtistResult(errorMessage = error.message,
                                            status = ERROR)
                                }
                                .observeOn(mainThread())
                                .startWith(ArtistResult(status = IN_FLIGHT))
                        else -> TODO()
                    }


                }
    }

    /** Show **/
    fun show(id: String): Observable<ShowResponse> {
        return cache.cacheObservable(id,
                artsyApi.show(id)
                        .subscribeOn(io())
                        .cache())
    }

    val show = { actions: Observable<Action> ->
        actions.filter { action -> action is ShowAction }
                .flatMap { action ->
                    when (action) {
                        is ShowAction -> show(id = action.id)
                                .map { response ->
                                    ShowResult(body = Show(id = response.id,
                                            name = response.name,
                                            description = response.description,
                                            status = response.status),
                                            status = SUCCESS)
                                }
                                .onErrorReturn { error ->
                                    ShowResult(errorMessage = error.message,
                                            status = ERROR)
                                }
                                .observeOn(mainThread())
                                .startWith(ShowResult(status = IN_FLIGHT))
                        else -> TODO()
                    }
                }
    }

    fun unwrapImageUri(links: Links?, imageVersions: List<String>?): String? {
        imageVersions?.let {
            if (it.contains("large"))
                return links?.image?.url?.replace("{image_version}", "large")
        }
        return links?.thumbnail?.url
    }
}