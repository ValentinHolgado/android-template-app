package ar.valentinholgado.template.backend

import io.reactivex.Observable

import android.util.LruCache

class StreamCache {
    private val apiObservables = LruCache<String, Observable<*>>(50)

    fun <T> cacheObservable(query: String, observable: Observable<T>): Observable<T> {
        var cachedObservable: Observable<T>? = apiObservables.get(query) as Observable<T>?
        if (cachedObservable == null) {
            cachedObservable = observable
            updateCache(query, cachedObservable)
        }
        return cachedObservable
    }

    private fun <T> updateCache(query: String, observable: Observable<T>) {
        apiObservables.put(query, observable)
    }
}
