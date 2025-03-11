package webproject.webproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import webproject.webproject.models.payPalModel;



@Repository
public interface paypalRepo extends JpaRepository<payPalModel, Integer> {
    payPalModel findByOrderId(String orderId);
}
