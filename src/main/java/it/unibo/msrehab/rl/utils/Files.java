package it.unibo.msrehab.rl.utils;

import java.io.*;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Utility class for performing file input and output operations.
 */
public class Files
{
    private Files(){}
    /**
     * Writes the specified content to a file at the given path. If the file does not exist,
     * it will be created. If the file already exists, its content will be overwritten.
     *
     * @param path the file path where the content should be written
     * @param content the content to write to the file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static void writeTextFile(String path, String content) throws IOException
    {
        OutputStreamWriter osw = new OutputStreamWriter(java.nio.file.Files.newOutputStream(Paths.get(path)));
        BufferedWriter writer = new BufferedWriter(osw);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    /**
     * Reads the contents of a text file from the specified file path and returns it as a string.
     *
     * @param path the file path of the text file to be read
     * @return the content of the text file as a single string, with lines separated by system line separators
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static String readTextFile(String path) throws IOException
    {
        InputStreamReader isr = new InputStreamReader(java.nio.file.Files.newInputStream(Paths.get(path)));
        BufferedReader br = new BufferedReader(isr);
        String fileContent = br
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        br.close();
        return fileContent;
    }
}
