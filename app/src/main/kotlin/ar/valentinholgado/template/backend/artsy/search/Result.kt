package ar.valentinholgado.template.backend.artsy.search

import ar.valentinholgado.template.backend.Result

data class SearchResult(val body: List<Entry>? = null,
                        override val successMessage: String? = null,
                        override val errorMessage: String? = null,
                        override val status: Status) : Result()