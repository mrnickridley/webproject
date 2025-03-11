package webproject.webproject.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webproject.webproject.models.memberAccount;
import webproject.webproject.repositories.memberRepo;

@RestController
@RequestMapping(path="/account")
public class memberController {
    @Autowired
    memberRepo repo;
    
    //for Sign Up Page
    @PostMapping(path="/createAccount")
    @CrossOrigin
    public ResponseEntity<memberAccount> addUser(@RequestBody memberAccount newmember) throws URISyntaxException{
        memberAccount create = repo.save(newmember);
        return ResponseEntity.created(new URI("/productinventory/saveProduct" + create.getEmail())).body(create);
    }

    @GetMapping(path="/{username}/{password}")
    @CrossOrigin
    public ResponseEntity<memberAccount> getUserByUsernameAndPassword(@PathVariable String username, @PathVariable String password){
        memberAccount member = repo.findByUsername(username);
        member = repo.findByPassword(password);
        
        if(member != null){
            return ResponseEntity.ok(member);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
