package ar.valentinholgado.template.view.audio

import ar.valentinholgado.template.view.Event

data class AudioEvent(val id: String,
                      val type: String) : Event()