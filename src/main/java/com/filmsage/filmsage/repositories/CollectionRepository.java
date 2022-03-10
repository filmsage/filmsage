package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
