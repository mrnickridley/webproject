package webproject.webproject.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webproject.webproject.models.adminInventory;
import webproject.webproject.models.payPalTransactionModel;
import webproject.webproject.repositories.adminInventoryRepo;
import webproject.webproject.repositories.payPalTransactionRepo;

@RestController
@RequestMapping(path="/inventory")
public class adminInventoryController {

    @Autowired
    adminInventoryRepo repo;

    @Autowired 
    payPalTransactionRepo transRepo;

    @PostMapping(path="/saveUnit")
    @CrossOrigin
    public ResponseEntity<adminInventory> addUnit(@RequestBody adminInventory product) throws URISyntaxException{
        adminInventory add = repo.save(product);
        return ResponseEntity.created(new URI("/productinventory/saveProduct" + add.getUnitname())).body(add);
    }

    @GetMapping(path="/allUnits")
    @CrossOrigin
    public List<adminInventory> listAllUnits(){
        return repo.findAll();
    }

    @GetMapping(path="getPhoto/{id}")
    @CrossOrigin
    public String findProductPhotoById(@PathVariable int id){
        adminInventory product = repo.findById(id);
        return product != null ? product.getPhotopath() : null;
    }

    @GetMapping(path="/{id}")
    @CrossOrigin
    public adminInventory findProductById(@PathVariable int id){
        return repo.findById(id);
    }

    @GetMapping(path="/getTrans")
    @CrossOrigin
    public List<payPalTransactionModel> getAllTrans(){
        return transRepo.findAll();
    }

    @DeleteMapping(path="/deleteUnit/{id}")
    @CrossOrigin
    public ResponseEntity<?> deleteUnits(@PathVariable int id){
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path="/deleteTrans/{id}")
    @CrossOrigin
    public ResponseEntity<?> deleteTrans(@PathVariable int id){
        transRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
