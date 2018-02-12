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
        inputStream
                .filter { action ->
                    action is PlayAction
                            || action is PauseAction
                            || action is StopAction
                }
                .flatMap { action ->
                    when (action) {
                        is PlayAction ->
                            if (rxAudioPlayer.progress() > 0
                                    && action.filename == trackInfo) {
                                resumePlaying()
                            } else {
                                startPlaying(action)
                            }
                        is PauseAction -> {
                            rxAudioPlayer.pause()
                            Observable.just(AudioResult(Result.Status.ON_PAUSE,
                                    trackInfo,
                                    trackInfo))
                        }
                        is StopAction -> {
                            rxAudioPlayer.stopPlay()
                            trackInfo = null
                            Observable.just(AudioResult(Result.Status.STOPPED))
                        }
                        else -> TODO(action.toString())
                    }
                }
                .cache()
                .subscribe(outputStream)
    }

    private fun resumePlaying(): Observable<AudioResult>? {
        rxAudioPlayer.resume()
        return Observable.just(AudioResult(Result.Status.RESUMING))
                .mergeWith(ticks())
    }

    private fun startPlaying(action: PlayAction): Observable<AudioResult>? {
        return rxAudioPlayer.play(PlayConfig.url(action.filename).build())
                .map { _ ->
                    trackInfo = action.filename
                    AudioResult(Result.Status.SUCCESS,
                            title = action.filename,
                            filepath = action.filename)
                }
                .mergeWith(ticks())
                .onErrorReturn { error ->
                    AudioResult(errorMessage = error.message,
                            status = Result.Status.ERROR)
                }
                .doOnComplete { outputStream.onNext(AudioResult(Result.Status.FINISHED,
                        title = action.filename,
                        filepath = action.filename)) }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(AudioResult(Result.Status.STOPPED))
    }

    private fun resumePlaying(): Observable<AudioResult>? {
        rxAudioPlayer.resume()
        return Observable.just(AudioResult(Result.Status.RESUMING))
                .mergeWith(ticks())
    }

    private fun startPlaying(action: PlayAction): Observable<AudioResult>? {
        return rxAudioPlayer.play(PlayConfig.url(action.filename).build())
                .map { _ ->
                    trackInfo = action.filename
                    AudioResult(Result.Status.SUCCESS,
                            title = action.filename,
                            filepath = action.filename)
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

    private fun ticks(): Observable<AudioResult> {
        return Observable.interval(16, TimeUnit.MILLISECONDS)
                .map { _ ->
                    AudioResult(Result.Status.PLAYING,
                            progress = rxAudioPlayer.progress(),
                            title = trackInfo ?: "No track information",
                            filepath = trackInfo)
                }
                .takeWhile { _ ->
                    rxAudioPlayer.mediaPlayer != null
                            && rxAudioPlayer.mediaPlayer.isPlaying
                }
    }
}