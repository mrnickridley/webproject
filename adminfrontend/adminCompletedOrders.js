const completedOrderBubb= document.getElementById('completeOrdersNotification');
const orderCount = document.getElementById('orderCount');

function getAllTransactions(){
    fetch('http://localhost:8099/inventory/getTrans')
    .then(response => {
        console.log("This is the response object:", response);
        return response.json();
    })
    .then(transactions => {
        const completedBody = document.getElementById("adminCompletedOrderBody");
        completedBody.innerHTML = "";

        transactions.forEach(transaction => {
            const row = document.createElement("tr");

            const cellTransId = document.createElement("td");
            const cellStatus = document.createElement("td");
            const cellCustomerName = document.createElement("td");
            const cellEmail = document.createElement("td");
            const cellAmount = document.createElement("td");
            const cellShippingAddress = document.createElement("td");
            const cellTimeOfTrans = document.createElement("td");
            const cellComplete = document.createElement("td");


            cellTransId.textContent = transaction.transactionId;
            cellStatus.textContent = transaction.status;
            cellCustomerName.textContent = transaction.customerName;
            cellEmail.textContent = transaction.payerEmail;
            cellAmount.textContent = transaction.priceAmount;
            cellShippingAddress.textContent = transaction.shippingAddress;
            cellTimeOfTrans.textContent = transaction.timeOfTransaction;

            const completebutton = document.createElement("button");
            completebutton.textContent = "Order Completed";
            cellComplete.appendChild(completebutton);
            
            completebutton.onclick = function(){
                completeTransaction();
                row.remove(transaction.id);
            }
    

            row.appendChild(cellTransId);
            row.appendChild(cellStatus);
            row.appendChild(cellCustomerName);
            row.appendChild(cellEmail);
            row.appendChild(cellAmount);
            row.appendChild(cellShippingAddress);
            row.appendChild(cellTimeOfTrans);
            row.appendChild(cellComplete);

            completedBody.appendChild(row)

            function completeTransaction(){
                const identityCell = transaction.id;
    
                fetch('http://localhost:8099/inventory/deleteTrans/' + identityCell, {
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

    function updateTrans(){
        if(transactions.length > 0 ){
            completedOrderBubb.style.display = "flex";
            orderCount.textContent = transactions.length;

            localStorage.setItem("completedTransactions", transactions.length);
        } else {
            completedOrderBubb.style.display = 'none';
        }
    }

        updateTrans();

    })
    .catch(error => {
        console.error("There was an error with the fetch operation:", error)
    })
}

getAllTransactions();