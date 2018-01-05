package ar.valentinholgado.template.view.audio

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import ar.valentinholgado.template.R
import ar.valentinholgado.template.databinding.ActivityAudioBinding
import ar.valentinholgado.template.databinding.ActivityDetailBinding
import ar.valentinholgado.template.view.ReactiveActivity
import ar.valentinholgado.template.view.detail.DetailUiModel
import io.reactivex.Observer

class AudioActivity : ReactiveActivity<AudioUiModel, AudioEvent>() {

    private lateinit var binding: ActivityAudioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio)
    }

    override fun onStart() {
        super.onStart()
        sendIntentParamsToOutput(intent, outputStream)
    }

    override val successHandler = { model: AudioUiModel ->
        binding.model = model
    }

    private fun sendIntentParamsToOutput(intent: Intent, outputStream: Observer<AudioEvent>) {
        // TODO Put in ReactiveActivity.
        intent.data?.let {
            val queryId = it.getQueryParameter("id")
            val queryType = it.getQueryParameter("type")

            if (queryId == null || queryType == null)
                return

            outputStream.onNext(AudioEvent(
                    id = queryId,
                    type = queryType))
        }
    }
}