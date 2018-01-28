package ar.valentinholgado.template.backend.files

import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.Result
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.io.File
import java.util.*

class FilesRepository(val inputStream: Subject<Action> = BehaviorSubject.create(),
                      val outputStream: Subject<Result> = BehaviorSubject.create()) {

    init {
        inputStream
                .compose(actionsToResults)
                .subscribe(outputStream)
    }

    companion object {
        val actionsToResults = { actions: Observable<Action> ->
            actions.filter { action -> action is ListFilesAction }.map { action ->
                FilesResult(
                        status = Result.Status.SUCCESS,
                        fileList = Arrays.asList(
                                File("uri1"),
                                File("uri2"),
                                File("uri3"))
                )
            }
        }
    }
}