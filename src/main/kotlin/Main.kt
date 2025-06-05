import org.example.exception.InvalidConfigurationException
import org.slf4j.LoggerFactory
import java.io.File

val logger = LoggerFactory.getLogger("YamlParser")

fun main() {
    val file = File("src/main/resources/sample.yml")
    val parser = GitlabCIParser()

    runCatching {
        parser.parse(file)
    }.onSuccess { config ->
        logger.info("Parsing completed successfully:\n$config")
    }.onFailure { ex ->
        when (ex) {
            is InvalidConfigurationException -> logger.error("Invalid configuration: ${ex.message}")
            else -> logger.error("Unexpected error occurred", ex)
        }
    }
}