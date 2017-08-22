package ar.valentinholgado.template.backend.artsy.artwork

import ar.valentinholgado.template.backend.Result

data class ArtworkResult(val body: List<Artwork>? = null,
                         override val successMessage: String? = null,
                         override val errorMessage: String? = null,
                         override val status: Status) : Result()