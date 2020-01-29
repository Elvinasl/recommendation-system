

$(function(){
    loadRecommendations();
});

function loadRecommendations(){

    // console.log(navigator.parameterManager.all());
    // console.log(navigator.parameterManager.get("api-key"));
    // console.log("--------");



    helpers.ajax({
        method: "GET",
        url: "/recommendation",
        "api-key": navigator.parameterManager.get("api-key"),
        data: {
            "amount": 15
        },
        success: function (data) {
            if( typeof data["rows"] !== "undefined" &&
                typeof data["rows"][0] !== "undefined" &&
                typeof data["rows"][0]["cells"] !== "undefined" &&
                helpers.objectSize(typeof data["rows"][0]["cells"]) > 0){

                let columns = [ "#" ];
                let rows = data["rows"];
                let cellsOfFirstObject = data["rows"][0]["cells"];
                let headOfTable = $("#list-of-recommendations").find("thead");
                let headRow = $("<tr><td>#</td></tr>");
                for(let i in cellsOfFirstObject){
                    columns[columns.length] = cellsOfFirstObject[i]["columnName"];
                    headRow.append("<th>"+cellsOfFirstObject[i]["columnName"]+"</th>");
                }
                headOfTable.append(headRow);

                let tableData = [];
                for(let i in rows){
                    if(!rows.hasOwnProperty(i)) continue;
                    let cellObjects = rows[i]["cells"];
                    let cells = { "#": helpers.objectSize(rows)-i };
                    for(let j in cellObjects){
                        if(!cellObjects.hasOwnProperty(j)) continue;
                        cells[cellObjects[j]["columnName"]] = cellObjects[j]["value"];
                    }
                    tableData[tableData.length] = cells;
                }

                helpers.addTableData($("#list-of-recommendations"), tableData, columns,true);
            }
        },
        error: function (response) {
            console.log(response);
        }
    })

    // helpers.clearTableData($("#list-of-projects"));
    // helpers.ajax({
    //     method: "GET",
    //     url: "/projects",
    //     success: function (data) {
    //         let rows = data["objects"];
    //
    //
    //         for(let row in rows){
    //             if(!rows.hasOwnProperty(row)) continue;
    //             rows[row]['refresh-key'] = getRefreshKeyBtn(rows[row]['apiKey']);
    //             rows[row]['edit'] = getEditBtn(rows[row]['name'], rows[row]['apiKey']);
    //             rows[row]['delete'] = getDeleteBtn(rows[row]['apiKey']);
    //         }
    //         helpers.addTableData($("#list-of-projects"), data["objects"], ["name", "apiKey", "refresh-key", "edit", "delete"],true);
    //     },
    //     error: function (response) {
    //         helpers.alert("Something went wrong", "danger", 5000);
    //     }
    // });
    //
    // $("#add-project-btn").click(addProject);
}