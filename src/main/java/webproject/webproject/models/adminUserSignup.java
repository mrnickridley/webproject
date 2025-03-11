package webproject.webproject.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="webproject", name="adminusersignup")
@Getter
@Setter
public class adminUserSignup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;
    
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;


    public adminUserSignup(){
        super();
}

    public adminUserSignup(int id, String firstname, String lastname, String email, String password){
        super();
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

}
