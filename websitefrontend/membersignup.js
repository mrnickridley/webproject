const submit = document.getElementById("bSubmit");

function saveMember(){
    let firstN = document.getElementById("firstname").value;
    let lastN = document.getElementById("lastname").value;
    let eMail = document.getElementById("email").value;
    let uName = document.getElementById("username").value;
    let pWord = document.getElementById("password").value;

    fetch('http://localhost:8099/account/createAccount', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstname: firstN,
            lastname: lastN,
            email: eMail,
            username: uName,
            password: pWord
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log("Expense saved:", data);
        reloadPage(); // Refresh the page after saving the expense
    })
}

function reloadPage(){
    location.reload();
}

submit.addEventListener('click', saveMember);
