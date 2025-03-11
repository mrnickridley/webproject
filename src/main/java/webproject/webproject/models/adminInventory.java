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
@Table(schema="webproject", name="admininventory")
@Getter
@Setter
public class adminInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name="unitname")
    private String unitname;

    @Column(name="unitprice")
    private double unitprice;

    @Column(name="unitcategory")
    private String unitcategory;

    @Column(name="unitdescription")
    private String unitdescription;

    @Column(name="unitsex")
    private String unitsex;

    @Column(name="amountofunits")
    private int amountofunits;

    @Column(name="photopath")
    private String photopath;

    public adminInventory(){
        super();
    }
    
    public adminInventory(int id, String unitname, double unitprice, String unitcategory, String unitdescription, String unitsex, int amountofunits, String photopath){
            super();
            this.id = id;
            this.unitname = unitname;
            this.unitprice = unitprice;
            this.amountofunits = amountofunits;
            this.photopath = photopath;
            this.unitcategory = unitcategory;
            this.unitsex = unitsex;
            this.unitdescription = unitdescription;
        }
}
