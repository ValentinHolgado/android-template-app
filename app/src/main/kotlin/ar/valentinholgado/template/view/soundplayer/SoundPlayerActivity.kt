package ar.valentinholgado.template.view.soundplayer

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import ar.valentinholgado.template.R
import ar.valentinholgado.template.databinding.ActivityAudioBinding
import ar.valentinholgado.template.view.ReactiveActivity
import com.jakewharton.rxbinding2.view.clicks
import javax.inject.Inject


class SoundPlayerActivity : ReactiveActivity<AudioUiModel, SoundPlayerEvent>() {

    @Inject lateinit var layoutManager: RecyclerView.LayoutManager
    @Inject lateinit var adapter: AudioFileAdapter
    private lateinit var binding: ActivityAudioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio)
        binding.fileList.layoutManager = layoutManager
        binding.fileList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1)
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 2)
        connectOutput()
    }

    private fun connectOutput() {
        binding.transportPlayPause.clicks()
                .map { _ ->
                    binding.model?.let {
                        if (it.isPlaying) PauseEvent()
                        else PlayEvent(it.selectedFilePath)
                    }
                }
                .subscribe(outputStream)

        // Click on an item in the file list
        adapter.outputStream()
                .map { event -> SelectFileEvent(event.cardContent.path) }
                .subscribe(outputStream)

        outputStream.onNext(ReadyEvent())
    }

    override val successHandler = { model: AudioUiModel ->
        model.fileList?.let {
            if (it != binding.model?.fileList)
                adapter.updateList(it)
        }

        binding.model = model
    }

    // TODO Remove this function.
    // Taken from https://www.sitepoint.com/requesting-runtime-permissions-in-android-m-and-n/
    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }
}