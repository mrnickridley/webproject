package webproject.webproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import webproject.webproject.models.memberAccount;



@Repository
public interface memberRepo extends JpaRepository<memberAccount,Integer>{
    memberAccount findByUsername(String username);
    memberAccount findByPassword(String password);
}
