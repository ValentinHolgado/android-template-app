package ar.valentinholgado.template.view

import io.reactivex.Observable
import io.reactivex.Observer

/**
 * A Reactive View consists of an input stream, which is an Observer of UI models 'M',
 * and an output stream, an observable of UI events 'E'.
 */
interface ReactiveView<M, E> {
    fun inputStream(): Observer<M>
    fun outputStream(): Observable<E>
}