package managment.backend.repository;


import managment.backend.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface VendorRepository extends JpaRepository<Vendor, BigInteger> {


}
