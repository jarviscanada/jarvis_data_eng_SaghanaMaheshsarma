package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation og the JavaGrep interface for searching a text pattern in files.
 */
public class JavaGrepImp implements JavaGrep {

  private static final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);
  private String regex;
  private String rootPath;
  private String outFile;

  //Getters and setters
  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  public String getOutFile() {
    return outFile;
  }

  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  /**
   * Execute the process for finding the text pattern in the files
   *
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    try {
      List<String> matchedLines = listFiles(rootPath)
          .stream()
          .flatMap(file -> {
            try {
              return readLines(file).stream();
            } catch (IOException e) {
              handleException("Error reading lines from file", e);
              return Stream.empty();
            }
          })
          .filter(this::containsPattern)
          .collect(Collectors.toList());
      writeToFile(matchedLines);
    } catch (IOException e) {
      // Handle exceptions during processing
      handleException("Error during processing", e);
    }
  }

  /**
   * List all the files in the specified root directory
   *
   * @param rootDir
   * @return A list of files in the root directory
   * @throws IOException
   */
  @Override
  public List<File> listFiles(String rootDir) throws IOException {
    try (Stream<Path> pathStream = Files.walk(Paths.get(rootDir))) {
      return pathStream
          .filter(Files::isRegularFile)
          .map(Path::toFile)
          .collect(Collectors.toList());
    } catch (IOException e) {
      handleException("Error listing files", e);
      throw e;
    }
  }

  /**
   * Read all the lines from the given file
   *
   * @param inputFile
   * @return A list of lines read from the file
   * @throws IOException
   */
  @Override
  public List<String> readLines(File inputFile) throws IOException {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException(inputFile + "is not a regular file");
    }
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(inputFile.toPath())) {
      return reader.lines().collect(Collectors.toList());
    } catch (IOException e) {
      // Handle exceptions during file reading
      handleException("Error reading lines from file", e);
      throw e;
    }
  }

  /**
   * Check if a line contains the specified regex pattern
   *
   * @param line
   * @return True if the line contains the pattern, false otherwise.
   */
  @Override
  public boolean containsPattern(String line) {
    return Pattern.compile(regex).matcher(line).find();
  }

  /**
   * Write lines to the output file
   *
   * @param lines
   * @throws IOException
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outFile))) {
      lines.forEach(line -> {
        try {
          writer.write(line);
          writer.newLine();
        } catch (IOException e) {
          handleException("Error writing to file", e);
        }
      });
    } catch (IOException e) {
      // Handle exceptions during file writing
      handleException("Error writing to file", e);
      throw e;
    }
  }

  /**
   * Log an exception with the specified message
   *
   * @param message - The log message
   * @param e       - The exception to log
   */
  private void handleException(String message, Exception e) {
    logger.error(message, e);
  }

  /**
   * Main method for executing the grep application
   *
   * @param args - This has the command line arguments which consist of regex, the input directory
   *             to search and the output file
   */
  public static void main(String[] args) {
    BasicConfigurator.configure();
    if (args.length != 3) {
      // Log an error and throw an IllegalArgumentException
      logger.error("USAGE: java GrepApp <regex> <rootDir> <outFile>");
      throw new IllegalArgumentException("Incorrect number of arguments");
    }

    // Parse command-line arguments
    JavaGrepImp grepApp = new JavaGrepImp();
    grepApp.setRegex(args[0]);
    grepApp.setRootPath(args[1]);
    grepApp.setOutFile(args[2]);

    try {
      // Execute the grep process
      grepApp.process();
    } catch (IOException e) {
      logger.error("Error during processing", e);
    }
  }
}



