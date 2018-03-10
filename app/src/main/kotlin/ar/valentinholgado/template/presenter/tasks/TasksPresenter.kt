package ar.valentinholgado.template.presenter.tasks

import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.tasks.ActivateTaskAction
import ar.valentinholgado.template.backend.tasks.CompleteTaskAction
import ar.valentinholgado.template.backend.tasks.NewTaskAction
import ar.valentinholgado.template.backend.tasks.TasksRepository
import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.tasks.CheckboxEvent
import ar.valentinholgado.template.view.tasks.TasksUiModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TasksPresenter @Inject constructor(tasksView: ReactiveView<TasksUiModel, Event>,
                                         tasksRepository: TasksRepository) {

    init {
        val eventsToActions = { events: Observable<Event> ->
            events.map { event ->
                when (event) {
                    is CheckboxEvent -> {
                        if (event.checked)
                            ActivateTaskAction(event.cardContent.id)
                        else
                            CompleteTaskAction(event.cardContent.id)
                    }
                    else -> TODO()
                }
            }

        }

        val accumulator = { state: TasksUiModel, result: Result ->
            state
        }

        tasksView.outputStream()
                .compose(eventsToActions)
                .subscribe(tasksRepository.inputStream())

        tasksRepository.outputStream()
                .doOnEach { result -> Timber.i("Result: %s", result) }
                .scan(TasksUiModel(), accumulator)
                .subscribe(tasksView.inputStream())

        //TODO remove
        Observable.just(tasksRepository.inputStream())
                .subscribeOn(Schedulers.io())
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribe { observable ->
                    observable.onNext(NewTaskAction("lala", "sasa"))
                    observable.onNext(NewTaskAction("miau", "shakira"))
                    observable.onNext(NewTaskAction("wow", "cafe"))
                }
                .dispose()
    }
}