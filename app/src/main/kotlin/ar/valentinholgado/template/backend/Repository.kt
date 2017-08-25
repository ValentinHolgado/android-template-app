package ar.valentinholgado.template.backend

import ar.valentinholgado.template.backend.artsy.ArtsyRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 * Facade for different services with the same interface:
 * An input stream of Actions and an output stream of Results.
 */
class Repository(private val inputStream: Subject<Action> = BehaviorSubject.create(),
                 private val outputStream: Subject<Result> = BehaviorSubject.create(),
                 val artsyRepository: ArtsyRepository) {

    init {
        inputStream
                .compose(artsyRepository.search)
                .subscribe(outputStream)

        inputStream
                .compose(artsyRepository.artist)
                .subscribe(outputStream)

        inputStream
                .compose(artsyRepository.show)
                .subscribe(outputStream)
    }

    fun inputStream(): Observer<Action> {
        return inputStream
    }

    fun outputStream(): Observable<Result> {
        return outputStream
    }
}