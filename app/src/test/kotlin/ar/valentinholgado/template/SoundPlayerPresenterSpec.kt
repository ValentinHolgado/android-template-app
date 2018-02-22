package ar.valentinholgado.template

import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.audio.AudioResult
import ar.valentinholgado.template.presenter.audio.SoundPlayerPresenter
import ar.valentinholgado.template.view.soundplayer.AudioContent
import ar.valentinholgado.template.view.soundplayer.AudioFileContent
import ar.valentinholgado.template.view.soundplayer.AudioUiModel
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(JUnitPlatform::class)
class SoundPlayerPresenterSpec : Spek({

    describe("a sound player presenter accumulator") {
        val accumulator = SoundPlayerPresenter.accumulator

        on("a successful result") {
            val audioResult = AudioResult(
                    Result.Status.SUCCESS,
                    "guitar_sample",
                    "/some/path/guitar_sample.wav")

            val audioUiModel = AudioUiModel(
                    AudioContent(""),
                    fileList = listOf(
                            AudioFileContent(
                                    "/some/path/guitar_sample.wav",
                                    "guitar_sample"),
                            AudioFileContent(
                                    "/some/path/other_sample.wav",
                                    "other_sample")))

            val resultingAudioModel = accumulator(audioUiModel, audioResult)

            it("should be playing") {
                assertTrue { resultingAudioModel.isPlaying }
            }

            it("should update selected file path") {
                assertEquals("/some/path/guitar_sample.wav", resultingAudioModel.selectedFilePath)
            }

            it("should set file from list to selected") {
                resultingAudioModel.fileList?.forEach {
                    if (it.path == "/some/path/guitar_sample.wav")
                        assertTrue { it.selected }
                    else
                        assertFalse { it.selected }
                }
            }
        }
    }

})