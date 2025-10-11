package dev.xernas.atom.file;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static Path directories(Path basePath, Path relativePath) {
         Path directoryPath = basePath.resolve(relativePath).normalize();
         if (!directoryPath.toFile().exists() && !directoryPath.toFile().mkdirs()) throw new RuntimeException("Could not create directory: " + directoryPath);
         if (!Files.isDirectory(directoryPath)) throw new RuntimeException("Path is not a directory: " + directoryPath);
         return directoryPath;
    }

    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return ""; // No extension found
        }
        return fileName.substring(lastDotIndex + 1);
    }

    public static boolean hasWebExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return extension.equals("html") || extension.equals("css") || extension.equals("js")
                || extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")
                || extension.equals("gif") || extension.equals("svg") || extension.equals("ico")
                || extension.equals("json") || extension.equals("xml") || extension.equals("txt");
    }

}
