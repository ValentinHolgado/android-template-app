package ar.valentinholgado.template.view.audio

import ar.valentinholgado.template.view.Event

open class AudioEvent : Event()

data class TransportEvent(val type: String): AudioEvent()