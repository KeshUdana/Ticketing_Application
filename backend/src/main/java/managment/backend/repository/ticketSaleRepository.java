package managment.backend.repository;

import managment.backend.persistence.TicketSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSaleRepository extends JpaRepository<TicketSales, Long> {
    Object save(TicketSales ticketSales);
    List<TicketSales> findAll();
}
