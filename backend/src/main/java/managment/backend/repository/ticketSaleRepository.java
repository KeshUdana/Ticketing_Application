package managment.backend.repository;

import managment.backend.persistence.TicketSales;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSaleRepository extends CrudRepository<TicketSales, Long> {
    List<TicketSales> findAll();

}
