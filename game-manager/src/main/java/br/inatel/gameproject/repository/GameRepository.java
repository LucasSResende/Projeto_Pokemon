package br.inatel.gameproject.repository;

import br.inatel.gameproject.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {//[Adauto] - Your game entity has id as UUID and not String
    List<Game> findByPublisherId(String publisherId);

}