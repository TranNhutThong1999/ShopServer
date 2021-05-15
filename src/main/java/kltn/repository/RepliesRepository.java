package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Comment;

public interface RepliesRepository extends JpaRepository<Comment, Integer>{

}
