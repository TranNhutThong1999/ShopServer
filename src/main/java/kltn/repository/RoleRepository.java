package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findOneByName(String name);
}
