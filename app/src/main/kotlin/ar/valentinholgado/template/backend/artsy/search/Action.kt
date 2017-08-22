package ar.valentinholgado.template.backend.artsy.search

import ar.valentinholgado.template.backend.Action

data class SearchAction(val query: String) : Action()