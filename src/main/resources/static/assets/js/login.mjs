import("./main.mjs").then((module) => {
    let {createElm, Request} = module;
    //The login form is submitted
    document.getElementById("loginForm").addEventListener("submit", (e) => {
        e.preventDefault();
        let usernameField = document.getElementById("username");
        let passwordField = document.getElementById("password");
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
        if(localStorage.getItem("username") != null){
            usernameField.value = localStorage.getItem("username");
        }
        if(localStorage.getItem("password") != null){
            passwordField.value = localStorage.getItem("password");
        }
        //Uses the Request class to send a post request to the server with a login detailes that determines if the login was successful
        new Request("login", {username, password}, (data) => {
            if(data == "login failed"){
                //If the login was unsuccessful a message is displayed to the user
                document.getElementById("loginForm").append(createElm({elm: "p", id: "loginFailed", text: "Login failed"}));
            }
            else{
                //If successful the username and password is saved in local storage and the user is redirected to the home page
                localStorage.setItem("username", username);
                localStorage.setItem("password", password);
                location.href = "/";
            }
        });
    });
});
