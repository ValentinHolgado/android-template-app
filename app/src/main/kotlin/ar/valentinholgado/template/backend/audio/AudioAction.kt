package ar.valentinholgado.template.backend.audio

import ar.valentinholgado.template.backend.Action

data class PlayAction(val filename: String) : Action()
class PauseAction() : Action()
class StopAction() : Action()