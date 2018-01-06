package ar.valentinholgado.template.backend.audio

import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.Result
import com.github.piasy.rxandroidaudio.PlayConfig
import com.github.piasy.rxandroidaudio.RxAudioPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import timber.log.Timber

class AudioRepository(val inputStream: Subject<Action> = BehaviorSubject.create(),
                      val outputStream: Subject<Result> = BehaviorSubject.create(),
                      private val rxAudioPlayer: RxAudioPlayer) {

    init {
        inputStream.flatMap { action ->
            when (action) {
                is PlayAction ->
                    play(action.filename)
                            .map { prepared ->
                                AudioResult(Result.Status.SUCCESS)
                            }
                            .onErrorReturn { error ->
                                AudioResult(errorMessage = error.message,
                                        status = Result.Status.ERROR)
                            }
                            .doOnComplete { outputStream.onNext(AudioResult(Result.Status.FINISHED)) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(AudioResult(Result.Status.IN_FLIGHT))
                is PauseAction -> {
                    rxAudioPlayer.pause()
                    Observable.just(AudioResult(Result.Status.ON_PAUSE))
                }
                else -> TODO()
            }
        }.subscribe(outputStream)
    }

    fun play(filename: String): Observable<Boolean> {
        return rxAudioPlayer.play(PlayConfig.url(filename).build())
    }
}