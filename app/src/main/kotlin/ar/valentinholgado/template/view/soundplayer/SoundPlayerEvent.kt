package ar.valentinholgado.template.view.soundplayer

import ar.valentinholgado.template.view.Event

open class SoundPlayerEvent : Event()
class PlayEvent(val path: String?) : SoundPlayerEvent()
class PauseEvent : SoundPlayerEvent()

class ReadyEvent : SoundPlayerEvent()
class DestroyEvent : SoundPlayerEvent()

class StopEvent : SoundPlayerEvent()
class SelectFileEvent(val path: String) : SoundPlayerEvent()
class StartRecordEvent : SoundPlayerEvent()
class StopRecordEvent : SoundPlayerEvent()
