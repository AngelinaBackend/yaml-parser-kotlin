# Gitlab CI YAML Parser

A parser for the .gitlab-ci.yml configuration file, implemented in Kotlin.
The project deserializes a YAML file into strictly typed Kotlin objects reflecting the pipeline structure: stages and jobs. It uses Jackson with the YAML module, without relying on SDKs (e.g., gitlab4j).

## Features

- Deserialization of YAML files into Kotlin models
- Support for keys: stages, script, stage, image
- Handling of image in two formats: string and object
- Error logging with explanations
- Error handling with custom exceptions
- Unit tests covering major scenarios

## Technologies Used

- Kotlin
- Jackson (YAMLFactory, Kotlin module)
- SLF4J (logging)
- JUnit 5 (tests)

## How to Build and Run

1. Clone the repository:

```bash
git clone https://github.com/AngelinaBackend/yaml-parser-kotlin.git
cd yaml-parser-kotlin
```

2.	Build the project:
 
```bash
./gradlew build
```

On Windows:

```bash
gradlew.bat build
```

3.	Make sure a valid .yml file (e.g., sample.yml) is present in src/main/resources
4.	Run the application:
 
```bash
./gradlew run
```

## Sample Input File (sample.yml)

```yaml
stages:
- build
- test

build_job:
stage: build
script:
 - echo "Building..."
image: ubuntu
```

## Testing

To run tests:

```bash
./gradlew test
```

Test coverage includes:

 - Successful parsing of valid YAML
 - Errors when stages are missing
 - Invalid image format
 - Handling jobs without scripts

Contacts

Author: Kriklyvaya Angelina
Contact: angelina.kriklivaya@list.ru
