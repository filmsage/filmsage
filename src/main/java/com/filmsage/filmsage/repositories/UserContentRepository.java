package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContentRepository extends JpaRepository<UserContent, Long> {
    UserContent findUserContentByUser(User user);
//    UserContent findUserContentBy(User user);

}
