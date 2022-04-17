package ent.service.extractor;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileService {

    @SneakyThrows
    public File getFile(String text) {
        String uniqueName = String.valueOf(System.currentTimeMillis());
        uniqueName = uniqueName.substring((uniqueName.length() / 2));
        File file = File.createTempFile(uniqueName, ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
