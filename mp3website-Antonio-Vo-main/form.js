// This script adds an event listener to all forms on the page. PHP functionality is not implemented yet.
// It checks if the corresponding textarea is empty when the form is submitted.
// If the textarea is empty, it prevents the form submission and shows an alert.
document.addEventListener('DOMContentLoaded', () => {
    const forms = document.querySelectorAll('form'); // Select all forms
    const textareas = document.querySelectorAll('textarea'); // Select all textareas

    forms.forEach((form, index) => {
        form.addEventListener('submit', (event) => {
            event.preventDefault(); // Prevent the default form submission behavior

            // Check if the corresponding textarea is empty
            const textarea = textareas[index];
            if (!textarea || textarea.value.trim() === "") {
                alert('Error: The form cannot be empty!');
            } else {
                alert('Form submitted successfully!');
            }
        });
    });
});


//check if the screen/window is small
var x = window.innerWidth;

if (x < 1000){
    alert("Small screen/window detected. Please use a larger window for better experience.");
};