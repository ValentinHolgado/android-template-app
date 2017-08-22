package ar.valentinholgado.template.view.detail

import ar.valentinholgado.template.view.Event

data class DetailEvent(val id: String,
                       val type: String) : Event()