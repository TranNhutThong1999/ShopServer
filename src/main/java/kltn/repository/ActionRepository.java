package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Action;
import kltn.entity.Banner;

public interface ActionRepository extends JpaRepository<Action, Integer>{
}
