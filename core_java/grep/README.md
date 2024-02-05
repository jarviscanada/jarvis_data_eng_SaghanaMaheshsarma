# Introduction
The Java Grep App is a command-line tool inspired by the Linux grep command, enabling users to search for matching strings within files. The app is designed in core Java, utilizing features like file handling, streams, and regular expressions. The application's functionalities include recursive pattern matching, file traversal, and output to a specified file.

# Quick Start
To use the Java Grep App:
1. **Run with Docker:**
    - Build the Docker image: `docker build -t ${docker_user}/grep .`
    - Run the Docker container:
        ```bash
        docker run --rm \
        -v `pwd`/data:/data -v `pwd`/log:/log \
        ${docker_user}/grep <regex-pattern> <root-directory> <output-file>
        ```
      Replace `<regex-pattern>`, `<root-directory>`, and `<output-file>` with your desired parameters.

2. **Run with JAR:**
    - Download the JAR file from the [repository release](#).
    - Run the JAR using the `java -jar` command:
        ```bash
        java -jar grep.jar <regex-pattern> <root-directory> <output-file>
        ```
      Replace `<regex-pattern>`, `<root-directory>`, and `<output-file>` with your desired parameters.

# Implementation
## Pseudocode
```java
public class JavaGrepImp implements JavaGrep {
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        for (Path file : listFiles()) {
            matchedLines.addAll(
                Files.readAllLines(file)
                    .stream()
                    .filter(this::containsPattern)
                    .collect(Collectors.toList())
            );
        }
        writeToFile(matchedLines);
    }
    
    // Implementation of other methods..
}
```

## Performance Issue
The app may face memory issues when processing large files. To address this, consider implementing a streaming approach, reading files line by line, to minimize memory consumption.

# Test
Manually test the application by:
1. Preparing sample data with files containing matching and non-matching patterns.
2. Running the app with different regex patterns and root directories.
3. Verifying the generated output file for correctness.

# Deployment
Dockerize the app for distribution:
1. Build the Docker image using the provided Dockerfile.
2. Run the Docker container with the desired parameters.

# Improvement
Three potential improvements:
1. Implement multithreading for parallel file processing.
2. Enhance regex pattern validation for user-friendly input.
3. Integrate automated testing for better code coverage and reliability.