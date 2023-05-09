let seats = document.getElementById("seats");


for (let i = 0; i < 5; i++) {
    let row = document.createElement("tr");
    for (let j = 0; j < 10; j++) {
        let cell = document.createElement("td");
        let box = document.createElement("input");
        box.type = "checkbox";
        box.id = `seat{i}-{j}`
        box.name = `{i}-{j}`

        cell.appendChild(box);
        row.appendChild(cell);
    }
    seats.appendChild(row);
}