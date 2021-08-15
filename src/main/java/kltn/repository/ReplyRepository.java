package kltn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Comment;
import kltn.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
}
