package gui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

public class Utils {
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        try (InputStream source = FontManager.class.getClassLoader().getResourceAsStream(resource)) {
            if (source == null) {
                throw new IOException("Resource not found: " + resource);
            }

            buffer = ByteBuffer.allocate(bufferSize);
            Channels.newChannel(source).read(buffer);
            buffer.flip();
        }
        return buffer;
    }
}
