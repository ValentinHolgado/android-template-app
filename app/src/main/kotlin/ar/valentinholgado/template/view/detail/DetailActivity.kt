package ar.valentinholgado.template.view.detail

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import ar.valentinholgado.template.R
import ar.valentinholgado.template.databinding.ActivityDetailBinding
import ar.valentinholgado.template.view.ReactiveActivity
import io.reactivex.Observer

class DetailActivity : ReactiveActivity<DetailUiModel, DetailEvent>() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
    }

    override fun onStart() {
        super.onStart()
        sendIntentParamsToOutput(intent, outputStream)
    }

    override val successHandler = { model: DetailUiModel ->
        binding.model = model
    }

    private fun sendIntentParamsToOutput(intent: Intent, outputStream: Observer<DetailEvent>) {
        // TODO Put in ReactiveActivity.
        intent.data?.let {
            val queryId = it.getQueryParameter("id")
            val queryType = it.getQueryParameter("type")

            if (queryId == null || queryType == null)
                return

            outputStream.onNext(DetailEvent(
                                        id = queryId,
                                        type = queryType))
        }
    }
}
