import("./main.mjs").then((module) => {
    let { createElm, Request } = module;
    //Creates all the html elements for the profile page
    let form = createElm({elm: "form", id: "profileForm", method: "POST"});
    let name = createElm({elm: "div"});
    let nameLabel = createElm({elm: "label", for: "name", text: "Name:"});
    let nameInput = createElm({elm: "input", type: "text", name: "name", id: "name", placeholder: "Enter name here...", required: true});
    name.append(nameLabel, nameInput);
    let newPassword = createElm({elm: "div"});
    let newPasswordLabel = createElm({elm: "label", for: "newPassword", text: "New password: (Don't fill if you don't want to change)"});
    let newPasswordInput = createElm({elm: "input", type: "password", name: "newPassword", id: "newPassword", placeholder: "Enter password here..."});
    newPassword.append(newPasswordLabel, newPasswordInput);
    let phoneNumber = createElm({elm: "div"});
    let phoneNumberLabel = createElm({elm: "label", for: "phoneNumber", text: "Phone:"});
    let phoneNumberInput = createElm({elm: "input", type: "number", name: "phoneNumber", id: "phoneNumber", placeholder: "Enter number here...", required: true});
    phoneNumber.append(phoneNumberLabel, phoneNumberInput);
    let reenterpsw = createElm({elm: "div"});
    let reenterpswLabel = createElm({elm: "label", for: "reenterpsw", text: "Re-enter password:"});
    let reenterpswInput = createElm({elm: "input", type: "password", name: "reenterpsw", id: "reenterpsw", placeholder: "Re-enter password here..."});
    reenterpsw.append(reenterpswLabel, reenterpswInput);
    let email = createElm({elm: "div"});
    let emailLabel = createElm({elm: "label", for: "email", text: "E-mail:"});
    let emailInput = createElm({elm: "input", type: "email", name: "email", id: "email", placeholder: "Enter e-mail here...", required: true});
    email.append(emailLabel, emailInput);
    form.append(name, phoneNumber, email, newPassword, reenterpsw, createElm({elm: "button", type: "submit", id: "saveChangesBtn", text: "Save changes"}));

    document.getElementById("content").append(form);

    //Uses the Request class to send a post request to the server that returns the profile information of the user and inserts them into the correct fields
    new Request("getProfile", {}, (data) => {
        console.log(data);
        nameInput.value = data["name"];
        phoneNumberInput.value = data["phone"];
        emailInput.value = data["email"];
    });

    //The profile form is submitted
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        let data = new FormData(form);
        //Uses the Request class to send a post request to the server with the new profile information
        new Request("updateProfile", {name: data.get("name"), phone: data.get("phoneNumber"), email: data.get("email")}, () => {
            if(data.get("newPassword") && data.get("reenterpsw")){
                if(data.get("newPassword") !== data.get("reenterpsw")) {
                    alert("Passwords do not match!");
                    return;
                }
                else{
                    new Request("updateProfilePassword", {password: data.get("newPassword")}, (data) => {
                        location.reload();
                    });
                }
            }
            else{
                location.reload();
            }
        });
    });
});