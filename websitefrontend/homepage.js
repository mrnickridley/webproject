/*NEW CODE WHICH RETRIEVES DATA FROM DB AND 
SORTS THE PRICES WHEN CHANGING THE HIGHTOLOW SORT BOX*/
const memlogin = document.getElementById('memberCon');
const logout = document.getElementById('logout');

const checkoutBubb = document.getElementById("cartNotification");
const checkoutCount = document.getElementById("cartCount"); 

const gItem = localStorage.getItem('username');

document.getElementById('memberCon').textContent = gItem;
document.getElementById('logout');

const list = document.getElementById('productList');
const list2 = document.getElementById('productPreview')

const select = document.getElementById('lowHighSelect');
const select2 = document.getElementById('himHerSelect');
const select3 = document.getElementById('categorySelect');
const select4 = document.getElementById('brandSelect');

let products = [];
let products2 = [];
let products3 =[];
let products4=[];

const existingData=JSON.parse(localStorage.getItem("checkoutItems")) || [];

document.addEventListener("DOMContentLoaded", function() {
    if(existingData.length > 0){
        checkoutBubb.style.display = 'flex';
        checkoutCount.textContent = existingData.length;
    } else{
        checkoutBubb.style.display = 'none';
    }
})
/*-------------------*/

memlogin.addEventListener('click', () => {
    if(logout.style.display === 'none'){
        logout.style.display = 'block';
    } else {
        logout.style.display = 'none';
    }
})

/*-------------------*/

function memberNameInNav(){
    if(localStorage.getItem('username')){
        document.getElementById('memLink').style.display = "none";
        document.getElementById('memberCon').style.display = "block";
        }
}

/*-------------------*/

function memberLogout(){
    logout.addEventListener('click', () => {
        localStorage.removeItem('username');
        location.reload();
    })
}

/*-------------------*/

function fetchProduct(){
fetch('http://localhost:8099/inventory/allUnits')
.then(response => {
    console.log("response object:", response)
    return response.json();
})
.then(data => {
    products = data;
    products2 = data;
    products3 = data;
    products4 = data;
    productRender(products);
    productRender(products2);
    productRender(products3);
    productRender(products4);
})
.catch(error => {
    console.error("There was an error with the fetch operation:", error);
})
}

/*-------------------*/

function productRender(data){
    list.innerHTML = '';

    data.forEach(product => {
        const div1 = document.createElement('div');
        div1.className="productDiv"

        // Create a list item
        const listItem = document.createElement('li');

         // Create and set the image
        const image = document.createElement('img');
        image.src = product.photopath; 
        image.alt = 'Photo';

        // Create and set the name
        const name = document.createElement('h3');
        name.textContent = product.unitname;

        const descript = document.createElement('p')
        descript.textContent = product.unitdescription;

        // Create and set the price
        const price = document.createElement('p');
        price.textContent = `Price: $${product.unitprice}`;

        // Create and set the checkout button
        const checkoutButton = document.createElement('button');
        checkoutButton.textContent = "checkout";
        checkoutButton.className = "cButt"

        // Append elements to the list item
        div1.appendChild(listItem);
        listItem.appendChild(image);
        listItem.appendChild(name);
        listItem.appendChild(descript);
        listItem.appendChild(price);
        listItem.appendChild(checkoutButton);

        // Append the list item to the list
        list.appendChild(div1);

        image.onclick = function(){
            list2.classList.add("active");
            productPopup(product);
        }

        
        checkoutButton.onclick = function(){

            existingData.push({
                name: product.unitname,
                price: product.unitprice
            })
            
            localStorage.setItem('checkoutItems', JSON.stringify(existingData));
            

            function updatedNotify() {
            if(existingData.length > 0 ){
                checkoutBubb.style.display = "flex";
                checkoutCount.textContent = existingData.length;
            } else {
                checkoutBubb.style.display = "none";
            }
        }

        updatedNotify();

        window.addEventListener("storage", updatedNotify);
    }

    })
}

/*-------------------*/

function productPopup(product){
    list2.innerHTML = "";

    const divT = document.createElement('div');
    divT.className = "diviT";

    const buttonT = document.createElement('button');
    buttonT.textContent = 'X';
    buttonT.className = "bT";
    // Create and set the image
    const image = document.createElement('img');
    image.src = product.photopath;
    image.alt = 'Photo';
    

    // Create and set the name
    const name = document.createElement('h3');
    name.textContent = product.unitname;

    // Create and set the price
    const price = document.createElement('p');
    price.textContent = `Price: $${product.unitprice}`;

    // Create and set the checkout button
    const checkoutButt= document.createElement('button');
    checkoutButt.textContent = "checkout";
    checkoutButt.className = "cButt"

    divT.appendChild(image);
    divT.appendChild(name);
    divT.appendChild(price);
    divT.appendChild(checkoutButt);

    list2.appendChild(buttonT);
    list2.appendChild(divT);

    buttonT.onclick = function(){
        list2.classList.remove("active");
    }

    checkoutButt.onclick = function(){

        existingData.push({
            name: product.unitname,
            price: product.unitprice
        })
        
        localStorage.setItem('checkoutItems', JSON.stringify(existingData));

        function updatedPopUpNotify() {
            if(existingData.length > 0 ){
                checkoutBubb.style.display = "flex";
                checkoutCount.textContent = existingData.length;
            } else {
                checkoutBubb.style.display = "none";
            }
        }

        updatedPopUpNotify();

        window.addEventListener("storage", updatedPopUpNotify);
    }
}

/*-------------------*/

function productSort(lowHighOptions){
    let sortedP;
    if(lowHighOptions === "LowToHigh"){
        sortedP = [...products].sort((a, b) => a.unitprice - b.unitprice);
    } else if(lowHighOptions === "HighToLow"){
        sortedP = [...products].sort((a, b) => b.unitprice - a.unitprice);
    } 
    else{
        sortedP=products;
    }
    productRender(sortedP);
}

/*-------------------*/

function himHerSort(himHerOptions){
    let sorted;
    if(himHerOptions === "Him"){
        sorted = products2.filter(a => a.unitsex === "Male");
    } else if(himHerOptions === "Her"){
        sorted = products2.filter(a => a.unitsex === "Female");
    } else if(himHerOptions === "Both"){
        sorted = products2.filter(a => a.unitsex === "Both");
    }
    else{
        sorted=products2;
    }
    productRender(sorted);
}

/*-------------------*/

function categorySort(categoryOptions){
    let sort;
    if(categoryOptions === "Accessories"){
        sort = products3.filter(a => a.unitcategory === "Accessories");
    } else if(categoryOptions === "Body Wash"){
        sort = products3.filter(a => a.unitcategory === "Body Wash");
    } else if(categoryOptions === "Dental"){
        sort = products3.filter(a => a.unitcategory === "Dental");
    } else if(categoryOptions === "Deoderant"){
        sort = products3.filter(a => a.unitcategory === "Deoderant");
    } else if(categoryOptions === "Lotion"){
        sort = products3.filter(a => a.unitcategory === "Lotion");
    } else if(categoryOptions === "Shampoo"){
        sort = products3.filter(a => a.unitcategory === "Shampoo");
    } else if(categoryOptions === "Soap"){
        sort = products3.filter(a => a.unitcategory === "Soap");
    }else{
        sort = products3;
    }
    productRender(sort);
}

/*-------------------*/

function brandSort(brandOptions){
    let sortB;
    if(brandOptions === "Dr. Bronner's"){
        sortB = products4.filter(a => a.unitname === "Dr. Bronner's");
    } else if(brandOptions === "Mrs. Meyer's"){
        sortB = products4.filter(a => a.unitname === "Mrs. Meyer's");
    }else if(brandOptions === "Native"){
        sortB = products4.filter(a => a.unitname === "Native");
    }else if(brandOptions === "EOS"){
        sortB = products4.filter(a => a.unitname === "EOS");
    }else{
        sortB = products4;
    }
    productRender(sortB);
}

/*-------------------*/

select.addEventListener('change', function(){
    const selected = this.value;
    productSort(selected);
});

select2.addEventListener('change', function(){
    const selected2 = this.value;
    himHerSort(selected2);
});

select3.addEventListener('change', function(){
    const selected3 = this.value;
    categorySort(selected3);
});

select4.addEventListener('change', function(){
    const selected4 = this.value;
    brandSort(selected4);
});

memberNameInNav();
memberLogout();
fetchProduct();
