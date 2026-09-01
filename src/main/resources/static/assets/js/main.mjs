//let username = localStorage.getItem("username");
//let password = localStorage.getItem("password");
//new Request("login", {username, password});

//The createElm function is used to create html elements easily
export let createElm = (data) => {
    let elm = document.createElement(data["elm"]);
    if (data["id"]) {
        elm.id = data["id"];
    }
    if (data["classes"]) {
        if (Array.isArray(data["classes"])) {
            data["classes"].forEach(single => {
                elm.classList.add(single);
            });
        }
        else {
            elm.classList.add(data["classes"]);
        }
    }
    if (data["text"]) {
        elm.innerHTML = data["text"];
    }
    if (data["name"]) {
        elm.name = data["name"];
    }
    if (data["placeholder"]) {
        elm.placeholder = data["placeholder"];
    }
    if (data["type"]) {
        elm.type = data["type"];
    }
    if (data["href"]) {
        elm.href = data["href"];
    }
    if (data["src"]) {
        elm.src = data["src"];
    }
    if (data["for"]) {
        elm.htmlFor = data["for"];
    }
    if (data["hide"]) {
        elm.style.display = "none";
    }
    if (data["rel"]) {
        elm.rel = data["rel"];
    }
    if (data["value"]) {
        elm.value = data["value"];
    }
    if (data["required"]) {
        elm.setAttribute("required", "required");
    }
    if (data["method"]) {
        elm.method = data["method"];
    }
    return elm;
}

//The Request class is used to send post requests to the server
export class Request {
    constructor(req, param, callback) {
        (async () => {
            try {
                const rawResponse = await fetch('./' + req, {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(param)
                });

                if (!rawResponse.ok) {
                    throw new Error(`HTTP error! Status: ${rawResponse.status}`);
                }

                const data = await rawResponse.json();
                callback(data);
            } catch (error) {
                console.error('Error in Request:', error);
            }
        })();
    }
}

//The confirmBox function is used to display a confirmation box to the user
export let confirmBox = (msg, callback) => {
    let confirmBox = createElm({elm: "div", classes: "confirmBox"});
    let confirmText = createElm({elm: "p", text: msg});
    let confirmBtn = createElm({elm: "button", type: "button", text: "Yes"});
    let cancelBtn = createElm({elm: "button", type: "button", text: "No"});
    confirmBox.append(confirmText, confirmBtn, cancelBtn);
    document.body.append(confirmBox);
    confirmBtn.addEventListener("click", () => {
        confirmBox.remove();
        callback();
    });
    cancelBtn.addEventListener("click", () => {
        confirmBox.remove();
    });
}
