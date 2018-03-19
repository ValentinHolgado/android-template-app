package ar.valentinholgado.template.backend

import ar.valentinholgado.template.backend.artsy.ArtsyRepository
import ar.valentinholgado.template.backend.audio.AudioRepository

/**
 * Facade for different services with the same interface:
 * An input stream of Actions and an output stream of Results.
 */
class Repository(val artsyRepository: ArtsyRepository,
                 val audioRepository: AudioRepository) : BaseRepository<Action, Result>() {

    init {
        inputStream
                .compose(artsyRepository.search)
                .subscribe(outputStream)

        inputStream
                .compose(artsyRepository.artist)
                .subscribe(outputStream)

        inputStream
                .compose(artsyRepository.show)
                .subscribe(outputStream)
    }
}