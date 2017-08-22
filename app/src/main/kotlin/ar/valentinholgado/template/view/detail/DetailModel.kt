package ar.valentinholgado.template.view.detail

data class DetailUiModel(val content: DetailContent,
                         val errorMessage: String? = null,
                         val isLoading: Boolean = false)

data class DetailContent(val title: String,
                         val subtitle: String? = null,
                         val description: String? = null,
                         val imageUri: String? = null)
