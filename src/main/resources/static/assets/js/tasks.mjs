import("./main.mjs").then((module) => {
    let { createElm, Request, confirmBox } = module;
    import("./popUp.mjs").then((module) => {
        let { Popup } = module;
        import("./lists.mjs").then((module) => {
            let { List } = module;
            //Creates a list of all tasks with a parameter to determine which tasks to show based on their state
            let insertTasks = (search=null) => {
                new Request("getTasksForAccount", {search}, (tasks) => {
                    tasks.sort(compareByName);
                    tasks.forEach((task) => {
                        let taskElm = createElm({elm: "tr", classes: "tasklist"});
                        list.table.append(taskElm);
                        new Request("getProjectByTaskId", {taskId: task["id"]}, (projectData) => {
                            projectData["phases"].forEach((phase) => {
                                if(phase["active"]){
                                    let taskName = createElm({elm: "td", classes: "clickable", text: task["name"]});
                                    let projectName = createElm({elm: "td", classes: "clickable", text: projectData["name"]});
                                    let phaseName = createElm({elm: "td", text: phase["name"]});
                                    let BtnTd = createElm({elm: "td"});
                                    let finishBtn = createElm({elm: "button", classes: "finishBtn", type: "button", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M64 80c-8.8 0-16 7.2-16 16V416c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16V96c0-8.8-7.2-16-16-16H64zM0 96C0 60.7 28.7 32 64 32H384c35.3 0 64 28.7 64 64V416c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V96zM337 209L209 337c-9.4 9.4-24.6 9.4-33.9 0l-64-64c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0l47 47L303 175c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9z"/></svg>'});
                                    if(task["completed"]){
                                        finishBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" height="16" width="14" viewBox="0 0 448 512"><path fill="#00ae00" d="M64 32C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64H384c35.3 0 64-28.7 64-64V96c0-35.3-28.7-64-64-64H64zM337 209L209 337c-9.4 9.4-24.6 9.4-33.9 0l-64-64c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0l47 47L303 175c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9z"/></svg>';
                                    }
                                    let deleteBtn = createElm({elm: "button", classes: "deleteBtn", type: "button", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512"><path d="M256 48a208 208 0 1 1 0 416 208 208 0 1 1 0-416zm0 464A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c-9.4 9.4-9.4 24.6 0 33.9l47 47-47 47c-9.4 9.4-9.4 24.6 0 33.9s24.6 9.4 33.9 0l47-47 47 47c9.4 9.4 24.6 9.4 33.9 0s9.4-24.6 0-33.9l-47-47 47-47c9.4-9.4 9.4-24.6 0-33.9s-24.6-9.4-33.9 0l-47 47-47-47c-9.4-9.4-24.6-9.4-33.9 0z"/></svg>'});
                                    BtnTd.append(finishBtn, deleteBtn);
                                    taskElm.append(taskName, projectName, phaseName, BtnTd);
    
                                    new Request("getProjectByTaskId", {taskId: task["id"]}, (project) => {
                                        //Task popup
                                        taskName.addEventListener("click", () => {
                                            new Popup("taskPageTask", task["id"], createElm, Request);
                                        });
                                        //Project popup
                                        projectName.addEventListener("click", () => {
                                            new Popup("project", { "id": project["id"], "type": "view" }, createElm, Request);
                                        });
                                    });
    
                                    //Finish task
                                    finishBtn.addEventListener("click", () => {
                                        confirmBox("Are you sure you want to finish this task?", () => {
                                            new Request("finishTask", {taskId: task["id"]}, () => {
                                                clearTasks();
                                                insertTasks();
                                            });
                                        });
                                    });
    
                                    //Delete task
                                    deleteBtn.addEventListener("click", () => {
                                        confirmBox("Are you sure you want to delete this task?", () => {
                                            new Request("deleteTask", {taskId: task["id"]}, () => {
                                                clearTasks();
                                                insertTasks();
                                            });
                                        });
                                    });
                                }
                            });
                        });
                    });
                });
            }

            function compareByName(a, b) {
                return a["name"].localeCompare(b["name"]);
            }

            //Clears the list
            let clearTasks = () => {
                document.getElementById("list").innerHTML = "";
            }

            //Creates the list
            let list = new List(["name", "project", "phase", ""], null, insertTasks, createElm);
        });
    });
});