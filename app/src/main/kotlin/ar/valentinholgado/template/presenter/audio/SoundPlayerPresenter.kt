package ar.valentinholgado.template.presenter.audio

import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.audio.AudioRepository
import ar.valentinholgado.template.backend.audio.AudioResult
import ar.valentinholgado.template.backend.audio.PauseAction
import ar.valentinholgado.template.backend.audio.PlayAction
import ar.valentinholgado.template.backend.audio.StartRecordAction
import ar.valentinholgado.template.backend.audio.StopRecordAction
import ar.valentinholgado.template.backend.files.FilesAction
import ar.valentinholgado.template.backend.files.FilesRepository
import ar.valentinholgado.template.backend.files.FilesResult
import ar.valentinholgado.template.backend.files.ListFilesAction
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.soundplayer.AudioContent
import ar.valentinholgado.template.view.soundplayer.AudioFileContent
import ar.valentinholgado.template.view.soundplayer.AudioUiModel
import ar.valentinholgado.template.view.soundplayer.DestroyEvent
import ar.valentinholgado.template.view.soundplayer.PauseEvent
import ar.valentinholgado.template.view.soundplayer.PlayEvent
import ar.valentinholgado.template.view.soundplayer.ReadyEvent
import ar.valentinholgado.template.view.soundplayer.SelectFileEvent
import ar.valentinholgado.template.view.soundplayer.SoundPlayerEvent
import ar.valentinholgado.template.view.soundplayer.StartRecordEvent
import ar.valentinholgado.template.view.soundplayer.StopRecordEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class SoundPlayerPresenter constructor(audioView: ReactiveView<AudioUiModel, SoundPlayerEvent>,
                                       audioRepository: AudioRepository,
                                       filesRepository: FilesRepository) {

    private var subscription: Disposable? = null

    private val eventToActionMapper = { events: Observable<SoundPlayerEvent> ->
        events.map { event ->
            when (event) {
                is PlayEvent -> PlayAction(event.path ?: "")
                is PauseEvent -> {
                    if (event.recording) StopRecordAction()
                    else PauseAction()
                }
                is ReadyEvent -> ListFilesAction()
                is DestroyEvent -> {
                    subscription?.dispose()
                    object : Action() {}
                }
                is SelectFileEvent -> PlayAction(event.path)
                is StartRecordEvent -> StartRecordAction()
                is StopRecordEvent -> StopRecordAction()
                else -> TODO("Can't handle ${event::class.simpleName}")
            }
        }
    }

    private val accumulator = { state: AudioUiModel, result: Result ->
        // Timber.d(result.toString() + state.toString())
        when (result) {
            is AudioResult -> {
                handleAudioResult(result, state)
            }
            is FilesResult -> {
                state.copy(fileList = updateSelectedInList(result.copy().fileList.map { file ->
                    AudioFileContent(file.absolutePath, file.name)
                }, state.selectedFilePath))
            }
            else -> state
        }
    }

    private val resultsToContentMapper = { results: Observable<Result> ->
        // TODO Remove mocked id
        results.scan(AudioUiModel(AudioContent(audioId = "mock id")), accumulator)
    }

    init {
        // Connect to the audio engine.
        val viewEventStream = audioView.outputStream()
                .compose(eventToActionMapper)
                .share()

        viewEventStream
                .filter { action -> action is FilesAction }
                .map { action -> action as FilesAction }
                .subscribe(filesRepository.inputStream())

        viewEventStream.subscribe(audioRepository.inputStream())

        // Merge streams
        filesRepository.outputStream()
                .mergeWith(audioRepository.outputStream())
                .doOnSubscribe { disposable -> subscription = disposable }
                .compose(resultsToContentMapper)
                .subscribe(audioView.inputStream())
    }

    private fun handleAudioResult(result: AudioResult, state: AudioUiModel): AudioUiModel {
        return when (result.status) {

            Result.Status.SUCCESS -> state.copy(
                    isPlaying = true,
                    isRecording = false,
                    selectedFilePath = result.filepath,
                    fileList = updateSelectedInList(state.fileList, result.filepath),
                    // TODO Remove mocked ids
                    content = AudioContent(
                            audioId = "mocked id",
                            title = result.title))

            Result.Status.PLAYING -> state.copy(
                    isPlaying = true,
                    isRecording = false,
                    selectedFilePath = result.filepath,
                    fileList = updateSelectedInList(state.fileList, result.filepath),
                    content = state.content.copy(
                            title = result.title))

            Result.Status.RECORDING -> state.copy(
                    isPlaying = true,
                    isRecording = true,
                    selectedFilePath = result.filepath,
                    fileList = updateSelectedInList(state.fileList, result.filepath),
                    content = state.content.copy(
                            title = result.title))

            Result.Status.RESUMING -> state.copy(
                    isPlaying = true,
                    selectedFilePath = result.filepath,
                    fileList = updateSelectedInList(state.fileList, result.filepath))

            Result.Status.ON_PAUSE -> state.copy(
                    isPlaying = false,
                    isRecording = false,
                    selectedFilePath = result.filepath,
                    fileList = updateSelectedInList(state.fileList, result.filepath),
                    content = state.content.copy(
                            title = result.title))

            Result.Status.FINISHED -> state.copy(
                    isPlaying = false,
                    isRecording = false,
                    selectedFilePath = result.filepath,
                    fileList = updateSelectedInList(state.fileList, result.filepath),
                    content = state.content.copy(
                            title = result.title))

            Result.Status.STOPPED -> state.copy(isPlaying = false, isRecording = false)

            else -> state
        }
    }

    private fun updateSelectedInList(list: List<AudioFileContent>?, path: String?): List<AudioFileContent>? {
        return list?.map { file ->
            if (file.path == path) {
                file.copy(selected = true)
            } else {
                file.copy(selected = false)
            }
        }
    }
}