package ar.valentinholgado.template.view.tasks

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import ar.valentinholgado.template.R
import ar.valentinholgado.template.databinding.ActivityTasksBinding
import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveActivity
import ar.valentinholgado.template.view.common.CardEvent
import timber.log.Timber
import javax.inject.Inject

class TasksActivity : ReactiveActivity<TasksUiModel, Event>() {

    @Inject
    lateinit var layoutManager: RecyclerView.LayoutManager

    @Inject
    lateinit var adapter: TasksAdapter

    private lateinit var binding: ActivityTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks)
        binding.tasksList.layoutManager = layoutManager
        binding.tasksList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        connectOutput()
    }

    private fun connectOutput() {
        disposables.add(
                adapter.outputStream()
                        .subscribe { event ->
                            Timber.d("Event: ${event}")
                            when (event) {
                                is CheckboxEvent -> {
                                    outputStream.onNext(event)
                                }
                                is CardEvent -> {
                                    Timber.d("Navigating to task ${event.cardContent.id}")
                                    navigateTo("app://taskdetail?id=${event.cardContent.id}")
                                }
                            }
                        })

    }

    override val successHandler = { uiModel: TasksUiModel ->
        uiModel.snackbarMessage?.let {
            Snackbar.make(binding.tasksContainer, it, Snackbar.LENGTH_LONG).show()
        }

        adapter.updateList(uiModel.tasksList)
    }
}