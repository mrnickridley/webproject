const submitButton = document.getElementById("submitButt");

function getAdminUser(){
    //".value" gets the input value
    let eMail = document.getElementById("email").value; 
    let pWord = document.getElementById("password").value;

    fetch('http://localhost:8099/admin/' + eMail + '/' + pWord)
    .then(response => {
        console.log("response object:", response)
        return response.json(); // or response.text() if the response is not JSON
    })
    .then(data => {
        console.log("this is the response data:", data);

        if(data != null){
            window.location.href = './adminInventoryHome.html'
        } else {
            console.log('User not found or other condition');}
    })
    .catch(error => {
        console.error("There was an error,Bitch!:", error)
    })
}

submitButton.addEventListener('click', getAdminUser);