import("./main.mjs").then((module) => {
    let { createElm, Request } = module;
    //Uses the Request class to send a post request to the server that returns an the logged in user's data
    new Request("getProfile", {}, (profile) => {
        document.getElementById('topContent').append(
            createElm({ elm: "p", classes: "date", text: new Date().toLocaleDateString('en-us', { weekday: 'long', month: 'long', day: 'numeric' }) }),
            createElm({ elm: "h1", classes: "welcome", text: "Welcome, "+profile["name"] })
        );
    });

    //Uses the Request class to send a post request to the server that returns an array of json objects of the most recent tasks assigned to the logged in user
    new Request("recentTasks", {}, (tasks) => {
        tasks.forEach((task) => {
            document.getElementById("myTasks").append(createElm({ elm: "p", classes: "myTasks", text: task["name"] }));
        });
    });

    //Uses the Request class to send a post request to the server that returns an array of json objects of the most recent projects
    new Request("recentProjects", {}, (projects) => {
        projects.forEach((project) => {
            document.getElementById("projects").append(createElm({ elm: "p", classes: "project", text: project["name"] }));
        });
    });

    //Uses the Request class to send a post request to the server that returns an array of json objects of the most recent clients
    new Request("recentClients", {}, (clients) => {
        clients.forEach((client) => {
            document.getElementById("clients").append(createElm({ elm: "p", classes: "client", text: client["name"] }));
        });
    });

    //Uses the Request class to send a post request to the server that returns an array of json objects of the accounts in the system
    new Request("recentAccounts", {}, (accounts) => {
        if (accounts == "Not an admin"){
            document.getElementById("myAccounts").remove();
        } else {
            accounts.forEach((account) => {
                document.getElementById("myAccounts").append(createElm({elm: "p", classes: "account", text: account["name"]}));
            });
        }
    });
})