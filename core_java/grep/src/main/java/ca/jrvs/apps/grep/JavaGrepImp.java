package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepImp implements JavaGrep{
    private String regex;
    private String rootPath;
    private String outFile;
    private static final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

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

    @Override
    public void process() throws IOException {
        try {
            List<String> matchedLines = new ArrayList<>();
            for(File file: listFiles(rootPath)){
                matchedLines.addAll(readLines(file).stream().filter(this::containsPattern).collect(Collectors.toList()));
            }
            writeToFile(matchedLines);
        } catch (IOException e) {
            // Handle exceptions during processing
            handleException("Error during processing", e);
        }
    }

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

    @Override
    public List<String> readLines(File inputFile) throws IOException {
            //
            if(!inputFile.isFile()){
                throw new IllegalArgumentException(inputFile + "is not a regular file");
            }
            List<String> lines = new ArrayList<>();
            try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
                String line;
                while((line = reader.readLine()) !=null){
                    lines.add(line);
                }
            }catch (IOException e) {
                // Handle exceptions during file reading
                handleException("Error reading lines from file", e);
                throw e;
            }
            return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        return Pattern.compile(regex).matcher(line).find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            // Iterate through the lines and write them to the file
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle exceptions during file writing
            handleException("Error writing to file", e);
            throw e;
        }
    }

    private void handleException(String message, Exception e) {
        logger.error(message, e);
    }

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
            // Log an error if an IOException occurs during processing
            logger.error("Error during processing", e);
        }
    }
}



