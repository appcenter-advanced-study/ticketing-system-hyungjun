package com.appcenter.wnt.repository;

import com.appcenter.wnt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
