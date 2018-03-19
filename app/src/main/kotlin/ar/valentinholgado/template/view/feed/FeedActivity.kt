package ar.valentinholgado.template.view.feed

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import ar.valentinholgado.template.R
import ar.valentinholgado.template.databinding.ActivityFeedBinding
import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveActivity
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FeedActivity : ReactiveActivity<FeedUiModel, Event>() {

    @Inject lateinit var layoutManager: RecyclerView.LayoutManager
    @Inject lateinit var adapter: FeedAdapter
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed)
        binding.feedList.layoutManager = layoutManager
        binding.feedList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        connectOutput()
    }

    private fun connectOutput() {

        binding.feedSearchView.queryTextChanges()
                .filter { query -> !query.isNullOrEmpty() }
                .map { query -> SearchEvent(query = query.toString()) }
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(outputStream)

        binding.feedList.scrollEvents()
                .filter { event -> !event.view().canScrollVertically(1) }
                .map { _ -> SearchEvent(query = binding.feedSearchView.query.toString()) }
                .subscribe(outputStream)

        disposables.add(
                adapter.outputStream()
                        .subscribe { event ->
                            // Timber.d("Navigating to ${event.cardContent.intentUri}")
                            navigateTo(event.cardContent.intentUri)
                        })
    }

    private fun navigateTo(intentUri: String?) {
        intentUri?.let {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
    }

    override val successHandler = { model: FeedUiModel ->
        model.contentList?.let {
            adapter.updateList(it)
        }

        model.errorMessage?.let {
            Snackbar.make(binding.feedContainer, it, Snackbar.LENGTH_LONG).show()
        }

        binding.model = model
    }

    override val errorHandler = { _: Throwable ->
        Snackbar.make(binding.feedContainer, "An unexpected error occurred", Snackbar.LENGTH_LONG).show()
    }
}
