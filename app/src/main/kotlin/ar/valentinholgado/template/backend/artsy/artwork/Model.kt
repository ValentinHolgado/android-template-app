package ar.valentinholgado.template.backend.artsy.artwork

import ar.valentinholgado.template.backend.artsy.Links
import com.fasterxml.jackson.annotation.JsonProperty

/** Artwork **/
data class Artwork(val id: String,
                   val title: String,
                   val category: String,
                   val medium: String,
                   val date: String,
                   @JsonProperty("_links") val links: Links)