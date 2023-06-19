(async () => {
    let [_, showId] = window.location.href
    .split("?")[1]
    .split("&")
    .filter(item => item.startsWith("showId="))[0].split("=");

    let showData = await fetch(`/get_show_data?showId=${showId}`)
        .then(res => res.json());

    console.log(showData);

    let room = showData.data.movieRoom
    let seatsDisplay = document.getElementById("seats");

    for (let i = 0; i < room.rows; i++) {
        let row = document.createElement("tr");
        let labelY = document.createElement("td");
        labelY.classList.add("seats__label");
        labelY.textContent = i + 1;
        row.appendChild(labelY)

        for (let j = 0; j < room.seats; j++) {

            let cell = document.createElement("td");
            cell.classList.add("seat__cell")
            
            let box = document.createElement("input");
            box.classList.add("seat__checkbox");
            box.type = "checkbox";
            
            let id = `${i + 1}-${j + 1}`;

            let boxId = `seat${id}`;
            box.id = boxId;
            box.name = `seat-${i + 1}x${j + 1}`;
    
            let label = document.createElement("label");
            label.htmlFor = boxId;
            label.classList.add("seat__label");
    
    
            cell.appendChild(box);
            cell.appendChild(label);
            row.appendChild(cell);
        }
        seatsDisplay.appendChild(row);

    }
    let rowX = document.createElement("tr");
    rowX.appendChild(document.createElement("td"));
    for (let i = 0; i < room.seats; i++) {
        let labelX = document.createElement("td");
        labelX.classList.add("seats__label");
        labelX.textContent = i + 1;
        rowX.appendChild(labelX);
        rowX.appendChild(labelX);
    }
    seatsDisplay.appendChild(rowX);

    let reservedSeats = [].concat.apply([], showData.data.reservations.map(item => item.details));
    for (let seat of reservedSeats) {
        document.getElementById(`seat${seat.row}-${seat.seat}`).disabled = true;
        document.getElementById(`seat${seat.row}-${seat.seat}`).classList.add("seat__label--disabled");
    }

    let inputElem = document.getElementById("reservation-userid");
    inputElem.value = 1;
    inputElem.style.display = "none";
    document.getElementById("reservation-showid").value = showId;
})()
