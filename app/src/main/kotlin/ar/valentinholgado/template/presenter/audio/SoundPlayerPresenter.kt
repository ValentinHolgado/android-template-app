package ar.valentinholgado.template.presenter.audio

import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.audio.AudioRepository
import ar.valentinholgado.template.backend.audio.AudioResult
import ar.valentinholgado.template.backend.audio.PauseAction
import ar.valentinholgado.template.backend.audio.PlayAction
import ar.valentinholgado.template.backend.files.FilesRepository
import ar.valentinholgado.template.backend.files.FilesResult
import ar.valentinholgado.template.backend.files.ListFilesAction
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.soundplayer.AudioContent
import ar.valentinholgado.template.view.soundplayer.AudioFileContent
import ar.valentinholgado.template.view.soundplayer.AudioUiModel
import ar.valentinholgado.template.view.soundplayer.PauseEvent
import ar.valentinholgado.template.view.soundplayer.PlayEvent
import ar.valentinholgado.template.view.soundplayer.ReadyEvent
import ar.valentinholgado.template.view.soundplayer.SelectFileEvent
import ar.valentinholgado.template.view.soundplayer.SoundPlayerEvent
import io.reactivex.Observable
import timber.log.Timber

class SoundPlayerPresenter constructor(audioView: ReactiveView<AudioUiModel, SoundPlayerEvent>,
                                       audioRepository: AudioRepository,
                                       filesRepository: FilesRepository) {

    init {
        // Connect to the audio engine.
        val viewEventStream = audioView.outputStream()
                .compose(eventsToActions)
                .share()

        viewEventStream.subscribe(filesRepository.inputStream)
        viewEventStream.subscribe(audioRepository.inputStream)

        // Merge streams
        filesRepository.outputStream
                .mergeWith(audioRepository.outputStream)
                .compose(resultsToContent)
                .subscribe(audioView.inputStream())
    }

    companion object {
        // Event to action mapping
        val eventsToActions = { events: Observable<SoundPlayerEvent> ->
            events.map { event ->
                when (event) {
                    is PlayEvent -> PlayAction(event.path ?: "")
                    is PauseEvent -> PauseAction()
                    is ReadyEvent -> ListFilesAction()
                    is SelectFileEvent -> PlayAction(event.path)
                    else -> TODO("Can't handle ${event::class.simpleName}")
                }
            }
        }

        val resultsToContent = { results: Observable<Result> ->
            // TODO Remove mocked id
            results.scan(AudioUiModel(AudioContent(audioId = "mock id", title = "")), accumulator)
        }

        val accumulator = { state: AudioUiModel, result: Result ->
            Timber.d(result.toString() + state.toString())
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

        private fun handleAudioResult(result: AudioResult, state: AudioUiModel): AudioUiModel {
            return when (result.status) {

                Result.Status.SUCCESS -> state.copy(
                        isPlaying = true,
                        selectedFilePath = result.filepath,
                        fileList = updateSelectedInList(state.fileList, result.filepath),
                        // TODO Remove mocked ids
                        content = AudioContent(
                                audioId = "mocked id",
                                title = result.title))

                Result.Status.PLAYING -> state.copy(
                        isPlaying = true,
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
                        selectedFilePath = result.filepath,
                        fileList = updateSelectedInList(state.fileList, result.filepath),
                        content = state.content.copy(
                                title = result.title))

                Result.Status.FINISHED -> state.copy(
                        isPlaying = false,
                        selectedFilePath = result.filepath,
                        fileList = updateSelectedInList(state.fileList, result.filepath),
                        content = state.content.copy(
                                title = result.title))

                Result.Status.STOPPED -> state.copy(isPlaying = false)

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
}