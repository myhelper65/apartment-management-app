package apartment.management.dto;

public class ExpenseItemDTO {
    private String title;
    private Double price;
    private String notes;
    private String date;
    private String productImageBase64;
    private String invoiceImageBase64;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductImageBase64() {
        return productImageBase64;
    }

    public void setProductImageBase64(String productImageBase64) {
        this.productImageBase64 = productImageBase64;
    }

    public String getInvoiceImageBase64() {
        return invoiceImageBase64;
    }

    public void setInvoiceImageBase64(String invoiceImageBase64) {
        this.invoiceImageBase64 = invoiceImageBase64;
    }

    // Getters ve Setters...
}