package webproject.webproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import webproject.webproject.models.payPalTransactionModel;

@Repository
public interface payPalTransactionRepo extends JpaRepository<payPalTransactionModel, Integer>{
    
}
