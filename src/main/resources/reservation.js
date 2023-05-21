let seats = document.getElementById("seats");


for (let i = 0; i < 5; i++) {
    let row = document.createElement("tr");
    for (let j = 0; j < 10; j++) {
        let cell = document.createElement("td");
        cell.classList.add("seat__cell")
        
        let box = document.createElement("input");
        box.classList.add("seat__checkbox");
        box.type = "checkbox";
        
        let id = `seat${i}-${j}`;
        box.id = id;
        box.name = `${i}-${j}`;

        let label = document.createElement("label");
        label.htmlFor = id;
        label.classList.add("seat__label");


        cell.appendChild(box);
        cell.appendChild(label);
        row.appendChild(cell);
    }
    seats.appendChild(row);
}



