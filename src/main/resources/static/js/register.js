document.getElementById('registerForm').addEventListener("submit", function(event){
    event.preventDefault();
    const formData = new FormData(this);
    const agreeTerm = document.getElementById('checkbox').checked;
    if(!agreeTerm){
        alert("You must agree to the terms and conditions.");
        return;
    }
    const user = {
        userName : formData.get('userName'),
        password : formData.get('password'),
        confirmPassword : formData.get('confirmPassword'),
        address : formData.get('address'),
        email : formData.get('email'),
        phoneNumber : formData.get('phoneNumber'),
    };

    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
    .then(response => response.json())
    .then(data => {
        if(data.success){
            alert("Registration Successfull");
        }
        else{
            alert("Registration Failed"+ data.message);
        }
    })
    .catch(error => console.error("Error: "+ error));
})