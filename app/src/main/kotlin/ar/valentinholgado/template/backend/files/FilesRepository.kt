package ar.valentinholgado.template.backend.files

import android.content.Context
import android.os.Environment.DIRECTORY_MUSIC
import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.Result
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class FilesRepository(val inputStream: Subject<Action> = BehaviorSubject.create(),
                      val outputStream: Subject<Result> = BehaviorSubject.create(),
                      val context: Context) {

    init {
        val actionsToResults = { actions: Observable<Action> ->
            actions.filter { action -> action is ListFilesAction }
                    .map { action ->
                        FilesResult(
                                status = Result.Status.SUCCESS,
                                fileList = context
                                        // TODO Remove Hardcoded DIRECTORY_MUSIC. Choose the folder
                                        // using the action.
                                        .getExternalFilesDir(DIRECTORY_MUSIC)
                                        .listFiles()
                                        .toList()
                        )
                    }

        }

        inputStream
                .compose(actionsToResults)
                .cache()
                .subscribe(outputStream)
    }
}