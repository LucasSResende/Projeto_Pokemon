package br.inatel.gameproject.model.repository;

import br.inatel.gameproject.model.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GameRepository extends JpaRepository<Game, String >{
        Page<Game> findByPublisherId(String publisherId, Pageable pageable);

}
