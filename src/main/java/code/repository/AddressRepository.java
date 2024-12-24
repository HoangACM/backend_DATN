package code.repository;

import code.model.entity.Address;
import code.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUser(User user);
  Address findByDefaultAddress(boolean defaultAddress);
}
