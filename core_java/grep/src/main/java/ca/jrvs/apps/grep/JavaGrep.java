package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface JavaGrep {
    void process() throws IOException;


    List<File> listFiles(String rootDir) throws IOException;

    List<String> readLines(File inputFile) throws IOException;

    boolean containsPattern(String line);

    void writeToFile(List<String> lines) throws IOException;

    String getRootPath();

    void setRootPath(String rootPath);

    String getRegex();

    void setRegex(String regex);

    String getOutFile();

    void setOutFile(String outFile);

}
