package ar.valentinholgado.template.view.audio

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import ar.valentinholgado.template.R
import ar.valentinholgado.template.databinding.ActivityAudioBinding
import ar.valentinholgado.template.view.ReactiveActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observer

class AudioActivity : ReactiveActivity<AudioUiModel, AudioEvent>() {

    private lateinit var binding: ActivityAudioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio)
    }

    override fun onStart() {
        super.onStart()
        connectOutput()
    }

    private fun connectOutput() {
        binding.transportPlayPause.clicks()
                .map { _ -> TransportEvent("TRANSPORT_PLAY_PAUSE")}
                .subscribe(outputStream)
    }

    override val successHandler = { model: AudioUiModel ->
        binding.model = model
    }
}