package apartment.management.controller;

import apartment.management.dto.ExpenseItemDTO;
import apartment.management.model.Apartment;
import apartment.management.model.ExpenseItem;
import apartment.management.repository.ApartmentRepository;
import apartment.management.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@CrossOrigin(origins = "*") // Saha çalışanının UI'dan erişimi için CORS izni aktif
public class ApartmentController {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private S3Service s3Service;

    @GetMapping
    public List<Apartment> getAll() {
        // Sadece içi dolu olan daireleri UI'a gönderiyoruz
        return apartmentRepository.findApartmentsWithItems();
    }

    @PostMapping
    public Apartment createApartment(@RequestBody Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    // UI'DAN (ÇALIŞANDAN) GELEN HARCAMAYI EKLEME
    @PostMapping("/{apartmentId}/items")
    public Apartment addItem(@PathVariable Long apartmentId, @RequestBody ExpenseItemDTO itemDto) {
        Apartment apt = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Daire bulunamadı"));

        ExpenseItem item = new ExpenseItem();
        item.setTitle(itemDto.getTitle());
        item.setPrice(itemDto.getPrice());
        item.setNotes(itemDto.getNotes());
        item.setDate(itemDto.getDate());

        if (itemDto.getProductImageBase64() != null) {
            String url = s3Service.uploadBase64Image(itemDto.getProductImageBase64(), "product.jpg");
            item.setProductImageUrl(url);
        }
        if (itemDto.getInvoiceImageBase64() != null) {
            String url = s3Service.uploadBase64Image(itemDto.getInvoiceImageBase64(), "invoice.jpg");
            item.setInvoiceImageUrl(url);
        }

        item.setApartment(apt);
        apt.getItems().add(item);

        return apartmentRepository.save(apt);
    }

    // UI'DAN (ÇALIŞANDAN) GELEN SİLME İSTEĞİ (DÜZELTİLDİ)
    @DeleteMapping("/{apartmentId}/items/{itemId}")
    public ResponseEntity<?> deleteApartmentItem(
            @PathVariable Long apartmentId,
            @PathVariable Long itemId) {

        try {
            // 1. Önce hangi dairede olduğumuzu buluyoruz
            Apartment apt = apartmentRepository.findById(apartmentId)
                    .orElseThrow(() -> new RuntimeException("Daire bulunamadı"));

            // 2. O dairenin harcamaları (items) arasından bizim sildiğimiz ID'yi bulup listeden çıkartıyoruz
            boolean isRemoved = apt.getItems().removeIf(item -> item.getId().equals(itemId));

            if (isRemoved) {
                // 3. Daireyi tekrar kaydediyoruz. Cascade.ALL ayarı sayesinde listeden çıkan harcama DB'den de siliniyor.
                apartmentRepository.save(apt);

                // UI'ın (React/JS) hata fırlatmaması için geçerli bir JSON objesi dönüyoruz
                return ResponseEntity.ok().body("{\"message\": \"Harcama başarıyla silindi.\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"Silinmek istenen harcama bulunamadı.\"}");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}