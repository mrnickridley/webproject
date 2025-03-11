package webproject.webproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import webproject.webproject.models.adminUserSignup;


@Repository
public interface adminUserRepo extends JpaRepository<adminUserSignup, Integer> {
    adminUserSignup findByEmail(String email);
    adminUserSignup findByPassword(String password);
    
}

