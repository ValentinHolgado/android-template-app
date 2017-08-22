package ar.valentinholgado.template.view.detail

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import ar.valentinholgado.template.R
import ar.valentinholgado.template.databinding.ActivityDetailBinding
import ar.valentinholgado.template.presenter.detail.DetailPresenter
import ar.valentinholgado.template.view.*
import io.reactivex.Observer
import javax.inject.Inject

class DetailActivity : ReactiveActivity<DetailUiModel, DetailEvent>() {

    @Inject lateinit var detailPresenter: DetailPresenter

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
        intent.data?.let {
            outputStream.onNext(DetailEvent(id = it.getQueryParameter("id"),
                    type = it.getQueryParameter("type")))
        }
    }
}
