package ar.valentinholgado.template.inject

import android.content.Context
import ar.valentinholgado.template.backend.audio.AudioRepository
import com.github.piasy.rxandroidaudio.AudioRecorder
import com.github.piasy.rxandroidaudio.RxAudioPlayer
import dagger.Module
import dagger.Provides

@Module
class AudioBackendModule {

    @Provides
    @ApplicationScope
    fun provideRxAudioPlayer(): RxAudioPlayer {
        return RxAudioPlayer.getInstance()
    }

    @Provides
    @ApplicationScope
    fun provideAudioRecorder(): AudioRecorder {
        return AudioRecorder.getInstance()
    }

    @Provides
    @ApplicationScope
    fun provideAudioRepository(rxAudioPlayer: RxAudioPlayer,
                               audioRecorder: AudioRecorder,
                               context: Context): AudioRepository {
        return AudioRepository(rxAudioPlayer = rxAudioPlayer, audioRecorder = audioRecorder, context = context)
    }
}