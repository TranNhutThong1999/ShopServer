package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Address;
import kltn.entity.Banner;
import kltn.entity.District;

public interface AddressRepository extends JpaRepository<Address, Integer>{
}
