package apartment.management.repository; // Kendi paket adınla aynı olduğundan emin ol

import apartment.management.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> { // String yerine Long yaptık

    // İçinde en az 1 tane expense/harcama (items) olan daireleri getirir
    @Query("SELECT a FROM Apartment a WHERE a.items IS NOT EMPTY")
    List<Apartment> findApartmentsWithItems();
    
}