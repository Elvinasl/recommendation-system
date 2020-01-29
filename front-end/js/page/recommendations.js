

$(function(){

    loadRecommendations();


    $("#recommendation-for-user-btn").click(function(){
        let userId = $("#user-id").val();
        navigator.parameterManager.set("user", userId);
        navigator.updateHash();
        loadRecommendations(userId);

    });
});

function loadRecommendations(userId){

    let url = "/recommendation";
    if(typeof userId !== "undefined" && userId.length > 0){
        url += "/user/" + userId;
    }

    let table = $("#list-of-recommendations");
    helpers.clearTableHeaders(table);
    helpers.clearTableData(table);

    helpers.ajax({
        method: "GET",
        url: url,
        "api-key": navigator.parameterManager.get("api-key"),
        data: {
            "amount": 15
        },
        success: function (data) {
            if( typeof data["rows"] !== "undefined" &&
                typeof data["rows"][0] !== "undefined" &&
                typeof data["rows"][0]["cells"] !== "undefined" &&
                helpers.objectSize(typeof data["rows"][0]["cells"]) > 0){

                let columns = [];
                let rows = data["rows"];
                let cellsOfFirstObject = data["rows"][0]["cells"];
                let headOfTable = table.find("thead");
                let headRow = $("<tr></tr>");
                for(let i in cellsOfFirstObject){
                    columns[columns.length] = cellsOfFirstObject[i]["columnName"];
                    headRow.append("<th>"+cellsOfFirstObject[i]["columnName"]+"</th>");
                }
                headOfTable.append(headRow);

                let tableData = [];
                for(let i in rows){
                    if(!rows.hasOwnProperty(i)) continue;
                    let cellObjects = rows[i]["cells"];
                    let cells = {};
                    for(let j in cellObjects){
                        if(!cellObjects.hasOwnProperty(j)) continue;
                        cells[cellObjects[j]["columnName"]] = cellObjects[j]["value"];
                    }
                    tableData[tableData.length] = cells;
                }

                helpers.addTableData(table, tableData, columns,true);
            }
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });

}