package deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.example.exception.InvalidConfigurationException
import model.Image

class ImageDeserializer : JsonDeserializer<Image>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Image {
        val node = p.codec.readTree<JsonNode>(p)
        return when {
            node.isTextual -> Image(node.asText())
            node.isObject -> {
                val nameNode = node.get("name")
                if (nameNode != null && nameNode.isTextual) {
                    Image(nameNode.asText())
                } else {
                    throw InvalidConfigurationException("Invalid image object: missing or non-textual 'name'")
                }
            }
            else -> throw InvalidConfigurationException("Invalid image format: expected string or object")
        }
    }
}