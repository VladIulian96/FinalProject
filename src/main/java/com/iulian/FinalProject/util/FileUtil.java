package com.iulian.FinalProject.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void deleteDirectory(String folderDir) throws IOException {
        Path filePath = Paths.get(folderDir);

        try {
            FileUtils.deleteDirectory(filePath.toFile());
        } catch (IOException exception) {
            System.out.println("Folder doesn't exist!");
        }
    }

    public static void deleteFile(String fileDir) throws IOException {
        Path filePath = Paths.get(fileDir);

        if (Files.exists(filePath)) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException exception) {
                System.out.println("File doesn't exist!");
            }
        }
    }

}
