package model

data class GitlabCI(
    val stages: List<String> = emptyList(),
    val jobs: Map<String, Job> = emptyMap()
)
