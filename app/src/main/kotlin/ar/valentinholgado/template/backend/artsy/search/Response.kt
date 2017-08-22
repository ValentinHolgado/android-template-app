package ar.valentinholgado.template.backend.artsy.search

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The root of the root.
 */
data class SearchResponse(@JsonProperty("_embedded") val embedded: SearchEmbedded)

data class SearchEmbedded(val results: List<Entry>)