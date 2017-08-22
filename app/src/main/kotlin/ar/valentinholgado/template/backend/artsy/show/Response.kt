package ar.valentinholgado.template.backend.artsy.show

data class ShowResponse(val id: String,
                        val description: String? = null,
                        val name: String? = null,
                        val status: String? = null)