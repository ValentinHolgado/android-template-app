package ar.valentinholgado.template.backend.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.BaseRepository
import ar.valentinholgado.template.backend.Result
import com.github.piasy.rxandroidaudio.AudioRecorder
import com.github.piasy.rxandroidaudio.PlayConfig
import com.github.piasy.rxandroidaudio.RxAudioPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


class AudioRepository(private val rxAudioPlayer: RxAudioPlayer,
                      private val audioRecorder: AudioRecorder,
                      private val context: Context) : BaseRepository<Action, Result>() {

    private var trackInfo: String? = null

    init {
        inputStream
                .filter { action ->
                    action is PlayAction
                            || action is PauseAction
                            || action is StopAction
                            || action is StartRecordAction
                            || action is StopRecordAction
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
                                    File(trackInfo).name))
                        }
                        is StopAction -> {
                            rxAudioPlayer.stopPlay()
                            trackInfo = null
                            Observable.just(AudioResult(Result.Status.STOPPED))
                        }
                        is StartRecordAction -> {
                            audioRecorder.startRecord(
                                    MediaRecorder.AudioSource.DEFAULT,
                                    MediaRecorder.OutputFormat.MPEG_4,
                                    MediaRecorder.AudioEncoder.AAC,
                                    48000,
                                    24,
                                    File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString()
                                            + "/${ UUID.randomUUID() }.mp4"))
                            Observable.just(AudioResult(Result.Status.RECORDING))
                        }
                        is StopRecordAction -> {
                            audioRecorder.stopRecord()
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
                            title = File(action.filename).name,
                            filepath = action.filename)
                }
                .mergeWith(ticks())
                .onErrorReturn { error ->
                    AudioResult(errorMessage = error.message,
                            status = Result.Status.ERROR)
                }
                .doOnComplete {
                    outputStream.onNext(AudioResult(Result.Status.FINISHED,
                            title = File(action.filename).name,
                            filepath = action.filename))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(AudioResult(Result.Status.STOPPED))
    }

    private fun ticks(): Observable<AudioResult> {
        return Observable.interval(16, TimeUnit.MILLISECONDS)
                .map { _ ->
                    AudioResult(Result.Status.PLAYING,
                            progress = rxAudioPlayer.progress(),
                            title = File(trackInfo).name,
                            filepath = trackInfo)
                }
                .takeWhile { _ ->
                    rxAudioPlayer.mediaPlayer != null
                            && rxAudioPlayer.mediaPlayer.isPlaying
                }
    }
}