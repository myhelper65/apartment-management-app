package apartment.management.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "your-apartment-app-bucket"; // AWS'de oluşturduğunuz S3 bucket adını buraya yazın

    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public String uploadBase64Image(String base64String, String fileName) {
        if (base64String == null || base64String.isEmpty()) return null;

        // Frontend'den gelen "data:image/png;base64,iVBORw0KGgo..." ön ekini temizle
        String[] parts = base64String.split(",");
        String imageString = parts.length > 1 ? parts[1] : parts[0];

        byte[] decodedBytes = Base64.getDecoder().decode(imageString);
        String uniqueFileName = UUID.randomUUID() + "-" + fileName;

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(putOb, RequestBody.fromBytes(decodedBytes));

        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .build()).toExternalForm();
    }
}