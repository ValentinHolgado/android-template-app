package ar.valentinholgado.template.backend.audio

import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.Result
import com.github.piasy.rxandroidaudio.PlayConfig
import com.github.piasy.rxandroidaudio.RxAudioPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit


class AudioRepository(val inputStream: Subject<Action> = BehaviorSubject.create(),
                      val outputStream: Subject<Result> = BehaviorSubject.create(),
                      private val rxAudioPlayer: RxAudioPlayer) {

    private var trackInfo: String? = null

    init {
        inputStream.flatMap { action ->
            when (action) {
                is PlayAction ->
                    if (rxAudioPlayer.progress() > 0) {
                        rxAudioPlayer.resume()
                        Observable.just(AudioResult(Result.Status.RESUMING))
                                .mergeWith(ticks())
                    } else {
                        rxAudioPlayer.play(PlayConfig.url(action.filename).build())
                                .map { _ ->
                                    trackInfo = action.filename
                                    AudioResult(Result.Status.SUCCESS, title = action.filename)
                                }
                                .mergeWith(ticks())
                                .onErrorReturn { error ->
                                    AudioResult(errorMessage = error.message,
                                            status = Result.Status.ERROR)
                                }
                                .doOnComplete { outputStream.onNext(AudioResult(Result.Status.FINISHED)) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .startWith(AudioResult(Result.Status.STOPPED))
                    }
                is PauseAction -> {
                    rxAudioPlayer.pause()
                    Observable.just(AudioResult(Result.Status.ON_PAUSE))
                }
                is StopAction -> {
                    rxAudioPlayer.stopPlay()
                    Observable.just(AudioResult(Result.Status.STOPPED))
                }
                else -> TODO()
            }
        }.subscribe(outputStream)
    }

    private fun ticks(): Observable<AudioResult> {
        return Observable.interval(16, TimeUnit.MILLISECONDS)
                .map { _ -> AudioResult(Result.Status.PLAYING,
                        progress = rxAudioPlayer.progress(),
                        title = trackInfo ?: "No track information") }
                .takeWhile { _ ->
                    rxAudioPlayer.mediaPlayer != null
                            && rxAudioPlayer.mediaPlayer.isPlaying
                }
    }
}