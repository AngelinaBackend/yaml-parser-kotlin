package model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import deserializer.ImageDeserializer



data class Job(
    val stage: String? = null,
    @JsonDeserialize(using = ImageDeserializer::class)
    val image: Image? = null,
    val script: List<String> = emptyList()
)