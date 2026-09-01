import("./main.mjs").then((module) => {
    let { createElm, Request } = module;
    import("./popUp.mjs").then((module) => {
        let { Popup } = module;
        //Uses the Request class to send a post request to the server that return an array of json objects of all the employees in the system
        new Request("getEmployees", {}, (data) => {
            //Loops through the array of json objects and creates a the appropriate html elements for each employee
            data.forEach(employee => {
                let accountContainer = createElm({elm: "div", classes: ["accBox"], id: employee["id"]});
                let accName = createElm({elm: "div", classes: ["accName"], text: employee["name"]});
                let accRole = createElm({elm: "div", text: employee["role"]});
                accountContainer.append(accName, accRole);

                accountContainer.addEventListener("click", () => new Popup("editAccount", employee["id"], createElm, Request));

                document.getElementById("accountsOverview").append(accountContainer);
            });
        });

        document.getElementById("addAccountBtn").addEventListener("click", () => new Popup("addAccount", null, createElm, Request));
    });
});