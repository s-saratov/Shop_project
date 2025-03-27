package de.aittr.g_52_shop.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.aittr.g_52_shop.service.interfaces.FileService;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final AmazonS3 client;
    private final ProductService productService;

    public FileServiceImpl(AmazonS3 client, ProductService productService) {
        this.client = client;
        this.productService = productService;
    }

    @Override
    public String upload(MultipartFile file, String productTitle) {
        try {
            String uniqueName = generateUniqueFileName(file);


            // Этот объект содержит метаданные, т.е. тип передаваемого файла.
            // Этот тип мы берём из самого файла
            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(file.getContentType());

            /*
             Этот объект является запросом на DO по добавлению туда нового файла.
             При создании этого запроса мы указываем имя bucket-а, уникальное имя файла,
             поток ввода из файла и метаданные, которые мы создали выше.
             А также при помощи CannedAccessControlList.PublicRead разрешаем чтение из этого файла
             для всех, у кого есть ссылка на этот файл.
             */
            PutObjectRequest request = new PutObjectRequest(
                    "shop-bucket2", uniqueName, file.getInputStream(), metaData
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            // Отправка запроса
            client.putObject(request);

            // Получение ссылки на файл
            String url = client.getUrl("shop-bucket2", uniqueName).toString();

            // Привязка загруженной картинки к продукту в базе данных
            productService.attachImage(url, productTitle);

            return url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateUniqueFileName(MultipartFile file) {
        // file -> banana.picture.jpeg
        String sourceFileName = file.getOriginalFilename();
        // Вычисляем индекс последней точки, чтобы разделить имя файла на имя и расширение
        int dotIndex = sourceFileName.lastIndexOf(".");
        // banana.picture.jpeg -> banana.picture
        String fileName = sourceFileName.substring(0, dotIndex);
        // banana.picture.jpeg -> .jpeg
        String extension = sourceFileName.substring(dotIndex);

        // banana.picture-fsf786-sdfsdf-6fsdf.jpeg
        return String.format("%s-%s%s", fileName, UUID.randomUUID(), extension);
    }
}