package ar.valentinholgado.template.backend.artsy.search

import ar.valentinholgado.template.backend.artsy.Links
import com.fasterxml.jackson.annotation.JsonProperty

/** Search entry **/
data class Entry(val type: String,
                 val title: String,
                 val description: String?,
                 val og_type: String,
                 @JsonProperty("_links") val links: Links)