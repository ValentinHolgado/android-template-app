package ar.valentinholgado.template.view.soundplayer

data class AudioUiModel(val content: AudioContent,
                        val fileList: List<AudioFile>? = null,
                        val isPlaying: Boolean = false,
                        val errorMessage: String? = null,
                        val isLoading: Boolean = false)

data class AudioContent(val audioId: String,
                        val title: String? = "No name found",
                        val subtitle: String? = null,
                        val description: String? = null,
                        val imageUri: String? = null)

data class AudioFile(val path: String,
                     val name: String)