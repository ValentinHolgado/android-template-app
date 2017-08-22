package ar.valentinholgado.template.backend.artsy.artist

import ar.valentinholgado.template.backend.artsy.Links
import com.fasterxml.jackson.annotation.JsonProperty

data class ArtistResponse(val id: String,
                          val name: String,
                          val birthday: String? = null,
                          val location: String? = null,
                          val nationality: String? = null,
                          @JsonProperty("_links") val links: Links? = null,
                          @JsonProperty("image_versions") val imageVersions: List<String>? = null)