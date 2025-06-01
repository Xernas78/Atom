package dev.xernas.atom.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourceUtils {

    public static String getResourceString(Class<?> origin, String path, Charset encoding) throws IOException {
        try (InputStream stream = origin.getResourceAsStream(path)) {
            if (stream == null) {
                throw new FileNotFoundException("'" + path + "' wasn't found");
            }
            byte[] resourceBytes = stream.readAllBytes();
            return new String(resourceBytes, encoding);
        }
    }

}
