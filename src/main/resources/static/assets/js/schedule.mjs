import("./main.mjs").then((module) => {
  let { createElm, Request } = module;
  import("./popUp.mjs").then((module)=> {
    let { Popup } = module;
    const today = new Date();
    let currentDate = new Date();
    let mode = "day";
    currentDate.setHours(8, 0, 0);
    let infoHeaderContainer, prevBtn, todayBtn, nextBtn, todayDate;
    //Creates the navigation buttons for the schedule
    let createNavBtns = () => {
      let dayBtn = createElm({ elm: "button", type: "button", text: "Day", classes: "open", value: "daySchedule" });
      let weekBtn = createElm({ elm: "button", type: "button", text: "Week", value: "weekSchedule" });
      let monthBtn = createElm({ elm: "button", type: "button", text: "Month", value: "monthSchedule" });
    
      document.getElementById("scheduleButtons").append(dayBtn, weekBtn, monthBtn);
      document.querySelectorAll("#scheduleButtons > button").forEach(button => {
        button.addEventListener("click", () => {navigateSchedule(button)});
      });

      infoHeaderContainer = createElm({ elm: "div", classes: "infoHeaderContainer" });
      prevBtn = createElm({ elm: "button", type: "button", classes: "prevBtn", text: '<svg xmlns="http://www.w3.org/2000/svg" height="16" width="16" viewBox="0 0 512 512"><path d="M512 256A256 256 0 1 0 0 256a256 256 0 1 0 512 0zM231 127c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-71 71L376 232c13.3 0 24 10.7 24 24s-10.7 24-24 24l-182.1 0 71 71c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0L119 273c-9.4-9.4-9.4-24.6 0-33.9L231 127z"/></svg>' });
      todayBtn = createElm({ elm: "button", type: "button", classes: "todayBtn", text: "Today" });
      nextBtn = createElm({ elm: "button", type: "button", classes: "nextBtn", text: '<svg xmlns="http://www.w3.org/2000/svg" height="16" width="16" viewBox="0 0 512 512"><path d="M0 256a256 256 0 1 0 512 0A256 256 0 1 0 0 256zM281 385c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l71-71L136 280c-13.3 0-24-10.7-24-24s10.7-24 24-24l182.1 0-71-71c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0L393 239c9.4 9.4 9.4 24.6 0 33.9L281 385z"/></svg>' });
      todayDate = createElm({ elm: "div", classes: "todayDate", text: currentDate.toLocaleDateString() })
      infoHeaderContainer.append(todayDate, prevBtn, todayBtn, nextBtn);

      prevBtn.addEventListener("click", prev);
      nextBtn.addEventListener("click", next);
      todayBtn.addEventListener("click", () => {
        currentDate = new Date();
        updateSchedule();
      });

      navigateSchedule(dayBtn);
    }

    //Updates the schedule based on the current mode and what date is selected
    let updateSchedule = () => {
      if(mode == "day"){
        todayDate.textContent = currentDate.toLocaleDateString();
        createDaySchedule();
      }
      else if(mode == "week"){
        fixWeek();
        createWeekSchedule();
      }
      else if(mode == "month"){
        setCurrentTo1st();
        createMonthSchedule();
      }
    }

    //Goes forward in the schedule based on the current mode and updates the schedule afterwards
    let next = () => {
      if(mode == "day"){
        currentDate.setDate(currentDate.getDate() + 1);
        updateSchedule();
      }
      else if(mode == "week"){
        currentDate = new Date(currentDate.getTime() + 604800000);
        updateSchedule();
      }
      else if(mode == "month"){
        currentDate = new Date(currentDate.getTime() + 86400000*getDaysInMonth());
        updateSchedule();
      }
    }

    //Goes backward in the schedule based on the current mode and updates the schedule afterwards
    let prev = () => {
      if(mode == "day"){
        currentDate.setDate(currentDate.getDate() - 1);
        updateSchedule();
      }
      else if(mode == "week"){
        currentDate = new Date(currentDate.getTime() - 604800000);
        updateSchedule();
      }
      else if(mode == "month"){
        currentDate = new Date(currentDate.getTime() - 86400000);
        updateSchedule();
      }
    }

    //Changes the mode of the schedule based on the button pressed and updates the schedule afterwards
    let navigateSchedule = (button) => {
      document.querySelector("#scheduleButtons .open").classList.remove("open");
      button.classList.add("open");
      currentDate = new Date();
      switch(button.value){
        case "daySchedule":
          mode = "day";
          if(document.querySelector(".daysContainer")){
            document.querySelector(".daysContainer").remove();
          }
          todayDate.textContent = currentDate.toLocaleDateString();
          updateSchedule();
          break;
        case "weekSchedule":
          mode = "week";
          let daysContainer = createElm({ elm: "div", classes: "daysContainer" });
          daysContainer.append(
            createElm({ elm: "div", classes: "day" }),
            createElm({ elm: "div", classes: "day", text: "Monday" }),
            createElm({ elm: "div", classes: "day", text: "Tuesday" }),
            createElm({ elm: "div", classes: "day", text: "Wednesday" }),
            createElm({ elm: "div", classes: "day", text: "Thursday" }),
            createElm({ elm: "div", classes: "day", text: "Friday" }),
            createElm({ elm: "div", classes: "day", text: "Saturday" }),
            createElm({ elm: "div", classes: "day", text: "Sunday" })
          );
          infoHeaderContainer.append(daysContainer);
          updateSchedule();
          break;
        case "monthSchedule":
          mode = "month";
          if(document.querySelector(".daysContainer")){
            document.querySelector(".daysContainer").remove();
          }
          updateSchedule();
          break;
      }
    }

    //Fixes the date to be the first day of the week and updates the date text
    let fixWeek = () => {
      currentDate.setHours(8, 0, 0);
      if(currentDate.getDay() != 1){
        if(currentDate.getDay() == 0) currentDate.setDate(currentDate.getDate() - 6);
        else currentDate.setDate(currentDate.getDate() - (currentDate.getDay() - 1));
      }
      let lastDayOfWeek = new Date(currentDate.getTime());
      lastDayOfWeek.setDate(lastDayOfWeek.getDate() + 6);

      todayDate.textContent = currentDate.toLocaleDateString()+" - "+lastDayOfWeek.toLocaleDateString();
    }

    //Gets the number of days in the current month
    getDaysInMonth = () => {
      return new Date(currentDate.getFullYear(), currentDate.getMonth()+1, 0).getDate();
    }

    //Sets the date to the first day of the month and updates the date text
    let setCurrentTo1st = () => {
      if(currentDate.getDate() != 1){
        currentDate.setDate(currentDate.getDate() - (currentDate.getDate() - 1));
      }
      currentDate.setHours(0, 0, 0);
      let lastDayOfMonth = new Date(currentDate.getTime() + 86400000*(getDaysInMonth()-1));
      todayDate.textContent = currentDate.toLocaleDateString()+" - "+lastDayOfMonth.toLocaleDateString();
    }

    //Creates the schedule html elements for the day mode
    let createDaySchedule = () => {
      currentDate.setHours(8, 0, 0);
      /*Table for day schedule*/
      let dayTable = createElm({ elm: "div", classes: "scheduleDay" });
      document.getElementById("schedule").innerHTML = "";
      document.getElementById("schedule").append(infoHeaderContainer, dayTable);
      let startTime = new Date(currentDate.setHours(8, 0, 0));

      while (startTime.getHours() < 17){
        let hourRow = createElm({ elm: "div", classes: "hourRow" });
        let hour = createElm({ elm: "div", classes: "hour", text: (startTime.getHours() < 10) ? "0"+startTime.getHours()+":00" : startTime.getHours()+":00" });
        let hourContent = createElm({ elm: "div", classes: "hourContent" });
        new Request("getTasksForDate", { date: startTime.getTime(), format: "hour"}, (tasks) => {
          tasks.forEach((task) => {
            insertTask(task, hourContent);
          });
        });
        new Request("getMeetingsForDate", { date: startTime.getTime(), format: "hour"}, (meetings) => {
          meetings.forEach((meeting) => {
            insertMeeting(meeting, hourContent);
          });
        });

        hourRow.append(hour, hourContent);
        dayTable.append(hourRow);
        startTime.setHours(startTime.getHours() + 1);
      }

    }

    //Creates the schedule html elements for the week mode
    let createWeekSchedule = () => {
      /*Table for day schedule*/
      let weekTable = createElm({ elm: "div", classes: "scheduleWeek" });
      document.getElementById("schedule").innerHTML = "";
      document.getElementById("schedule").append(infoHeaderContainer, weekTable);
      let startTime = new Date(currentDate);
      let weekDay = 0;
      while(startTime.getHours() < 17){
        let hour = createElm({ elm: "div", classes: "hour", text: (startTime.getHours() < 10) ? "0"+startTime.getHours()+":00" : startTime.getHours()+":00" });
        weekTable.append(hour);
        startTime.setHours(startTime.getHours() + 1);
      }
      startTime.setHours(8, 0, 0);
      while (weekDay < 7){
        
        while (startTime.getHours() < 17){
          let hourRow = createElm({ elm: "div", classes: "hourRow" });
          let hourContent = createElm({ elm: "div", classes: "hourContent" });
          
          new Request("getTasksForDate", { date: startTime.getTime(), format: "hour"}, (tasks) => {
            tasks.forEach((task) => {
              insertTask(task, hourContent);
            });
          });
          new Request("getMeetingsForDate", { date: startTime.getTime(), format: "hour"}, (meetings) => {
            meetings.forEach((meeting) => {
              insertMeeting(meeting, hourContent);
            });
          });

          hourRow.append(hourContent);
          weekTable.append(hourRow);
          startTime.setHours(startTime.getHours() + 1);
        }
        startTime.setHours(8, 0, 0);
        startTime.setDate(startTime.getDate() + 1);
        weekDay++;
      }
    }

    //Creates the schedule html elements for the month mode
    let createMonthSchedule = () => { 
      /*Table for day schedule*/
      let monthTable = createElm({ elm: "div", classes: "scheduleMonth" });
      document.getElementById("schedule").innerHTML = "";
      document.getElementById("schedule").append(infoHeaderContainer, monthTable);
      let startTime = new Date(currentDate);
      while (startTime.getMonth() == currentDate.getMonth()){
        let day = createElm({ elm: "div", classes: "day" });
        new Request("getTasksForDate", { date: startTime.getTime(), format: "day"}, (tasks) => {
          tasks.forEach((task) => {
            insertTask(task, day);
          });
        });
        new Request("getMeetingsForDate", { date: startTime.getTime(), format: "day"}, (meetings) => {
          meetings.forEach((meeting) => {
            insertMeeting(meeting, day);
          });
        });
        day.append(createElm({ elm: "div", classes: "date", text: startTime.getDate() }));
        monthTable.append(day);
        startTime.setDate(startTime.getDate() + 1);
      }
    }

    //Inserts tasks for a cell in the schedule
    let insertTask = (task, cell) => {
      let taskBox = createElm({ elm: "div", classes: "task", text: task["name"] });
      cell.append(taskBox);
      new Request("getProjectByTaskId", {taskId: task["id"]}, (project) => {
        taskBox.addEventListener("click", () => {
          createTaskPopup(task, project);
        });
      });
    }

    //Inserts meetings for a cell in the schedule
    let insertMeeting = (meeting, cell) => {
      let meetingBox = createElm({ elm: "div", classes: "meeting", text: meeting["name"] });
      cell.append(meetingBox);
      new Request("getProjectByMeetingId", {meetingId: meeting["id"]}, (project) => {
        meetingBox.addEventListener("click", () => {
          createMeetingPopup(meeting, project);
        });
      });
    }

    //Creates a popup for a task
    let createTaskPopup = (task, project) => {
      let popupBody = createElm({ elm: "div", classes: ["popupBody", "taskPopup"] });
      let popupContent = createElm({ elm: "div", classes: "popupContent" });
      let popupBackdrop = createElm({ elm: "div", classes: "popupBackdrop" });
      popupBody.append(popupBackdrop, popupContent);
      let closeBtn = createElm({ elm: "button", type: "button", text: "x" });
      popupContent.append(
        closeBtn,
        createElm({ elm: "p", text: "Name: "+task["name"] }),
        createElm({ elm: "p", text: "Description: "+task["description"] }),
        createElm({ elm: "p", text: "Deadline: "+new Date(task["deadline"]).toLocaleString() }),
        createElm({ elm: "p", text: task["completed"] ? "Status: Completed" : "Status: Not completed" }),
        createElm({ elm: "p", text: "Project name: "+project["name"] })
      );
      closeBtn.addEventListener("click", () => {
        popupBody.remove();
      });
      popupBackdrop.addEventListener("click", () => {
        popupBody.remove();
      });
      document.body.append(popupBody);
    }

    //Creates a popup for a meeting
    let createMeetingPopup = (meeting, project) => {
      let popupBody = createElm({ elm: "div", classes: ["popupBody", "meetingPopup"] });
      let popupContent = createElm({ elm: "div", classes: "popupContent" });
      let popupBackdrop = createElm({ elm: "div", classes: "popupBackdrop" });
      popupBody.append(popupBackdrop, popupContent);
      let closeBtn = createElm({ elm: "button", type: "button", text: "x" });
      popupContent.append(
        closeBtn,
        createElm({ elm: "p", text: "Name: "+meeting["name"] }),
        createElm({ elm: "p", text: "Description: "+meeting["description"] }),
        createElm({ elm: "p", text: "Meeting start: "+new Date(meeting["startTime"]).toLocaleString() }),
        createElm({ elm: "p", text: "Duration: "+meeting["duration"]+" minutes" }),
        createElm({ elm: "p", text: "Project name: "+project["name"] })
      );
      closeBtn.addEventListener("click", () => {
        popupBody.remove();
      });
      popupBackdrop.addEventListener("click", () => {
        popupBody.remove();
      });
      document.body.append(popupBody);
    }

    createNavBtns();
  });
});