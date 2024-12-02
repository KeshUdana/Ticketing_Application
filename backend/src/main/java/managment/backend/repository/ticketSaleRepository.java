package managment.backend.repository;

import managment.backend.persistence.TicketSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketSaleRepository extends JpaRepository<TicketSales, Long> {
    // JpaRepository already provides methods like save, findById, findAll, deleteById, etc.
}
