package apartment.management.model;

import jakarta.persistence.*;

@Entity
public class ExpenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double price;
    private String notes;
    private String date;
    private String productImageUrl;
    private String invoiceImageUrl;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    @com.fasterxml.jackson.annotation.JsonBackReference // Döngüyü kırmak için bunu ekle
    private Apartment apartment;

    public ExpenseItem() {
    }

    // Yeni eklenen alanın Getter ve Setter'ı
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    // Mevcut diğer tüm Getter/Setter'ların aynen kalacak...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
    public String getInvoiceImageUrl() { return invoiceImageUrl; }
    public void setInvoiceImageUrl(String invoiceImageUrl) { this.invoiceImageUrl = invoiceImageUrl; }
}