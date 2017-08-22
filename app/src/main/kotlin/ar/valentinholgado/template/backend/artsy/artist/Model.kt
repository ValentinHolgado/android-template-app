package ar.valentinholgado.template.backend.artsy.artist

data class Artist(val id: String,
                  val name: String,
                  val nationality: String?,
                  val location: String?,
                  val birthday: String?,
                  val imageUri: String?)