const subButt = document.getElementById('submitButton');

function getAccountMember(){
    let userN = document.getElementById('username').value;
    let passW = document.getElementById('password').value;

    fetch('http://localhost:8099/account/' + userN + '/' + passW)
    .then(response => {
        console.log("response object:", response)
        return response.json(); // or response.text() if the response is not JSON)
    })
    .then(data => {
        console.log("this is the response data:", data);

        if(data != null){
            localStorage.setItem('username', userN)
            window.location.href = './homepage.html'
        } else {
            console.log('User not found or other condition');}
    })
    .catch(error => {
        console.error("There was an error,Bitch!:", error)
    })
}

subButt.addEventListener('click', getAccountMember);