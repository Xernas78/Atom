package dev.xernas.atom.resource;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceUtils {

    public static String getResourceString(Path path) throws IOException {
        return getResourceString(path, Charset.defaultCharset());
    }

    public static String getResourceString(Path path, Charset encoding) throws IOException {
        return new String(getResourceBytes(path), encoding);
    }

    public static byte[] getResourceBytes(Path path) throws IOException {
        try (InputStream stream = Files.newInputStream(path, StandardOpenOption.READ)) {
            return stream.readAllBytes();
        }
    }

    public static List<Path> list(Path path) throws IOException {
        List<Path> paths = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                paths.add(entry);
            }
        }
        return paths;
    }

    public static Path getResourcePath(String path) throws FileNotFoundException, URISyntaxException {
        return getResourcePath(getContextClassLoader(), path);
    }

    public static Path getResourcePath(ClassLoader loader, String path) throws FileNotFoundException, URISyntaxException {
        URL resource = loader.getResource(path);
        if (resource == null) {
            throw new FileNotFoundException("'" + path + "' wasn't found");
        }
        URI resourceUri = resource.toURI();
        if (resourceUri.getScheme().equals("jar")) {
            FileSystem fileSystem;
            try (FileSystem currentFileSystem = FileSystems.newFileSystem(resourceUri, Collections.emptyMap());) {
                fileSystem = currentFileSystem;
            } catch (FileSystemAlreadyExistsException | IOException e) {
                fileSystem = FileSystems.getFileSystem(resourceUri);
            }
            if (fileSystem == null) throw new FileNotFoundException("File system for '" + path + "' couldn't be created");
            return fileSystem.getPath(path);
        }
        return Paths.get(resourceUri);
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
