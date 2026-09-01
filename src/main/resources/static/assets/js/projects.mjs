import("./main.mjs").then((module) => {
    let { createElm, Request, confirmBox } = module;
    import("./popUp.mjs").then((module) => {
        let { Popup } = module;
        //Add project popup
        document.getElementById("addProjectBtn").addEventListener("click", () => new Popup("project", { "type": "add" }, createElm, Request));
        import("./lists.mjs").then((module) => {
            let { List } = module;
            //Creates a list of all projects with a parameter to determine which projects to show based on their state
            let insertProjects = (page) => {
                new Request("getProjects", {}, (projects) => {
                    projects.forEach(project => {
                        new Request("getClientForProject", { clientId: project["clientId"] }, (clientData) => {
                            let row = createElm({ elm: "tr" });
                            let name = createElm({ elm: "td", classes: "clickable", text: project["name"] });
                            let phase = createElm({ elm: "td", text: project["status"] });
                            let client = createElm({ elm: "td", text: clientData });
                            let removeBtn = createElm({ elm: "td" });
                            removeBtn.append(createElm({ elm: "button", type: "button", classes: "deleteBtn", text: '<svg class="svgButtons" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="currentColor" d="M256 48a208 208 0 1 1 0 416 208 208 0 1 1 0-416zm0 464A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c-9.4 9.4-9.4 24.6 0 33.9l47 47-47 47c-9.4 9.4-9.4 24.6 0 33.9s24.6 9.4 33.9 0l47-47 47 47c9.4 9.4 24.6 9.4 33.9 0s9.4-24.6 0-33.9l-47-47 47-47c9.4-9.4 9.4-24.6 0-33.9s-24.6-9.4-33.9 0l-47 47-47-47c-9.4-9.4-24.6-9.4-33.9 0z" /></svg>' }));
                            row.append(name, phase, client, removeBtn);
                            if(
                                (page == "archived" && project["status"] == "archived") ||
                                (page == "current" && project["status"] != "archived") ||
                                (page == "all")
                            ) list.table.append(row);


                            removeBtn.addEventListener("click", () => {
                                confirmBox("Are you sure you want to delete this project?", () => {
                                    new Request("deleteProject", { "id": project["id"] }, () => {
                                        row.remove();
                                        confirmBox.remove();
                                        location.reload();
                                    });
                                });
                            });

                            name.addEventListener("click", () => new Popup("project", { "id": project["id"], "type": "view" }, createElm, Request));
                        });
                    });
                });
            }
            //Creates the list
            let list = new List(["name", "phase", "client", ""], ["current", "archived", "all"], insertProjects, createElm);
        });
    });
});


