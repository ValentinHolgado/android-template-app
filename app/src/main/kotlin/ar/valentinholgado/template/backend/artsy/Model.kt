package ar.valentinholgado.template.backend.artsy

import com.fasterxml.jackson.annotation.JsonProperty

data class Links(val thumbnail: Link? = null,
                 val permalink: Link? = null,
                 val self: Link? = null,
                 val image: Link? = null)

data class Link(@JsonProperty("href") val url: String,
                val templated: Boolean?)
