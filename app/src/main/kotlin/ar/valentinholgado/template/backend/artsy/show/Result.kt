package ar.valentinholgado.template.backend.artsy.show

import ar.valentinholgado.template.backend.Result

data class ShowResult(val body: Show? = null,
                        override val successMessage: String? = null,
                        override val errorMessage: String? = null,
                        override val status: Status) : Result()