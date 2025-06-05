
package org.example
import GitlabCIParser
import org.example.exception.InvalidConfigurationException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files


class GitlabCIParserTest {
private val parser = GitlabCIParser()

@Test
fun `parse valid yaml with simple job`() {
    val yamlContent = """
            stages:
              - build
              - test
            
            build_job:
              stage: build
              script:
                - echo "build"
              image: ubuntu
        """.trimIndent()

    val file = Files.createTempFile(null, ".yml").toFile().apply {
        writeText(yamlContent)
    }
    val result = parser.parse(file)

    assertEquals(listOf("build", "test"), result.stages)
    assertTrue(result.jobs.containsKey("build_job"))

    val job = result.jobs["build_job"]!!
    assertEquals("build", job.stage)
    assertEquals(listOf("echo \"build\""), job.script)
    assertEquals("ubuntu", job.image?.name)
}

@Test
fun `parse yaml missing stages throws exception`() {
    val yamlContent = """
            build_job:
              stage: build
              script:
                - echo "build"
        """.trimIndent()

    val file = Files.createTempFile(null, ".yml").toFile().apply {
        writeText(yamlContent)
    }

    val ex = assertThrows<InvalidConfigurationException> {
        parser.parse(file)
    }
    assertTrue(ex.message!!.contains("Missing 'stages' section"))
}

@Test
fun `parse job with invalid image format throws exception`() {
    val yamlContent = """
            stages:
              - build

            build_job:
              stage: build
              script:
                - echo "build"
              image:
                invalidField: something
        """.trimIndent()

    val file = Files.createTempFile(null, ".yml").toFile().apply {
        writeText(yamlContent)
    }

    val ex = assertThrows<InvalidConfigurationException> {
        parser.parse(file)
    }
    assertTrue(ex.message!!.contains("Invalid image"))
}

@Test
fun `parse job without script does not fail if script is empty`() {
    val yamlContent = """
            stages:
              - build

            build_job:
              stage: build
        """.trimIndent()


    val file = Files.createTempFile(null, ".yml").toFile().apply {
        writeText(yamlContent)
    }

    val result = parser.parse(file)

    val job = result.jobs["build_job"]!!
    assertEquals("build", job.stage)
    assertTrue(job.script.isEmpty())
}
}
