package ent.service.extractor;

import ent.Bot;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

@Service
public class ExtractorService {

    private final Bot bot;
    private final ResourceLoader resourceLoader;
    private final CloudVisionTemplate cloudVisionTemplate;

    public ExtractorService(Bot bot, ResourceLoader resourceLoader, CloudVisionTemplate cloudVisionTemplate) {
        this.bot = bot;
        this.resourceLoader = resourceLoader;
        this.cloudVisionTemplate = cloudVisionTemplate;
    }

    private String extractText(String url) {
        return this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(url));
    }

    public String prepareFile(PhotoSize photo) {
        String fileUniqueId = photo.getFileUniqueId();
        String fileId = photo.getFileId();
        GetFile getFile = new GetFile(fileId);
        String path = bot.downloadPhoto(getFile, fileUniqueId);
        return extractText(path);
    }

    public String prepareFile(String url) {
        return extractText(url);
    }

    /*public SendDocument sendTxt(PhotoSize photo) throws IOException {
        String uniqueName = String.valueOf(System.currentTimeMillis());
        uniqueName = uniqueName.substring((uniqueName.length() / 2));
        String result = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n Lorem Ipsum has been the industry's standard dummy text ever since the 1500s\n when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries\n but also the leap into electronic typesetting\n remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages\n and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        File file = File.createTempFile(uniqueName, ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(result);
            SendDocument document = new SendDocument();
            document.setDocument(new InputFile(file));
            document.setCaption("🔰 @TasvirniMatngaBot tomonidan yaratildi");
            return document;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }*/
}
