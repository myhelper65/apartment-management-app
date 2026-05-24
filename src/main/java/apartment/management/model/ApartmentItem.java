package apartment.management.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ApartmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Harcamalar otomatik artsın (1, 2, 3...)
    private Long id;

    private String title;
    private double price;

    @Lob
    @Column(columnDefinition = "LONGTEXT") // Uzun base64 resim verilerini saklayabilmek için
    private String productImage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String invoiceImage;

    private String notes;
    private String date;
}