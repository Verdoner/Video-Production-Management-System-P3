export class Popup {
    #type;
    #createElm;
    #identifier;
    #Request;
    popup;
    popUpBody;
    popupBodyTop;
    popupTopHeadings;
    popupControls;
    popupContent;
    popUpCloseBtn;

    constructor(type, identifier, createElm, Request) {
        this.#type = type;
        this.#createElm = createElm;
        this.#identifier = identifier;
        this.#Request = Request;
        this.#popupSetup();
        if (this.#type == "project") this.projectPopup();
        else if (this.#type == "meeting") this.meetingPopUp();
        else if (this.#type == "addMeeting") this.addMeetingPopUp();
        else if (this.#type == "task") this.taskPopUp();
        else if (this.#type == "addTask") this.addTaskPopUp();
        else if (this.#type == "addClient") this.addClientPopup();
        else if (this.#type == "editClient") this.editClientPopup();
        else if (this.#type == "addAccount") this.addAccountPopUp();
        else if (this.#type == "editAccount") this.editAccountPopup();
        else if (this.#type == "taskPageTask") this.taskPageTaskPopUp();
    }

    insertAfter(newNode, existingNode) {
        existingNode.parentNode.insertBefore(newNode, existingNode.nextSibling);
    }

    #popupSetup(createElm = this.#createElm) {
        let popupClass;
        if (this.#type == "project") popupClass = "projectPopup";
        else if (this.#type == "addMeeting") popupClass = "addMeetingPopUp";
        else if (this.#type == "meeting") popupClass = "meetingPopup";
        else if (this.#type == "addTask") popupClass = "addTaskPopUp";
        else if (this.#type == "task") popupClass = "taskPopup";
        else if (this.#type == "addClient" || this.#type == "editClient" || this.#type == "addAccount" || this.#type == "editAccount") popupClass = "simplePopup";
        else if (this.#type == "taskPageTask") popupClass = "taskPopup";

        this.popup = createElm({ elm: "div", classes: ["popup", popupClass] });
        this.popUpBody = createElm({ elm: "div", classes: "popupBody" });
        let popUpCover = createElm({ elm: "div", classes: "cover" });
        this.popup.append(popUpCover, this.popUpBody);

        this.popupBodyTop = createElm({ elm: "div", classes: "top" });
        this.popUpCloseBtn = createElm({ elm: "button", type: "button", classes: "closeButton", id: "popUpCloseBtn", text: '<svg class="svgButtons" id="closeMenu" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path fill="currentColor" d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z"></path></svg>' });
        if(this.#type != "addClient" && this.#type != "editClient" && this.#type != "addAccount" && this.#type != "editAccount"){
            this.popupTopHeadings = createElm({ elm: "div", classes: "topHeadings" });
            this.popupTopHeadings.append(
                createElm({ elm: "input", classes: "title", type: "text", placeholder: "Title" }),
                createElm({ elm: "input", classes: "subtitle", type: "text" })
            );
            this.popupBodyTop.append(this.popupTopHeadings);
        }

        this.popupBodyTop.append(this.popUpCloseBtn);

        this.popupContent = createElm({ elm: "div", classes: "content" });
        if (this.#type == "project") {
            this.popupControls = createElm({ elm: "div", classes: "controls" });
            this.popUpBody.append(
                this.popupBodyTop,
                this.popupControls,
                this.popupContent
            );
        }
        else {
            this.popUpBody.append(
                this.popupBodyTop,
                this.popupContent
            );
        }
        this.popUpCloseBtn.addEventListener("click", this.removePopup.bind(this));
        popUpCover.addEventListener("click", this.removePopup.bind(this));
        document.body.append(this.popup);
    }

    removePopup() {
        this.popup.remove();
    }

    fillMeetingAndTaskLists = (activePhase, projectChanges, createElm = this.#createElm) => {

        document.querySelector(".tasksList").replaceWith(document.querySelector(".tasksList").cloneNode(true));
        document.querySelector(".meetingsList").replaceWith(document.querySelector(".meetingsList").cloneNode(true));
       
        let counter = 0;
        projectChanges["phases"].forEach(phase => {
            if (phase["name"] === activePhase["name"]) {
                //get tasks from server and add tasks to task container
                phase["tasks"].forEach(task => {
                    let taskDiv
                    if (counter % 2 != 0) {
                        taskDiv = createElm({ elm: "div", classes: "gray", id: task["name"] + "," + task["deadline"] });
                    } else {
                        taskDiv = createElm({ elm: "div", id: task["name"] + "," + task["deadline"] });
                    }

                    let taskBtn = createElm({ elm: "button", type: "button", classes: "task", text: task["name"], value: task["id"] });
                    taskDiv.append(taskBtn);
                    taskBtn.addEventListener("click", () => {
                        let taskPopup = new Popup("task", { "activePhase": activePhase, "projectChanges": projectChanges, "task": task }, createElm, this.#Request);
                    });
                    let settingDiv = createElm({ elm: "div" });
                    let deleteBtn = createElm({ elm: "button", classes: ["removeBtn", "removeTask"], value: task["name"]+","+task["deadline"], text: '<svg class="svgButtons" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="currentColor" d="M256 48a208 208 0 1 1 0 416 208 208 0 1 1 0-416zm0 464A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c-9.4 9.4-9.4 24.6 0 33.9l47 47-47 47c-9.4 9.4-9.4 24.6 0 33.9s24.6 9.4 33.9 0l47-47 47 47c9.4 9.4 24.6 9.4 33.9 0s9.4-24.6 0-33.9l-47-47 47-47c9.4-9.4 9.4-24.6 0-33.9s-24.6-9.4-33.9 0l-47 47-47-47c-9.4-9.4-24.6-9.4-33.9 0z" /></svg>' });


                    if (task.completed === true) {
                        let ShowCompleted = createElm({ elm: "p", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512"><path fill="currentcolor" d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM369 209L241 337c-9.4 9.4-24.6 9.4-33.9 0l-64-64c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0l47 47L335 175c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9z"/></svg>' })
                        settingDiv.append(ShowCompleted);
                    }
                    settingDiv.append(deleteBtn);
                    taskDiv.append(settingDiv);
                    tasksList.append(taskDiv);
                    counter++;
                });
                counter = 0;
                //get meetings from server and add meetings to meeting container
                phase["meetings"].forEach(meeting => {
                    let meetingDiv;
                    if (counter % 2 != 0) {
                        meetingDiv = createElm({ elm: "div", classes: "gray", id: meeting["name"] + "," + meeting["startTime"] });
                    } else {
                        meetingDiv = createElm({ elm: "div", id: meeting["name"] + "," + meeting["startTime"] });
                    }
                    let meetingBtn = createElm({ elm: "button", type: "button", classes: "task", text: meeting["name"], value: meeting["id"] });
                    meetingDiv.append(meetingBtn);
                    meetingBtn.addEventListener("click", () => {
                        let meetingPopup = new Popup("meeting", { "activePhase": activePhase, "projectChanges": projectChanges, "meeting": meeting }, createElm, this.#Request);
                    });
                    let settingDiv = createElm({ elm: "div" });
                    let deleteBtn = createElm({ elm: "button", classes: ["removeBtn", "removeMeeting"], value: meeting["name"] + "," + meeting["startTime"], text: '<svg class="svgButtons" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="currentColor" d="M256 48a208 208 0 1 1 0 416 208 208 0 1 1 0-416zm0 464A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c-9.4 9.4-9.4 24.6 0 33.9l47 47-47 47c-9.4 9.4-9.4 24.6 0 33.9s24.6 9.4 33.9 0l47-47 47 47c9.4 9.4 24.6 9.4 33.9 0s9.4-24.6 0-33.9l-47-47 47-47c9.4-9.4 9.4-24.6 0-33.9s-24.6-9.4-33.9 0l-47 47-47-47c-9.4-9.4-24.6-9.4-33.9 0z" /></svg>' });

                    if (new Date(new Date(meeting.startTime).getTime() + meeting.duration * 60000) < new Date()) {
                        let ShowCompleted = createElm({ elm: "p", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512"><path fill="currentcolor" d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM369 209L241 337c-9.4 9.4-24.6 9.4-33.9 0l-64-64c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0l47 47L335 175c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9z"/></svg>' })
                        settingDiv.append(ShowCompleted);
                    }
                    settingDiv.append(deleteBtn);
                    meetingDiv.append(settingDiv);
                    meetingsList.append(meetingDiv);
                    counter++;
                });
            };
        });
        document.querySelectorAll(".meetingsList, .tasksList").forEach((list) => {
            list.addEventListener("click", (e) => {
                if (e.target.matches("svg") || e.target.matches("path")) {


                    let confirmBox = createElm({ elm: "div", classes: "confirmBox" });
                    let confirmText = createElm({ elm: "p" });
                    let buttonBox = createElm({ elm: "div" });
                    let confirmBtn = createElm({ elm: "button", type: "button", id: "confirm" });
                    let cancelBtn = createElm({ elm: "button", type: "button", text: "No" });
                    buttonBox.append(confirmBtn, cancelBtn);
                    confirmBox.append(confirmText, buttonBox);
                    document.body.append(confirmBox);

                    let removeButton = e.target.closest("button");
                    let name = removeButton.value.split(",")[0];
                    let time = removeButton.value.split(",")[1];

                    if (removeButton.classList.contains("removeMeeting")) {
                        confirmText.textContent = "Are you sure you want to delete this meeting?";
                        confirmBtn.textContent = "Delete Meeting";

                        confirmBtn.addEventListener("click", () => {
                            document.getElementById(removeButton.value).remove();
                            projectChanges["phases"].forEach(phase => {
                                for (let index = 0; index < phase["meetings"].length; index++) {
                                    if (phase["meetings"][index]["name"] == name && phase["meetings"][index]["startTime"] == time) {
                                        phase["meetings"].splice(index,1);
                                        break;
                                    }
                                }
                            });
                            document.getElementById("tasksList").innerHTML = "";
                            document.getElementById("meetingsList").innerHTML = "";
                            this.fillMeetingAndTaskLists(activePhase, projectChanges);
                            confirmBox.remove();
                        });

                        cancelBtn.addEventListener("click", () => {
                            confirmBox.remove();
                        });
                    } else if (removeButton.classList.contains("removeTask")) {
                        confirmText.textContent = "Are you sure you want to delete this task?";
                        confirmBtn.textContent = "Delete Task";

                        confirmBtn.addEventListener("click", () => {
                            document.getElementById(removeButton.value).remove();

                            projectChanges["phases"].forEach(phase => {
                                for (let index = 0; index < phase["tasks"].length; index++) {
                                    if (phase["tasks"][index]["name"] == name && phase["tasks"][index]["deadline"] == time) {
                                        phase["tasks"].splice(index,1);
                                        break;
                                    }
                                }
                            });
                            document.getElementById("tasksList").innerHTML = "";
                            document.getElementById("meetingsList").innerHTML = "";
                            this.fillMeetingAndTaskLists(activePhase, projectChanges);
                            confirmBox.remove();
                        });
                        cancelBtn.addEventListener("click", () => {
                            confirmBox.remove();
                        });
                    }
                };
            });
        });
    }
    

    /*This is where the individual popups are created*/
    projectPopup(createElm = this.#createElm) {

        //add save button to top
        let saveBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M48 96V416c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16V170.5c0-4.2-1.7-8.3-4.7-11.3l33.9-33.9c12 12 18.7 28.3 18.7 45.3V416c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V96C0 60.7 28.7 32 64 32H309.5c17 0 33.3 6.7 45.3 18.7l74.5 74.5-33.9 33.9L320.8 84.7c-.3-.3-.5-.5-.8-.8V184c0 13.3-10.7 24-24 24H104c-13.3 0-24-10.7-24-24V80H64c-8.8 0-16 7.2-16 16zm80-16v80H272V80H128zm32 240a64 64 0 1 1 128 0 64 64 0 1 1 -128 0z"/></svg>' });
        this.insertAfter(saveBtn, this.popupTopHeadings);

        //add phase containers
        let phaseButtons = createElm({ elm: "div", classes: "phaseButtons" });
        let phaseContent = createElm({ elm: "div", classes: "phaseContent" });

        let desc = createElm({ elm: "div", id: "rightSideButton" });
        let buttonDiv = createElm({ elm: "div", id: "phaseDescDiv" });
        let descButton = createElm({ elm: "button", classes: "phaseButton", text: "Description", id: "descButton" });
        let descriptionContent = createElm({ elm: "div", id: "descContent" });
        descriptionContent.style.display = "none";
        let descriptionText = createElm({ elm: "textarea" });
        descriptionText.placeholder = "Description of project: "

        descriptionContent.append(descriptionText);

        desc.append(descButton);

        //add phase buttons to phase button container
        phaseButtons.append(
            createElm({ elm: "h2", text: "Phases:" }),
            createElm({ elm: "button", type: "button", classes: "phaseButton", id: "ideaGen", text: "Idea Generation", value: "1" }),
            createElm({ elm: "button", type: "button", classes: "phaseButton", id: "clientInterview", text: "Client Interview", value: "2" }),
            createElm({ elm: "button", type: "button", classes: "phaseButton", id: "recording", text: "Recording", value: "3" }),
            createElm({ elm: "button", type: "button", classes: "phaseButton", id: "editing", text: "Editing", value: "4" }),
            createElm({ elm: "button", type: "button", classes: "phaseButton", id: "end", text: "End", value: "5" }),
        );

        //add buttons to phase content container
        let archiveProjectBtn = createElm({ elm: "button", type: "button", classes: "addButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512"><path d="M32 32H480c17.7 0 32 14.3 32 32V96c0 17.7-14.3 32-32 32H32C14.3 128 0 113.7 0 96V64C0 46.3 14.3 32 32 32zm0 128H480V416c0 35.3-28.7 64-64 64H96c-35.3 0-64-28.7-64-64V160zm128 80c0 8.8 7.2 16 16 16H336c8.8 0 16-7.2 16-16s-7.2-16-16-16H176c-8.8 0-16 7.2-16 16z"/></svg> Archive Project' });
        let dropdownMenu = createElm({ elm: "select", id: "dropdown", name: "dropdownMenu" });
        let dropdownName = createElm({ elm: "option", value: "Option 1", text: "Change phase" });
        dropdownName.setAttribute("disabled", true);
        dropdownName.setAttribute("hidden", true);
        dropdownName.setAttribute("selected", true);

        dropdownMenu.append(
            dropdownName,
            createElm({ elm: "option", value: "Option 2", text: "Idea Generation" }),
            createElm({ elm: "option", value: "Option 3", text: "Client Interview" }),
            createElm({ elm: "option", value: "Option 4", text: "Recording" }),
            createElm({ elm: "option", value: "Option 5", text: "Editing" }),
            createElm({ elm: "option", value: "Option 6", text: "End" })
        );
        this.popupControls.append(
            archiveProjectBtn,
            dropdownMenu
        );


        //add task- and meeting container to phase content container
        let tasks = createElm({ elm: "div", classes: "tasks" });
        let tasksHeadingCon = createElm({ elm: "div", classes: "tasksHeadingCon" });
        let tasksList = createElm({ elm: "div", classes: "tasksList", id: "tasksList" })
        let addTasks = createElm({ elm: "button", type: "button", classes: "addButton", id: "addTaskBtn", text: '<svg class="svgButtons" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM232 344V280H168c-13.3 0-24-10.7-24-24s10.7-24 24-24h64V168c0-13.3 10.7-24 24-24s24 10.7 24 24v64h64c13.3 0 24 10.7 24 24s-10.7 24-24 24H280v64c0 13.3-10.7 24-24 24s-24-10.7-24-24z" /></svg> Add Task' });

        tasksHeadingCon.append(createElm({ elm: "h2", text: "Tasks" }), addTasks);
        tasks.append(tasksHeadingCon, tasksList);

        let meetings = createElm({ elm: "div", classes: "meetings" });
        let meetingsHeadingCon = createElm({ elm: "div", classes: "meetingsHeadingCon" });
        let meetingsList = createElm({ elm: "div", classes: "meetingsList", id: "meetingsList" });
        let addMeetings = createElm({ elm: "button", type: "button", classes: "addButton", id: "addMeetingBtn", text: '<svg class="svgButtons" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM232 344V280H168c-13.3 0-24-10.7-24-24s10.7-24 24-24h64V168c0-13.3 10.7-24 24-24s24 10.7 24 24v64h64c13.3 0 24 10.7 24 24s-10.7 24-24 24H280v64c0 13.3-10.7 24-24 24s-24-10.7-24-24z" /></svg> Add Meeting' });

        meetingsHeadingCon.append(createElm({ elm: "h2", text: "Meetings" }), addMeetings);

        meetings.append(meetingsHeadingCon, meetingsList);

        phaseContent.append(tasks, meetings);
        buttonDiv.append(phaseButtons, desc)
        this.popupContent.append(buttonDiv, phaseContent, descriptionContent);


        descButton.addEventListener("click", () => {
            phaseContent.style.display = "none"
            descriptionContent.style.display = "flex"
            document.querySelector(".phaseButton.active").classList.remove("active");
            descButton.classList.add("active");
        });

        if (this.#identifier["type"] === "view") {

            //change text
            new this.#Request("getProjectById", { projectId: this.#identifier["id"] }, (data) => {

                let projectChanges = data;
                let activePhase = data["phases"].find(phase => phase.active === true);

                document.querySelectorAll(".phaseButtons>button").forEach(button => {
                    if (button.textContent === activePhase["name"]) {
                        button.classList.add("active");
                    };
                });

                addTasks.addEventListener("click", () => {
                    new Popup("addTask", { "activePhase": activePhase, "projectChanges": projectChanges }, createElm, this.#Request);
                });
                addMeetings.addEventListener("click", () => {
                    new Popup("addMeeting", { "activePhase": activePhase, "projectChanges": projectChanges }, createElm, this.#Request);
                });


                this.popup.querySelector(".top .title").value = data["name"];
                descriptionText.textContent = data["description"];
                this.popup.querySelector(".top .subtitle").setAttribute("disabled", true);


                new this.#Request("getClientForProject", { clientId: data["clientId"] }, (data) => {
                    this.popup.querySelector(".top .subtitle").value = "Client: " + data;
                });


                this.fillMeetingAndTaskLists(activePhase, projectChanges);

                //EventListener which 
                document.querySelectorAll(".phaseButtons>button").forEach(button => {
                    button.addEventListener("click", () => {
                        phaseContent.style.display = "flex";
                        descriptionContent.style.display = "none";
                        document.querySelector(".phaseButton.active").classList.remove("active");
                        document.getElementById("descButton").classList.remove("active");
                        button.classList.add("active");
                        document.getElementById("tasksList").innerHTML = "";
                        document.getElementById("meetingsList").innerHTML = "";

                        activePhase = data["phases"].find(phase => phase.id === button.value);

                        document.getElementById("addTaskBtn").replaceWith((document.getElementById("addTaskBtn")).cloneNode(true));
                        let clonedTaskBtn = document.getElementById("addTaskBtn");

                        clonedTaskBtn.addEventListener("click", () => {
                            let taskPopup = new Popup("addTask", { "activePhase": activePhase, "projectChanges": projectChanges }, createElm, this.#Request);
                        });

                        document.getElementById("addMeetingBtn").replaceWith((document.getElementById("addMeetingBtn")).cloneNode(true));
                        let clonedMeetingsBtn = document.getElementById("addMeetingBtn");

                        clonedMeetingsBtn.addEventListener("click", () => {
                            let meetingPopup = new Popup("addMeeting", { "activePhase": activePhase, "projectChanges": projectChanges }, createElm, this.#Request);
                        });

                        this.fillMeetingAndTaskLists(activePhase, projectChanges);
                    });
                });

                document.querySelectorAll("#dropdown>option").forEach(option => {
                    if (option.text === data["status"]) {
                        document.querySelector("#dropdown>option:checked").setAttribute("selected", false);
                        option.setAttribute("selected", true);

                        document.querySelectorAll(".phaseButtons>button").forEach(button => {
                            if (button.textContent === option.text) {
                                document.querySelector(".phaseButton.active").classList.remove("active");
                                button.classList.add("active");
                            };
                        });
                    };
                });



                archiveProjectBtn.addEventListener("click", () => {
                    new this.#Request("setProjectStatus", { "id": data["id"], "status": "archived" }, (data) => { alert(data); location.reload(); });
                });


                document.getElementById("popUpCloseBtn").replaceWith(document.getElementById("popUpCloseBtn").cloneNode(true));
                let clonedButton = document.getElementById("popUpCloseBtn");
                clonedButton.addEventListener("click", () => {
                    let confirmCover = createElm({ elm: "div", classes: ["cover", "confirm"] });
                    let confirmBox = createElm({ elm: "div", classes: "confirmBox" });
                    let confirmText = createElm({ elm: "p", text: "Discard Changes?" });
                    let buttonBox = createElm({ elm: "div" });
                    let confirmBtn = createElm({ elm: "button", type: "button", text: "Yes", id: "confirm" });
                    let cancelBtn = createElm({ elm: "button", type: "button", text: "No" });
                    buttonBox.append(confirmBtn, cancelBtn);
                    confirmBox.append(confirmText, buttonBox);
                    document.body.append(confirmCover, confirmBox);

                    confirmBtn.addEventListener("click", () => {
                        confirmBox.remove();
                        confirmCover.remove();
                        this.popup.remove();
                        projectChanges = {};
                    });
                    cancelBtn.addEventListener("click", () => {
                        confirmBox.remove();
                        confirmCover.remove();
                    });
                });


                saveBtn.addEventListener("click", () => {
                    projectChanges['name'] = this.popup.querySelector(".top .title").value;
                    projectChanges['description'] = descriptionText.value;
                    if (document.querySelector("#dropdown>option:checked").textContent != "Change phase") {
                        projectChanges['status'] = document.querySelector("#dropdown>option:checked").textContent;
                    };
                    projectChanges["phases"].forEach(phase => {
                        phase.active = false;
                        if (phase.name === projectChanges["status"]) {
                            phase.active = true;
                        }
                    });

                    console.log(projectChanges);
                    new this.#Request("updateProject", projectChanges, (data) => {
                        alert(data);
                        location.reload();
                    });
                });


            });
        } else if (this.#identifier["type"] === "add") {

            this.popup.querySelector(".top .title").remove();
            this.popup.querySelector(".top .subtitle").remove();
            this.popupControls.remove();
            this.popupContent.innerHTML = "";


            this.popupBodyTop.classList.add("add");
            this.popupContent.classList.add("add");
            this.popUpBody.classList.add("add");
            this.popupBodyTop.prepend(createElm({ elm: "h1", text: "Add Project" }));

            let addForm = createElm({ elm: "form", id: "addForm" });

            let projectBox = createElm({ elm: "div", classes: "addDiv" });
            let projectName = createElm({ elm: "input", type: "text", id: "addProjectName" })
            projectName.setAttribute("required", true);
            projectName.placeholder = "Project name..."
            projectBox.append(
                createElm({ elm: "label", for: "addProjectName", text: "Project Name: " }),
                projectName
            );

            let clientBox = createElm({ elm: "div", id: "clientBox" });
            let clientSelect = createElm({ elm: "select", classes: "sort", id: "addClient" })
            clientSelect.setAttribute("required", true);

            let clientOption;
            new this.#Request("getAllClients", {}, (data) => {
                data.forEach(client => {
                    clientOption = createElm({ elm: "option", text: client["name"], value: client["id"] });
                    clientSelect.append(clientOption);
                });
            });

            clientBox.append(
                createElm({ elm: "label", for: "addClient", text: "Choose Client: " }),
                clientSelect
            );


            let deadlineBox = createElm({ elm: "div", id: "deadlineBox" })
            let deadline = createElm({ elm: "input", type: "date", id: "deadline" })
            deadline.setAttribute("required", true);
            deadline.setAttribute("min", new Date().toISOString().split("T")[0]);
            deadline.setAttribute("value", new Date().toISOString().split("T")[0]);
            deadlineBox.append(
                createElm({ elm: "label", for: "deadline", text: "Set Deadline: " }),
                deadline
            )

            let descriptionBox = createElm({ elm: "div", classes: "addDiv" });
            let projectDescription = createElm({ elm: "textarea", id: "projectDescription" })
            descriptionBox.append(
                createElm({ elm: "label", for: "projectDescription", text: "Description: " }),
                projectDescription
            );

            addForm.append(
                projectBox,
                clientBox,
                deadlineBox,
                descriptionBox
            );

            this.popupContent.append(addForm);

            saveBtn.setAttribute("form", "addForm");
            saveBtn.setAttribute("type", "submit");

            addForm.addEventListener("submit", (e) => {
                e.preventDefault(); 
                console.log("submit");
                new this.#Request("createProject", { projectName: projectName.value, clientId: clientSelect.value, deadline: deadline.value, projectDescription: projectDescription.value }, (data) => {
                    alert(data);
                });
            });
        };
    };

    addZeroBefore(n) {
        return (n < 10 ? '0' : '') + n;
    }


    //meeting Pop Up creation
    meetingPopUp(createElm = this.#createElm) {

        let saveBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M48 96V416c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16V170.5c0-4.2-1.7-8.3-4.7-11.3l33.9-33.9c12 12 18.7 28.3 18.7 45.3V416c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V96C0 60.7 28.7 32 64 32H309.5c17 0 33.3 6.7 45.3 18.7l74.5 74.5-33.9 33.9L320.8 84.7c-.3-.3-.5-.5-.8-.8V184c0 13.3-10.7 24-24 24H104c-13.3 0-24-10.7-24-24V80H64c-8.8 0-16 7.2-16 16zm80-16v80H272V80H128zm32 240a64 64 0 1 1 128 0 64 64 0 1 1 -128 0z"/></svg>' });
        this.insertAfter(saveBtn, this.popupTopHeadings);

        this.popup.querySelector(".top .title").placeholder = "Meeting name";
        this.popup.querySelector(".top .title").value = this.#identifier["meeting"]["name"];
        this.popup.querySelector(".top .subtitle").remove();

        let timeBox = createElm({ elm: "div", id: "meetingTimeBox" })

        let date = new Date(this.#identifier["meeting"]["startTime"]);
        let startTime = createElm({ elm: "input", type: "time", id: "meetingStartTime" });
        startTime.value = this.#identifier["meeting"]["startTime"].split("T")[1];

        let endTimeFromData = new Date(date.getTime() + this.#identifier["meeting"]["duration"] * 60000);
        let endTime = createElm({ elm: "input", type: "time", id: "meetingEndTime" });
        endTime.value = this.addZeroBefore(endTimeFromData.getHours()) + ":" + this.addZeroBefore(endTimeFromData.getMinutes());

        let meetingDate = createElm({ elm: "input", type: "date", id: "meetingDate" });
        meetingDate.value = this.#identifier["meeting"]["startTime"].split("T")[0];
        meetingDate.min = new Date().toISOString().split("T")[0];

        timeBox.append("Time: ", startTime, " - ", endTime, " d. ", meetingDate);
        this.popup.querySelector(".top .title").parentNode.insertBefore(timeBox, this.popup.querySelector(".top .title").nextSibling);


        //Assigned
        let assignedContainer = createElm({ elm: "div", classes: "assignedContainer" });
        let assignedHeader = createElm({ elm: "div", classes: "assignedHeader" });
        let dropdownMenu = createElm({ elm: "select", id: "dropdown", name: "dropdownMenu" });
        assignedHeader.append(createElm({ elm: "h2", text: "Participants:" }), dropdownMenu);
        assignedContainer.append(assignedHeader, createElm({ elm: "div", classes: "assignedList" }));


        dropdownMenu.append(
            createElm({ elm: "option", value: "Option 1", text: "Add participant", id: "disabledOption" }),
            createElm({ elm: "option", value: "Option 2", text: "Julius" }),
            createElm({ elm: "option", value: "Option 3", text: "Lars" }),
            createElm({ elm: "option", value: "Option 4", text: "Malek" }),
            createElm({ elm: "option", value: "Option 5", text: "Markus Holmager Hundborg" })
        );
        //noget som indsætter participants i møde popupen
        // data["participants"].forEach(participant => {

        // });

        //Description
        let descriptionContainer = createElm({ elm: "div", classes: "descriptionContainer" });
        let descriptionHeader = createElm({ elm: "div", classes: "descriptionHeader" });
        let desc = createElm({ elm: "textarea", classes: "description" })
        desc.textContent = this.#identifier["meeting"]["description"];
        descriptionHeader.append(
            createElm({ elm: "h2", text: "Description:" }),
        );
        descriptionContainer.append(descriptionHeader, desc);
        this.popupContent.append(assignedContainer, descriptionContainer);

        saveBtn.addEventListener("click", () => {
            let date1 = new Date(meetingDate.value + "T" + startTime.value);
            let date2 = new Date(meetingDate.value + "T" + endTime.value);

            console.log(date1);
            console.log(date2);

            this.#identifier["projectChanges"]["phases"].forEach(phase => {
                if (phase["name"] === this.#identifier["activePhase"]["name"]) {
                    phase["meetings"].forEach(meeting => {
                        if (meeting["name"] === this.#identifier["meeting"]["name"]) {
                            meeting["name"] = this.popup.querySelector(".top .title").value;
                            meeting["description"] = desc.value;
                            meeting["startTime"] = meetingDate.value + "T" + startTime.value;
                            meeting["duration"] = (date2 - date1) / 60000;
                            console.log(meeting);
                        }
                    })
                }
            })
            document.getElementById("tasksList").innerHTML = "";
            document.getElementById("meetingsList").innerHTML = "";
            this.fillMeetingAndTaskLists(this.#identifier["activePhase"], this.#identifier["projectChanges"]);
            this.removePopup();
        });
    }


    //task pop up creation
    taskPopUp(createElm = this.#createElm) {
        //add save button to top
        let saveBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M48 96V416c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16V170.5c0-4.2-1.7-8.3-4.7-11.3l33.9-33.9c12 12 18.7 28.3 18.7 45.3V416c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V96C0 60.7 28.7 32 64 32H309.5c17 0 33.3 6.7 45.3 18.7l74.5 74.5-33.9 33.9L320.8 84.7c-.3-.3-.5-.5-.8-.8V184c0 13.3-10.7 24-24 24H104c-13.3 0-24-10.7-24-24V80H64c-8.8 0-16 7.2-16 16zm80-16v80H272V80H128zm32 240a64 64 0 1 1 128 0 64 64 0 1 1 -128 0z"/></svg>' });
        let completBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z"/></svg>' });
        this.insertAfter(completBtn, this.popupTopHeadings);
        this.insertAfter(saveBtn, this.popupTopHeadings);

        this.popup.querySelector(".top .title").placeholder = "Task Name";
        this.popup.querySelector(".top .subtitle").placeholder = "Project Name";
        this.popup.querySelector(".top .title").value = this.#identifier["task"]["name"];
        this.popup.querySelector(".top .subtitle").value = "Project: " + this.#identifier["projectChanges"]["name"];
        this.popup.querySelector(".top .subtitle").setAttribute("disabled", true);

        let completed = false;

        completBtn.addEventListener("click", () => {
            completed = true;
        })

        const timeOptions = { weekday: "long", year: "2-digit", month: "2-digit", day: "2-digit" }
        let taskDate = createElm({ elm: "input", type: "date", value: this.#identifier["task"]["deadline"], id: "taskDeadline" });
        this.popupTopHeadings.append(
            createElm({ elm: "label", for: "taskDeadline", text: "Deadline: " }),
            taskDate
        );

        //Assigned
        let assignedContainer = createElm({ elm: "div", classes: "assignedContainer" });
        let assignedToSelect = createElm({ elm: "select", id: "assignedToSelect", classes: "sort" });
        let selectOption;
        new this.#Request("getEmployees", {}, (data) => {
            data.forEach(employee => {
                selectOption = createElm({ elm: "option", value: employee["id"], text: employee["name"] });
                if (employee["id"] === this.#identifier["task"]["employeeId"]) {
                    selectOption.setAttribute("selected", true);
                }
                assignedToSelect.append(selectOption);
            });
        });

        assignedContainer.append(
            createElm({ elm: "label", for: "assignedToSelect", text: "Assigned Employee:" }),
            assignedToSelect
        )


        //Description
        let descriptionContainer = createElm({ elm: "div", classes: "descriptionContainer" });
        let descriptionHeader = createElm({ elm: "div", classes: "descriptionHeader" });
        descriptionHeader.append(
            createElm({ elm: "h2", text: "Description:" }),
        );
        let desc = createElm({ elm: "textarea", classes: "description", text: this.#identifier["task"]["description"] });
        descriptionContainer.append(descriptionHeader, desc);
        this.popupContent.append(assignedContainer, descriptionContainer);


        saveBtn.addEventListener("click", () => {

            this.#identifier["projectChanges"]["phases"].forEach(phase => {
                if (phase["name"] === this.#identifier["activePhase"]["name"]) {
                    phase["tasks"].forEach(task => {
                        if (task["name"] === this.#identifier["task"]["name"]) {
                            task["name"] = this.popup.querySelector(".top .title").value;
                            task["description"] = desc.value;
                            task["deadline"] = taskDate.value;
                            task["employeeId"] = assignedToSelect.value;
                            task["completed"] = completed;
                            console.log(task);
                        }
                    })
                }
            })
            document.getElementById("tasksList").innerHTML = "";
            document.getElementById("meetingsList").innerHTML = "";
            this.fillMeetingAndTaskLists(this.#identifier["activePhase"], this.#identifier["projectChanges"]);
            this.removePopup();
        });

    }


    addMeetingPopUp(createElm = this.#createElm) {
        let saveBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M48 96V416c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16V170.5c0-4.2-1.7-8.3-4.7-11.3l33.9-33.9c12 12 18.7 28.3 18.7 45.3V416c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V96C0 60.7 28.7 32 64 32H309.5c17 0 33.3 6.7 45.3 18.7l74.5 74.5-33.9 33.9L320.8 84.7c-.3-.3-.5-.5-.8-.8V184c0 13.3-10.7 24-24 24H104c-13.3 0-24-10.7-24-24V80H64c-8.8 0-16 7.2-16 16zm80-16v80H272V80H128zm32 240a64 64 0 1 1 128 0 64 64 0 1 1 -128 0z"/></svg>' });
        this.insertAfter(saveBtn, this.popupTopHeadings);

        this.popup.querySelector(".top .title").remove();
        this.popup.querySelector(".top .subtitle").remove();
        this.popupBodyTop.prepend(createElm({ elm: "h1", text: "Add Meeting" }));

        let addForm = createElm({ elm: "form", id: "addMeetingForm" });


        let nameBox = createElm({ elm: "div", classes: "addDiv" });
        let name = createElm({ elm: "input", type: "text", id: "addMeetingName" })
        name.setAttribute("required", true);
        name.placeholder = "Meeting name..."
        nameBox.append(
            createElm({ elm: "label", for: "addMeetingName", text: "Meeting Name: " }),
            name
        );


        let descBox = createElm({ elm: "div", classes: "addDiv" });
        let desc = createElm({ elm: "textarea", id: "meetingDescription" })
        desc.placeholder = "Enter Description..."
        descBox.append(
            createElm({ elm: "label", for: "meetingDescription", text: "Description: " }),
            desc
        );

        let dateBox = createElm({ elm: "div", classes: "addDiv", id: "meetingTimeBox" });
        let time = createElm({ elm: "input", type: "datetime-local", id: "dateNTime" });
        time.min = new Date().toISOString().split("T")[0] + "T" + this.addZeroBefore(new Date().getHours()) + ":" + this.addZeroBefore(new Date().getMinutes());
        time.value = new Date().toISOString().split("T")[0] + "T" + this.addZeroBefore(new Date().getHours()) + ":" + this.addZeroBefore(new Date().getMinutes());
        let duration = createElm({ elm: "input", type: "time", id: "endTime" });
        duration.setAttribute("min", time.value.split("T")[1]);
        duration.setAttribute("value", time.value.split("T")[1]);
        dateBox.append(
            createElm({ elm: "label", for: "dateNTime", text: "Meeting Start: " }),
            time,
            createElm({ elm: "label", for: "endTime", text: "To: " }),
            duration
        );



        let participantId;
        new this.#Request("getLoggedInEmployeeId", {}, (data) => {
            participantId = data;
        });

        addForm.append(
            nameBox,
            dateBox,
            descBox
        );

        this.popupContent.append(addForm);

        saveBtn.setAttribute("form", "addMeetingForm");
        saveBtn.setAttribute("type", "submit");

        let newMeeting = {};

        addForm.addEventListener("submit", (e) => {
            e.preventDefault();
            let date1 = new Date(time.value);
            let date2 = new Date(time.value);
            date2.setHours(this.addZeroBefore(duration.value.split(":")[0]));
            date2.setMinutes(this.addZeroBefore(duration.value.split(":")[1]));

            newMeeting["name"] = document.getElementById("addMeetingName").value;
            newMeeting["description"] = document.getElementById("meetingDescription").value;
            newMeeting["startTime"] = document.getElementById("dateNTime").value;
            newMeeting["duration"] = (date2 - date1) / 60000;
            newMeeting["id"] = "";
            newMeeting["participants"] = [];
            newMeeting["participants"].push({ "accepted": false, "employeeId": participantId });

            this.#identifier["projectChanges"]["phases"].forEach(phase => {
                if (phase["name"] === this.#identifier["activePhase"]["name"]) {
                    phase["meetings"].push(newMeeting);
                };
            });
            document.getElementById("tasksList").innerHTML = "";
            document.getElementById("meetingsList").innerHTML = "";
            this.fillMeetingAndTaskLists(this.#identifier["activePhase"], this.#identifier["projectChanges"]);
            this.removePopup();
        })
    }

    addTaskPopUp(createElm = this.#createElm) {
        let saveBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M48 96V416c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16V170.5c0-4.2-1.7-8.3-4.7-11.3l33.9-33.9c12 12 18.7 28.3 18.7 45.3V416c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V96C0 60.7 28.7 32 64 32H309.5c17 0 33.3 6.7 45.3 18.7l74.5 74.5-33.9 33.9L320.8 84.7c-.3-.3-.5-.5-.8-.8V184c0 13.3-10.7 24-24 24H104c-13.3 0-24-10.7-24-24V80H64c-8.8 0-16 7.2-16 16zm80-16v80H272V80H128zm32 240a64 64 0 1 1 128 0 64 64 0 1 1 -128 0z"/></svg>' });
        this.insertAfter(saveBtn, this.popupTopHeadings);

        this.popup.querySelector(".top .title").remove();
        this.popup.querySelector(".top .subtitle").remove();
        this.popupBodyTop.prepend(createElm({ elm: "h1", text: "Add Task" }));

        let addForm = createElm({ elm: "form", id: "addTaskForm" });


        let nameBox = createElm({ elm: "div", classes: "addDiv" });
        let name = createElm({ elm: "input", type: "text", id: "addTaskName" })
        name.setAttribute("required", true);
        name.placeholder = "Task name..."
        nameBox.append(
            createElm({ elm: "label", for: "addTaskName", text: "Task Name: " }),
            name
        );


        let descBox = createElm({ elm: "div", classes: "addDiv" });
        let desc = createElm({ elm: "textarea", id: "taskDescription" })
        desc.placeholder = "Enter Description..."
        descBox.append(
            createElm({ elm: "label", for: "taskDescription", text: "Description: " }),
            desc
        );

        let dateBox = createElm({ elm: "div", classes: "addDiv", id: "taskDeadlineBox" });
        let time = createElm({ elm: "input", type: "datetime-local", id: "taskDeadline" });
        time.min = new Date().toISOString().split("T")[0];
        time.value = new Date().toISOString().split("T")[0];
        dateBox.append(
            createElm({ elm: "label", for: "taskDeadline", text: "Deadline for Task: " }),
            time,
        );

        let assginToBox = createElm({ elm: "div", classes: "addDiv", id: "taskAssign" });
        let employeeSelect = createElm({ elm: "select", id: "employeeSelect", classes: "sort" });
        let employeeOption;
        new this.#Request("getEmployees", {}, (data) => {
            data.forEach(employee => {
                employeeOption = createElm({ elm: "option", value: employee["id"], text: employee["name"] });
                employeeSelect.append(employeeOption);
            });
        });

        assginToBox.append(
            createElm({ elm: "label", for: "employeeSelect", text: "Assign Task to Employee: " }),
            employeeSelect
        );

        addForm.append(
            nameBox,
            dateBox,
            assginToBox,
            descBox
        );

        this.popupContent.append(addForm);

        saveBtn.setAttribute("form", "addTaskForm");
        saveBtn.setAttribute("type", "submit");

        let newTask = {};

        addForm.addEventListener("submit", (e) => {
            e.preventDefault();

            newTask["name"] = document.getElementById("addTaskName").value;
            newTask["description"] = document.getElementById("taskDescription").value;
            newTask["creationDate"] = new Date().toISOString().split("T")[0];
            newTask["deadline"] = document.getElementById("taskDeadline").value;
            newTask["employeeId"] = employeeSelect.value;
            newTask["completed"] = false;
            newTask["id"] = "";

            this.#identifier["projectChanges"]["phases"].forEach(phase => {
                if (phase["name"] === this.#identifier["activePhase"]["name"]) {
                    phase["tasks"].push(newTask);
                };
            });
            document.getElementById("tasksList").innerHTML = "";
            document.getElementById("meetingsList").innerHTML = "";
            this.fillMeetingAndTaskLists(this.#identifier["activePhase"], this.#identifier["projectChanges"]);
            this.removePopup();
        })
    }

    //account pop up creation
    addAccountPopUp() {
        this.accountPopupTemplate(false, (formData) => {
            new this.#Request("addEmployee", { name: formData.get("name"), phone: formData.get("phone"), email: formData.get("email"), role: formData.get("role") }, (data) => {
                data == "Email already exists" ? alert("Email already exists") : location.reload();
            });
        });
        
    }

    //specific account popup
    editAccountPopup() {
        this.accountPopupTemplate(true, (formData) => {
            new this.#Request("updateEmployee", { id: this.#identifier, name: formData.get("name"), phone: formData.get("phone"), email: formData.get("email"), role: formData.get("role") }, (data) => {
                data == "Email already exists" ? alert("Email already exists") : location.reload();
            });
        });
    }

    accountPopupTemplate(fill, callback, createElm = this.#createElm){
        let accountForm = createElm({ elm: "form", id: "assignedContainer" });
        this.popupContent.append(accountForm);

        let accountNameInput = createElm({ elm: "input", type: "text", placeholder: "Enter name...", text: "required", name: "name" });
        let roleOptions = createElm({ elm: "select", id: "dropdown", name: "role" });
        roleOptions.append(
            createElm({ elm: "option", value: "editor", text: "Editor" }),
            createElm({ elm: "option", value: "admin", text: "Administrator" })
        );
        let accountPhoneInput = createElm({ elm: "input", type: "text", placeholder: "Enter phone number...", required: true, name: "phone" });
        let accountMailInput = createElm({ elm: "input", type: "text", placeholder: "Enter E-mail...", required: true, name: "email" });
        let btnContainer = createElm({ elm: "div", classes: "btnContainer" });
        let saveAccountBtn = createElm({ elm: "button", type: "submit", id: "saveAccBtn", classes: "saveButton", text: 'Save Account' });
        btnContainer.append(saveAccountBtn);

        accountForm.append(
            createElm({ elm: "label", text: 'Name' }), 
            accountNameInput,
            createElm({ elm: "label", text: 'Role' }),
            roleOptions,
            createElm({ elm: "label", text: 'Phone number' }),
            accountPhoneInput,
            createElm({ elm: "label", text: 'E-mail' }),
            accountMailInput,
            btnContainer
        );
        
        if(fill){
            let deleteAccountBtn = createElm({ elm: "button", type: "button", id: "deleteAccBtn", classes: "deleteButton", text: 'Delete Account' });
            btnContainer.append(deleteAccountBtn);
            new this.#Request("getEmployee", { id: this.#identifier }, (employee) => {
                accountNameInput.value = employee["name"];
                accountPhoneInput.value = employee["phone"];
                accountMailInput.value = employee["email"];
                roleOptions.value = employee["role"];
            });
            deleteAccountBtn.addEventListener("click", () => {
                new this.#Request("deleteEmployee", { id: this.#identifier }, () => {
                    location.reload();
                });
            });
        }

        accountForm.addEventListener("submit", (e) => {
            e.preventDefault();
            if (this.validatePhone(accountPhoneInput.value)) {
                this.validateEmail(accountMailInput.value) ? callback(new FormData(accountForm)) : alert("Incorrect e-mail");
            }
            else {
                alert("Incorrect phone number");
            }
        });
    }

    //Add client pop up creation
    addClientPopup() {
        this.clientPopupTemplate(false, (formData) => {
            new this.#Request("createClient", { name: formData.get("name"), phone: formData.get("phone"), email: formData.get("email"), acceptance: formData.get("acceptance"), state: formData.get("state") }, (data) => {
                if (data == "Email already exists") {
                    alert("Email already exists");
                }
                else {
                    location.reload();
                }
            });
        });
    }

    //Edit client pop up creation
    editClientPopup() {
        this.clientPopupTemplate(true, (formData) => {
            new this.#Request("updateClient", { id: this.#identifier, name: formData.get("name"), phone: formData.get("phone"), email: formData.get("email"), acceptance: formData.get("acceptance"), state: formData.get("state") }, (data) => {
                if (data == "Email already exists") {
                    alert("Email already exists");
                }
                else {
                    location.reload();
                }
            });
        });
    }

    clientPopupTemplate(fill, callback, createElm = this.#createElm){
        let clientForm = createElm({ elm: "form", id: "assignedContainer" });
        this.popupContent.append(clientForm);

        let clientNameInput = createElm({ elm: "input", type: "text", placeholder: "Enter name...", name: "name", required: true });

        let clientPhoneInput = createElm({ elm: "input", type: "number", placeholder: "Enter phone number...", name: "phone", required: true });

        let clientMailInput = createElm({ elm: "input", type: "text", placeholder: "Enter E-mail...", name: "email", required: true });

        let acceptanceOptions = createElm({ elm: "select", name: "acceptance" });
        acceptanceOptions.append(
            createElm({ elm: "option", value: "Not likely", text: "Not likely" }),
            createElm({ elm: "option", value: "Likely", text: "Likely" }),
            createElm({ elm: "option", value: "Very likely", text: "Very likely" }),
            createElm({ elm: "option", value: "Accepted", text: "Accepted" })
        );

        let stateOptions = createElm({ elm: "select", name: "state" });
        stateOptions.append(
            createElm({ elm: "option", value: "active", text: "Active" }),
            createElm({ elm: "option", value: "archived", text: "Archived" })
        );
        let btnContainer = createElm({ elm: "div", classes: "btnContainer" });
        let saveButton = createElm({ elm: "button", type: "submit", classes: "saveButton", text: 'Save' });
        btnContainer.append(saveButton);
        clientForm.append(
            createElm({ elm: "p", text: 'Name' }),
            clientNameInput,
            createElm({ elm: "p", text: 'Phone number' }),
            clientPhoneInput,
            createElm({ elm: "p", text: 'E-mail' }),
            clientMailInput,
            createElm({ elm: "p", text: 'Chance of acceptance' }), 
            acceptanceOptions,
            createElm({ elm: "p", text: 'State' }), 
            stateOptions,
            btnContainer
        );
        if(fill){
            new this.#Request("getClient", { id: this.#identifier }, (data) => {
                clientNameInput.value = data["name"];
                clientPhoneInput.value = data["phone"];
                clientMailInput.value = data["email"];
                acceptanceOptions.value = data["chance"];
                stateOptions.value = data["state"];
            });
        }
        clientForm.addEventListener("submit", (e) => {
            e.preventDefault();
            if (this.validatePhone(clientPhoneInput.value)) {
                this.validateEmail(clientMailInput.value) ? callback(new FormData(clientForm)) : alert("Incorrect e-mail");
            }
            else {
                alert("Incorrect phone number");
            }
        });
    }

    validatePhone(phone){
        let phonePattern = /^(?:(?:00|\+)?45)?(?=1|2|3|4|5|6|7|8|9)\d{8}$/g;
        return phonePattern.test(String(phone).toLowerCase());
    }

    validateEmail(email){
        let emailPattern = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/g;
        return emailPattern.test(String(email).toLowerCase());
    }

    //task pop up creation
    taskPageTaskPopUp(createElm = this.#createElm) {
        //add save button to top
        let saveBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M48 96V416c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16V170.5c0-4.2-1.7-8.3-4.7-11.3l33.9-33.9c12 12 18.7 28.3 18.7 45.3V416c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V96C0 60.7 28.7 32 64 32H309.5c17 0 33.3 6.7 45.3 18.7l74.5 74.5-33.9 33.9L320.8 84.7c-.3-.3-.5-.5-.8-.8V184c0 13.3-10.7 24-24 24H104c-13.3 0-24-10.7-24-24V80H64c-8.8 0-16 7.2-16 16zm80-16v80H272V80H128zm32 240a64 64 0 1 1 128 0 64 64 0 1 1 -128 0z"/></svg>' });
        let completBtn = createElm({ elm: "button", type: "button", classes: "saveButton", text: '<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z"/></svg>' });
        this.insertAfter(completBtn, this.popupTopHeadings);
        this.insertAfter(saveBtn, this.popupTopHeadings);
        new this.#Request("getTaskById", {taskId: this.#identifier}, (task) => {
            new this.#Request("getProjectByTaskId", {taskId: this.#identifier}, (project) => {
                this.popup.querySelector(".top .title").placeholder = "Task Name";
                this.popup.querySelector(".top .subtitle").placeholder = "Project Name";
                this.popup.querySelector(".top .title").value = task["name"];
                this.popup.querySelector(".top .subtitle").value = "Project: " + project["name"];
                this.popup.querySelector(".top .subtitle").setAttribute("disabled", true);

                completBtn.addEventListener("click", () => {
                    new this.#Request("finishTask", {taskId: this.#identifier}, () => {
                        console.log("task completed");
                    });
                });
                let taskDate = createElm({ elm: "input", type: "datetime-local", value: task["deadline"], id: "taskDeadline" });
                this.popupTopHeadings.append(
                    createElm({ elm: "label", for: "taskDeadline", text: "Deadline: " }),
                    taskDate
                );

                //Assigned
                let assignedContainer = createElm({ elm: "div", classes: "assignedContainer" });
                let assignedToSelect = createElm({ elm: "select", id: "assignedToSelect", classes: "sort" });
                let selectOption;
                new this.#Request("getEmployees", {}, (data) => {
                    data.forEach(employee => {
                        selectOption = createElm({ elm: "option", value: employee["id"], text: employee["name"] });
                        if (employee["id"] === task["employeeId"]) {
                            selectOption.setAttribute("selected", true);
                        }
                        assignedToSelect.append(selectOption);
                    });
                });

                assignedContainer.append(
                    createElm({ elm: "label", for: "assignedToSelect", text: "Assigned Employee:" }),
                    assignedToSelect
                );


                //Description
                let descriptionContainer = createElm({ elm: "div", classes: "descriptionContainer" });
                let descriptionHeader = createElm({ elm: "div", classes: "descriptionHeader" });
                descriptionHeader.append(
                    createElm({ elm: "h2", text: "Description:" }),
                );
                let desc = createElm({ elm: "textarea", classes: "description", text: task["description"] });
                descriptionContainer.append(descriptionHeader, desc);
                this.popupContent.append(assignedContainer, descriptionContainer);


                saveBtn.addEventListener("click", () => {
                    new this.#Request("updateTask", {taskId: this.#identifier, name: this.popup.querySelector(".top .title").value, description: desc.value, deadline: taskDate.value}, () => {
                        console.log("task updated");
                    });
                    this.removePopup();
                });
            });
        });

    }
}
