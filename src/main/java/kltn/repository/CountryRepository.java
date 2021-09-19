package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Country;

public interface CountryRepository extends JpaRepository<Country, String>{

}
