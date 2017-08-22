package ar.valentinholgado.template.view.feed

import ar.valentinholgado.template.view.Event

data class HomeEvent(val message: String? = null) : Event()
data class SearchEvent(val query: String) : Event()
