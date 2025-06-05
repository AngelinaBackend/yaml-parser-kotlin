import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

import org.example.exception.InvalidConfigurationException
import model.GitlabCI
import model.Job
import com.fasterxml.jackson.module.kotlin.convertValue
import org.slf4j.LoggerFactory
import java.io.File

class GitlabCIParser {
    private val logger = LoggerFactory.getLogger(GitlabCIParser::class.java)
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun parse(file: File): GitlabCI {
        val rootNode = readYaml(file)
        val stages = parseStages(rootNode)
        val jobs = parseJobs(rootNode)
        return GitlabCI(stages, jobs)
    }

    private fun readYaml(file: File): JsonNode {
        try {
            return mapper.readTree(file)
        } catch (e: Exception) {
            logger.error("Failed to read YAML: ${e.message}", e)
            throw InvalidConfigurationException("Cannot read YAML file: ${e.message}")
        }
    }

    private fun parseStages(rootNode: JsonNode): List<String> {
        val stagesNode = rootNode["stages"]
            ?: throw InvalidConfigurationException("Missing 'stages' section")

        if (!stagesNode.isArray) {
            throw InvalidConfigurationException("'stages' must be an array")
        }

        return stagesNode.map(JsonNode::asText)
    }

    private fun parseJobs(rootNode: JsonNode): Map<String, Job> {
        val jobs = mutableMapOf<String, Job>()
        val fields = rootNode.fields()

        while (fields.hasNext()) {
            val (key, value) = fields.next()
            if (key == "stages") continue

            try {
                val job = mapper.convertValue<Job>(value)
                jobs[key] = job
            } catch (e: Exception) {
                logger.error("Error parsing job '$key': ${e.message}", e)
                throw InvalidConfigurationException("Invalid job '$key': ${e.message}")
            }
        }

        return jobs
    }
}
