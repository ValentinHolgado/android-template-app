package ar.valentinholgado.template.backend.audio

import ar.valentinholgado.template.backend.Result

data class AudioResult(override val status: Status,
                       val title: String? = null,
                       val filepath: String? = null,
                       val progress: Int = 0,
                       override val successMessage: String? = null,
                       override val errorMessage: String? = null) : Result()