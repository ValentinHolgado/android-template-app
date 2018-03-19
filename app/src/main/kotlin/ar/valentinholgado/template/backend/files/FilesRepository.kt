package ar.valentinholgado.template.backend.files

import android.content.Context
import android.os.Environment.DIRECTORY_MUSIC
import android.os.FileObserver
import ar.valentinholgado.template.backend.BaseRepository
import ar.valentinholgado.template.backend.Result
import io.reactivex.Observable

class FilesRepository(val context: Context) : BaseRepository<FilesAction, Result>() {

    init {
        val actionsToResults = { actions: Observable<FilesAction> ->
            actions.map { _ ->
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

        object : FileObserver(context.getExternalFilesDir(DIRECTORY_MUSIC).absolutePath) {
            override fun onEvent(event: Int, path: String?) {
                if (event == CREATE) outputStream.onNext(FilesResult(
                        status = Result.Status.SUCCESS,
                        fileList = context
                                // TODO Remove Hardcoded DIRECTORY_MUSIC. Choose the folder
                                // using the action.
                                .getExternalFilesDir(DIRECTORY_MUSIC)
                                .listFiles()
                                .toList()))
            }
        }
    }
}