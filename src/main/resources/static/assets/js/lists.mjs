// A class for creating lists
export class List {
    #columns;
    #createElm;
    #tabs;
    #table;
    tabBtns=[];
    #addRows;
    #searchInput=null;
    constructor(columns, tabs=null, addRows, createElm) {
        this.#searchInput = document.getElementById("search");
        //columns is an array of strings that determines the structure of the table
        this.#columns = columns;
        this.#createElm = createElm;
        //tabs is an array of strings that determines the tabs in the list
        this.#tabs = tabs;
        //addRows is a function that takes a function as parameter that determines the structure of the rows
        this.#addRows = addRows;
        this.#listSetup();
        if(this.#tabs) this.#addRows(this.#tabs[0]);
        else if(this.#searchInput) this.#addRows(null);
        else this.#addRows();
        this.#checkEmpty();
    }

    get table(){
        return this.#table;
    }

    #checkEmpty(){
        if(this.table.querySelectorAll("tr") == 1){
            document.getElementById("list").innerHTML = "<tr><td colspan='"+this.#columns.length+"' class='notAvailable'>Nothing found</td></tr>";
        }
    }

    #listSetup(createElm = this.#createElm) {
        if(this.#tabs){
            let btnContainer = createElm({elm: "div", id: "buttons"});
            document.getElementById("content").append(btnContainer);
            this.#tabs.forEach((tab, index) => {
                let btn = createElm({elm: "button", type: "button", value: tab, text: tab.charAt(0).toUpperCase() + tab.slice(1)});
                if(index == 0) btn.classList.add("open");
                btnContainer.append(btn);
                this.tabBtns.push(btn);
                btn.addEventListener("click", function(){
                    btnContainer.querySelector(".open").classList.remove("open");
                    btn.classList.add("open");
                    this.#clear();
                    this.#addRows(btn.value);
                    this.#checkEmpty();
                }.bind(this), true);
            });
        }
        else if(this.#searchInput){
            let searchBefore = this.#searchInput.value;
            let timeout;
            this.#searchInput.addEventListener("input", () => {
                clearTimeout(timeout);
                timeout = setTimeout(function(){
                    if(this.#searchInput.value != searchBefore){
                        searchBefore = this.#searchInput.value;
                        this.#clear();
                        if(this.#searchInput.value == "") this.#addRows(null);
                        else this.#addRows(this.#searchInput.value);
                        this.#checkEmpty();
                    }
                }.bind(this), 1000);
            });
        }
        let table = createElm({elm: "table"});
        let thead = createElm({elm: "thead"});
        let headRow = createElm({elm: "tr"});
        thead.append(headRow);
        this.#columns.forEach(column => {
            headRow.append(createElm({elm: "th", text: column.charAt(0).toUpperCase() + column.slice(1)}));
        });

        this.#table = createElm({elm: "tbody", id: "list"});
        table.append(thead, this.#table);

        document.getElementById("content").append(table);
    }

    #clear(){
        this.#table.innerHTML = "";
    }
}