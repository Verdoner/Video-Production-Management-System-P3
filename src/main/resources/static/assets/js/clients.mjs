import("./main.mjs").then((module) => {
    let { createElm, Request, confirmBox } = module;
    import("./popUp.mjs").then((module) => {
        let { Popup } = module;
        //Uses the Popup class to create a popup with the ability to create clients
        document.getElementById("addClientBtn").addEventListener("click", () => new Popup("addClient", null, createElm, Request));
        import("./lists.mjs").then((module) => {
            let { List } = module;
            //Creates a list of all clients with a parameter to determine which clients to show based on their state
            let insertClients = (page) => {
                new Request("getAllClients", {}, (data) => {
                    data.forEach(client => {
                        let row = createElm({ elm: "tr" });
                        let removeBtn = createElm({ elm: "td", classes: "removeBtn", text: '<svg class="svgButtons" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="currentColor" d="M256 48a208 208 0 1 1 0 416 208 208 0 1 1 0-416zm0 464A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c-9.4 9.4-9.4 24.6 0 33.9l47 47-47 47c-9.4 9.4-9.4 24.6 0 33.9s24.6 9.4 33.9 0l47-47 47 47c9.4 9.4 24.6 9.4 33.9 0s9.4-24.6 0-33.9l-47-47 47-47c9.4-9.4 9.4-24.6 0-33.9s-24.6-9.4-33.9 0l-47 47-47-47c-9.4-9.4-24.6-9.4-33.9 0z" /></svg>' });
                        let editBtn = createElm({ elm: "td", classes: "clickable", text: client["name"] });
                        row.append(
                            editBtn,
                            createElm({ elm: "td", classes: ["number", "client"], text: phoneFormatter(client["phone"]) }),
                            createElm({ elm: "td", classes: ["mail", "client"], text: client["email"] }),
                            createElm({ elm: "td", classes: ["chance", "client"], text: client["chance"] }),
                            removeBtn
                        );

                        if (
                            (page == "archived" && client["state"] == "archived") ||
                            (page == "current" && client["state"] == "active") ||
                            (page == "all")
                        ) list.table.append(row);

                        removeBtn.addEventListener("click", () => {
                            confirmBox("Are you sure you want to delete this client?", () => {
                                new Request("deleteClient", { id: client["id"] }, () => {
                                    row.remove();
                                    confirmBox.remove();
                                });
                            });
                        });

                        editBtn.addEventListener("click", () => {
                            new Popup("editClient", client["id"], createElm, Request);
                        });
                    });
                });
            }
            let list = new List(["name", "phone", "email", "chance", ""], ["current", "archived", "all"], insertClients, createElm);
        });
    });
});

let phoneFormatter = (number) => {
    let phoneNumber = "";
    if (String(number).charAt(0) == "+") {
        for ( let i = 3; i < String(number).length; i+=2) {
            phoneNumber = phoneNumber + " " + String(number).charAt(i) + String(number).charAt(i+1);
        }
        phoneNumber = "+45" + phoneNumber;
    }
    else if (String(number).charAt(0) == "0" && String(number).charAt(1) == "0" && String(number).charAt(2) == "4" && String(number).charAt(3) == "5") {
        for ( let i = 4; i < String(number).length; i+=2) {
            phoneNumber = phoneNumber + " " + String(number).charAt(i) + String(number).charAt(i+1);
        }
        phoneNumber = "0045" + phoneNumber;
    }
    else {
        for (let i = 0; i < String(number).length; i+=2) {
            phoneNumber = phoneNumber + " " + String(number).charAt(i) + String(number).charAt(i+1);
        }
    }
    return phoneNumber;
}