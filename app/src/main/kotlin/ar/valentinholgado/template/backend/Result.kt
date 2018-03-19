package ar.valentinholgado.template.backend

/**
 * A basic Result.
 *
 * Contains a status, and can also have
 * a success and/or error message.
 */
abstract class Result {
    abstract val status: Status
    abstract val successMessage: String?
    abstract val errorMessage: String?

    enum class Status {
        IN_FLIGHT,
        SUCCESS,
        ERROR,
        ON_PAUSE,
        FINISHED,
        STOPPED,
        PLAYING,
        RESUMING,
        RECORDING
    }
}
