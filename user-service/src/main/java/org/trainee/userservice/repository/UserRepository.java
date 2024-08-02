package org.trainee.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trainee.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
