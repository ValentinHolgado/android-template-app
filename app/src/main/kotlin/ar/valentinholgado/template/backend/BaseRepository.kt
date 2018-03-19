package ar.valentinholgado.template.backend

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class BaseRepository<Input, Output>(
        protected val inputStream: Subject<Input> = BehaviorSubject.create(),
        protected val outputStream: Subject<Output> = BehaviorSubject.create()) {

    fun inputStream(): Observer<Input> {
        return inputStream
    }

    fun outputStream(): Observable<Output> {
        return outputStream
    }
}