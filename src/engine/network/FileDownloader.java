package engine.network;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class FileDownloader {

    @SuppressWarnings("deprecation")
    public static void downloadFile(String fileURL, String savePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileURL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
             
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            
            // Read from the input stream and write to the output stream
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

            System.out.println("Download complete: " + savePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}