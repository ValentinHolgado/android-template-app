package ar.valentinholgado.template.view.soundplayer

import ar.valentinholgado.template.R
import ar.valentinholgado.template.view.common.CardAdapter

class AudioFileAdapter : CardAdapter<AudioFileContent>() {

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.viewholder_audiofile
    }
}