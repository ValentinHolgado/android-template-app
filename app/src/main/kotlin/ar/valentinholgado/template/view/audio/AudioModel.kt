package ar.valentinholgado.template.view.audio

data class AudioUiModel(val content: AudioContent,
                        val isPlaying: Boolean = false,
                        val errorMessage: String? = null,
                        val isLoading: Boolean = false)

data class AudioContent(val audioId: String,
                        val title: String,
                        val subtitle: String? = null,
                        val description: String? = null,
                        val imageUri: String? = null)
