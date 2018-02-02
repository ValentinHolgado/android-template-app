package ar.valentinholgado.template.view.soundplayer

import ar.valentinholgado.template.view.Event

open class SoundPlayerEvent : Event()
class PlayEvent : SoundPlayerEvent()
class PauseEvent : SoundPlayerEvent()
class ReadyEvent : SoundPlayerEvent()
class StopEvent : SoundPlayerEvent()
class SelectFileEvent(val path: String) : SoundPlayerEvent()
