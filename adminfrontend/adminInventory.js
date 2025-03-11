const submitButt = document.getElementById("submitUnit");


const completedOrderBubb = document.getElementById('coNotification');
const orderCount = document.getElementById('orderCount');

function getAllTransactionsForNotify(){
    fetch('http://localhost:8099/inventory/getTrans')
    .then(response => {
        console.log("This is the response object:", response);
        return response.json();
    })
    .then(transactions => {
            if(transactions.length > 0){
                completedOrderBubb.style.display = 'flex';
                orderCount.textContent = transactions.length;
            } else{
                completedOrderBubb.style.display = 'none';
            }
        })
    .catch(error => {
        console.error("There was an error with the fetch operation:", error)
    })

}

function saveUnit(){
    let uName = document.getElementById("unitname").value;
    let uPrice = document.getElementById("unitprice").value;
    let uCatgeory = document.getElementById("unitcategory").value;
    let uDescription = document.getElementById("unitdescription").value;
    let uSex = document.getElementById("unitsex").value;
    let uAmount = document.getElementById("amountofunits").value;
    let pPath = document.getElementById("photopath").value;

    fetch('http://localhost:8099/inventory/saveUnit', {
        method:'POST',
        headers: {
            'Content-Type':'application/json'
        },
        body: JSON.stringify({
            unitname: uName,
            unitdescription: uDescription,
            unitcategory: uCatgeory,
            unitprice: uPrice,
            unitsex:uSex,
            amountofunits: uAmount,
            photopath: pPath
        })
    })
    .then(response => {
        console.log("This is the response object:", response);
        return response.json();
    })
    .then(data => {
        console.log("Unit saved:", data);
        reloadPage();
    })
    .catch(error => {
        console.error("There was an error with the fetch operation:", error);
    })
}

function getAllUnits(){
    fetch('http://localhost:8099/inventory/allUnits')
    .then(response => {
        console.log("This is the response object:", response);
        return response.json();
    })
    .then(units => {
        const inDaBody = document.getElementById("inventoryDataBody");
        inDaBody.innerHTML="";

        units.forEach(unit => {
        const row = document.createElement("tr");

        const idCell = document.createElement("td");
        const uNameCell = document.createElement("td");
        const uPriceCell = document.createElement("td");
        const uCategoryCell = document.createElement("td");
        const uDescriptionCell = document.createElement("td");
        const uSexCell = document.createElement("td");
        const uAmountCell = document.createElement("td");
        const pPathCell = document.createElement("td");
        const deleteCell = document.createElement("td");

        idCell.textContent = unit.id;
        uNameCell.textContent = unit.unitname;
        uPriceCell.textContent = unit.unitprice.toFixed(2);
        uCategoryCell.textContent = unit.unitcategory;
        uDescriptionCell.textContent = unit.unitdescription;
        uSexCell.textContent = unit.unitsex;
        uAmountCell.textContent = unit.amountofunits;
        pPathCell.textContent = unit.photopath;


        const deleteButt = document.createElement("button");
        deleteButt.textContent = "Delete";
        deleteCell.appendChild(deleteButt);
        deleteButt.onclick = function(){
            deleteUnit();
            row.remove(unit.id);
        }

        row.appendChild(idCell);
        row.appendChild(uNameCell);
        row.appendChild(uDescriptionCell);
        row.appendChild(uCategoryCell);
        row.appendChild(uPriceCell);
        row.appendChild(uSexCell);
        row.appendChild(uAmountCell);
        row.appendChild(pPathCell);
        row.appendChild(deleteCell);


        inDaBody.appendChild(row);

        function deleteUnit(){
            const idCell = unit.id;

            fetch('http://localhost:8099/inventory/deleteUnit/' + idCell, {
                method:'DELETE',
                header:{
                    'Content-Type': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {
                console.log("Unit deleted:", data)
            })
            .catch(error => {
                console.error("There was an error with the fetch operation:", error)
        })
    }

        })
    })
    .catch(error => {
        console.error("There was an error with the fetch operation:", error)
    })
}



function reloadPage(){
    location.reload();
}

document.addEventListener("DOMContentLoaded", function () {
    getAllTransactionsForNotify();
});
getAllUnits();
submitButt.addEventListener("click", saveUnit);
