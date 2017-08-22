package ar.valentinholgado.template.backend.artsy.artwork

import com.fasterxml.jackson.annotation.JsonProperty

/** Artwork **/
data class ArtworkResponse(@JsonProperty("_embedded") val embedded: ArtworkEmbedded)

data class ArtworkEmbedded(val artworks: List<Artwork>)

