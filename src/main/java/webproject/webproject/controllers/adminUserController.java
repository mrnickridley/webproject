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

import webproject.webproject.models.adminUserSignup;
import webproject.webproject.repositories.adminUserRepo;

@RestController
@RequestMapping(path="/admin")
public class adminUserController {

    @Autowired
    adminUserRepo userRepo;

    //for Sign Up Page
    @PostMapping(path="/saveUser")
    @CrossOrigin
    public ResponseEntity<adminUserSignup> addUser(@RequestBody adminUserSignup user) throws URISyntaxException{
        adminUserSignup add = userRepo.save(user);
        return ResponseEntity.created(new URI("/productinventory/saveProduct" + add.getEmail())).body(add);
    }

    @GetMapping(path="/findAll")
    @CrossOrigin
    public List<adminUserSignup> findAllUsers(){
        return userRepo.findAll();
    }
    
    //for Log-In Page
    @GetMapping(path="/{email}/{password}")
    @CrossOrigin
    public ResponseEntity<adminUserSignup> getUserByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        adminUserSignup user = userRepo.findByEmail(email);
        user = userRepo.findByPassword(password);
        
        if(user != null){
            return ResponseEntity.ok(user);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(path="/deleteUser/{id}")
    @CrossOrigin
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        userRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
