[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=19572713)
# User Profile Form Project - Step 1: Basic Form and Front-End Validation

## Project Overview

In this project, you will create a complete user profile form using HTML, CSS, JavaScript, PHP, and SQLite. By the end of the project, you will have a robust signup and login system that securely handles user data. The first step focuses on building the initial form structure and implementing front-end validation.

## Step 1 Objectives

By the end of this step, you should be able to:

* Create a basic HTML form for user profile creation
* Implement front-end (JavaScript) validation for each input field
* Provide clear and meaningful feedback for user input errors
* Use appropriate HTML attributes and CSS for form styling

## Assignment Instructions

### 1. Project Setup

* I have created a GitHub repository for this project with the following files already set up:

  * **index.html** - The main HTML file for your form
  * **style.css** - A CSS file for styling your form
  * **script.js** - A JavaScript file for adding front-end validation

### 2. Form Structure

* Add the following fields to your `index.html` file using the specified IDs:

  * **Username** (`id="username"`): Minimum 3 characters
  * **Email** (`id="email"`): Must be in a valid email format
  * **Phone Number** (`id="phone"`): 10 digits in the format 123-456-7890
  * **Password** (`id="password"`): Minimum 8 characters, at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character
  * **Confirm Password** (`id="confirm-password"`): Must match the Password field
* Add a submit button and an empty `div` for displaying error messages (`id="error-message"`).
* Use semantic HTML5 elements and proper form structure for accessibility.

### 3. Adding Front-End Validation

* Use the `script.js` file to add a `validateForm()` function.
* Implement the following validation rules:

  * Username must be at least 3 characters long.
  * Email must be in a valid email format.
  * Phone number must be exactly 10 digits in the format 123-456-7890.
  * Password must be at least 8 characters long, including 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character.
  * Confirm Password must match the Password field.
  * Check for New Jersey area codes (`201`, `551`, `609`, `732`, `848`, `856`, `862`, `908`, `973`) and log a message if one is detected.
* Use the `error-message` div to display any error messages.

### 4. Testing Your Form

* Test each validation rule separately to ensure it works as expected.
* Make sure your error messages are clear and informative.
* Ensure the form only submits when all inputs are valid.

### 5. Submitting Your Work

* Commit and push your changes to the repository after each significant change.


## Grading Criteria

* Correct form structure and HTML attributes
* Effective front-end validation
* Clear and professional form styling (if included)
* Meaningful and user-friendly error messages
* Clean, well-organized, and properly commented code

## Next Steps

Once you have completed Step 1, we will move on to Step 2 together, where we will add database integration to securely store user data.
