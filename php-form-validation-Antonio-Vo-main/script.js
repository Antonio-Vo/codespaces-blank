// Front-End Validation - scripts.js
// let email = document.getElementById('email');
// let phone = document.getElementById('phone');
// let password = document.getElementById('password').value;
// let confirm_password = document.getElementById('confirm-password');
// document.getElementById('submit-button').addEventListener("click", validateForm);
// Task 1: Validate the form on submit
// Implement a validateForm() function that will run when the form is submitted
function validateForm(){
    // Task 2: Retrieve form values
    // Get the values of the username, email, phone, password, and confirm password inputs
let username = document.getElementById('username').value;
let email = document.getElementById('email').value;
let phone = document.getElementById('phone').value;
let password = document.getElementById('password').value;
let confirm_password = document.getElementById('confirm-password').value;
    // Task 3: Validate Username - Minimum 3 characters
    if (username.length == 0){
        alert("Must input a name");
    } else if (username.length < 3){
    alert("Name must be at least 3 characters long");
    return false;
   }else{
    alert("works");
   }



    // Task 4: Validate Email - Must be a valid email format
    if (email.length == 0){
        alert("email required");
    } else if (email.match(/[\w]+@[\w].+\.([a-z]|[A-Z]){2,}/)){
        alert('vaild email');
    } else {
        alert("invaild email");
    }


    // Task 5: Validate Phone Number - 10 digits in 123-456-7890 format
    
    if (phone.match(/[\d]{3}-[\d]{3}-[\d]{4}/)){
        alert("all right");
    } else {
        alert("uh oh");
    }
    


    // Task 6: Check for NJ area codes
    



    // Task 7: Validate Password - Minimum 8 characters, 1 uppercase, 1 lowercase, 1 number, 1 special character
    
// /(?=.*[a-z])(?=.*[A-Z])(?=.*[\d])(?=.*[\!\@\#\$\%\^\&\*\_\-\+\=])(?=.{8,})/ pattern needed
    if(password.match(/(?=.*[a-z])(?=.*[A-Z])(?=.*[\d])(?=.*[\!\@\#\$\%\^\&\*\_\-\+\=])(?=.{8,})/)){
        alert("vaild!");
    } else {
        alert("bad");
    }

    // Task 8: Confirm Password Match
    // if(confirm_password === password){
    //     alert("password confirmed");
    // } else {
    //     alert("not same");
    // }



    // Task 9: If all validations pass, submit the form
}
