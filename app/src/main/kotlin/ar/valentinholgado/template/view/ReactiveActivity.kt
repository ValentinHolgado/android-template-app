package ar.valentinholgado.template.view

import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

abstract class ReactiveActivity<M, E> : DaggerAppCompatActivity(), ReactiveView<M, E> {
    protected val outputStream: Subject<E> = BehaviorSubject.create()
    protected var inputStream: Subject<M> = BehaviorSubject.create()
    protected var disposables = ArrayList<Disposable>()

    @Inject
    @field:[Named("presenter")]
    lateinit var presenter: Any

    abstract val successHandler: (M) -> Unit

    open val errorHandler: (Throwable) -> Unit = { error: Throwable ->
        throw NotImplementedError("Error handler is not implemented yet. The received error is: ${error.message}")
    }

    /**
     * The observer input stream of UI models.
     */
    override fun inputStream(): Observer<M> {
        return inputStream
    }

    /**
     * The observable output stream of UI events.
     */
    override fun outputStream(): Observable<E> {
        return outputStream
    }

    /**
     * Calls subscribe to start receiving events from the stream.
     */
    override fun onStart() {
        super.onStart()
        subscribe()
    }

    /**
     * Calls dispose to dispose all listening subscribers.
     */
    override fun onStop() {
        super.onStop()
        dispose()
    }

    /**
     * Subscribes the input stream to the success and error handlers.
     */
    fun subscribe() {
        disposables.add(inputStream.subscribe(successHandler, errorHandler))
    }

    /**
     * Dispose all subscribers
     */
    fun dispose() {
        disposables.forEach { disposable -> if (!disposable.isDisposed) disposable.dispose() }
    }
}