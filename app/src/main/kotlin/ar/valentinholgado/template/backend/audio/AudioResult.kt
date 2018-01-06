package ar.valentinholgado.template.backend.audio

import ar.valentinholgado.template.backend.Result

data class AudioResult(override val status: Status,
                       override val successMessage: String? = null,
                       override val errorMessage: String? = null) : Result()