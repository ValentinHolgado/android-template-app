package ar.valentinholgado.template.backend.artsy.artist

import ar.valentinholgado.template.backend.Result

data class ArtistResult(val body: Artist? = null,
                        override val successMessage: String? = null,
                        override val errorMessage: String? = null,
                        override val status: Status) : Result()