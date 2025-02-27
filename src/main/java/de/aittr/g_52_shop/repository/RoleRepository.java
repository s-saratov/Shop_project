package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<User, Long> {

}
