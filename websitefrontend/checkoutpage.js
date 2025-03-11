const gItem = localStorage.getItem('username');

const logout = document.getElementById('logout');
const memLogin = document.getElementById('memberCon');

document.getElementById('memberCon').textContent = gItem;
const proceed = document.getElementById("proceedCheckout");


/*-------------------*/

function getCheckout(){
        const checkoutBod = document.getElementById('checkoutBody');
        const totalPriceElement= document.getElementById('totalCount');
        const memEmail = document.getElementById('checkoutEmail');
        const memComments = document.getElementById('checkoutComments');
        const checkoutBubb = document.getElementById('cartNotification');
        const checkoutCount = document.getElementById('cartCount');

        checkoutBod.innerHTML="";

        const checkoutItems = JSON.parse(localStorage.getItem("checkoutItems"));


        let totalPrice= 0;


        checkoutItems.forEach((item, index) => {
            const row = document.createElement("tr");
            row.setAttribute("data-index", index); // Add index as a data attribute

            const nameCell = document.createElement("td");
            const priceCell = document.createElement("td");
            const currencyCell = document.createElement("td");
            const deleteB= document.createElement("button");

            nameCell.textContent = item.name;
            priceCell.textContent = item.price;
            currencyCell.textContent = "USD";
            deleteB.textContent = "Delete"
            

            row.appendChild(nameCell);
            row.appendChild(priceCell);
            row.appendChild(currencyCell);
            row.appendChild(deleteB);

            checkoutBod.appendChild(row);

            totalPrice += parseFloat(item.price);

            deleteB.onclick = function(){
                let newData = JSON.parse(localStorage.getItem("checkoutItems")) || [];
                row.remove();
                
                // Remove the corresponding item from the data
                // Remove the item at the current index
                newData.splice(index, 1); 

                // Update localStorage
                localStorage.setItem("checkoutItems", JSON.stringify(newData));
                
                // Re-render the table to refresh indices
                getCheckout();
            };
        });

        if(checkoutItems.length > 0 ){
            checkoutBubb.style.display = "flex";
            checkoutCount.textContent = checkoutItems.length;

        } else {
            checkoutBubb.style.display = 'none';
        }

            totalPriceElement.textContent = totalPrice.toFixed(2);


            proceed.textContent = 'Checkout';
            proceed.className = "proceedCheck";

            proceed.onclick= function (){ 
                fetch('http://localhost:8099/paypal-api/createOrder', {
                method:'POST',
                headers: {
                    'Content-Type':'application/json'
                },
                body: JSON.stringify({
                    amount: totalPrice.toFixed(2).toString(),
                    currency: "USD",
                    description: memComments.value,
                    payeremail: memEmail.value
                })
            })
            .then(response => response.json())
            .then(data => {
                console.log("Order Created:", data);

                // Find the approval URL in the response data
                const links = data.links;
                const approvalLink = links.find(link => link.rel === "approve")

                if(approvalLink) {
                    window.location.href = approvalLink.href;
                }else{
                    console.error("Approval URL not found in the response.");
                    alert("Unable to redirect to PayPal for approval.");
                }
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
        }

}

/*-------------------*/

function captureCheckout(){
    // Get the token from the URL
    const searchUrl = new URLSearchParams(window.location.search);
    const getToken= searchUrl.get("token");
    
    if(getToken){
        // Make a POST request to capture the payment
        fetch('http://localhost:8099/paypal-api/captureTransaction/' + getToken, {
            method:'POST',
            headers: {
                'Content-Type':'application/json'
            }
        })
        .then(response => {
            console.log("This is the response object:", response);
            return response.json();
        })
        .then(data => {
            console.log("Payment Captured!:", data);
            window.location.href="./homepage.html"
            localStorage.clear();
        })
        .catch(error => {
            console.error("There was an error with the fetch operation:", error);
        })
        }
    }
    
/*-------------------*/

memLogin.addEventListener('click', () => {
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

//localStorage.clear();
document.addEventListener("DOMContentLoaded", captureCheckout)
getCheckout();
memberNameInNav();
memberLogout();