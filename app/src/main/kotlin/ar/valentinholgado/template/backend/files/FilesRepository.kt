package ar.valentinholgado.template.backend.files

import android.content.Context
import android.os.Environment.DIRECTORY_MUSIC
import android.os.FileObserver
import ar.valentinholgado.template.backend.BaseRepository
import ar.valentinholgado.template.backend.Result
import io.reactivex.Observable
import java.io.File

class FilesRepository(val context: Context) : BaseRepository<FilesAction, Result>() {

    private val fileObserver = object : FileObserver(context.getExternalFilesDir(DIRECTORY_MUSIC).absolutePath) {
        override fun onEvent(event: Int, path: String?) {
            if (event == CREATE) outputStream.onNext(FilesResult(
                    status = Result.Status.SUCCESS,
                    fileList = getFiles()))
        }
    }

    private val actionsToResults = { actions: Observable<FilesAction> ->
        actions.map { _ ->
            FilesResult(
                    status = Result.Status.SUCCESS,
                    fileList = getFiles()
            )
        }
    }

    init {
        inputStream
                .compose(actionsToResults)
                .cache()
                .subscribe(outputStream)

        fileObserver.startWatching()
    }

    private fun getFiles(): List<File> {
        return context
                // TODO Remove Hardcoded DIRECTORY_MUSIC. Choose the folder
                // using the action.
                .getExternalFilesDir(DIRECTORY_MUSIC)
                .listFiles()
                .toList()
    }
}