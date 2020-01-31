

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

                if(typeof userId !== "undefined" && userId.length > 0){
                    // Actions column
                    headRow.append("<th>Actions</th>");
                    columns[columns.length] = "actions";
                }

                headOfTable.append(headRow);

                let tableData = [];
                for(let i in rows){
                    if(!rows.hasOwnProperty(i)) continue;
                    let cellObjects = rows[i]["cells"];
                    let cells = {};


                    if(typeof userId !== "undefined" && userId.length > 0){
                        cells["actions"] = $("<div class='actions'></div>");
                        cells["actions"].append(behaviorBtn(true, cellObjects, userId));
                        cells["actions"].append(behaviorBtn(false, cellObjects, userId));
                    }


                    for(let j in cellObjects){
                        if(!cellObjects.hasOwnProperty(j)) continue;
                        cells[cellObjects[j]["columnName"]] = cellObjects[j]["value"];
                    }
                    tableData[tableData.length] = cells;
                }

                helpers.addTableData(table, tableData, columns,false);
            }
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });

}

function behaviorBtn(like, cells, userId){

    if(typeof userId === "undefined" || userId.length === 0){
        return "";
    }

    let icon = like ? "thumbs-up" : "thumbs-down";
    let btn = $("<button class='btn btn-sm btn-primary'><i class='fas fa-"+icon+"'></i></button>");
    btn.click(function(){
        sendBehavior($(this).data('like'), $(this).data('cells'), $(this).data('user-id'));
    });
    btn.data('like', like);
    btn.data('cells', cells);
    btn.data('user-id', userId);
    return btn;
}

function sendBehavior(like, cells, userId){
    if(typeof userId === "undefined" || userId.length === 0){
        return;
    }
    let dataToSend = {
        cells: cells,
        liked: like === true,
        userId: userId
    };

    helpers.ajax({
        method: "POST",
        url: "/behavior",
        "api-key": navigator.parameterManager.get("api-key"),
        data: dataToSend,
        success: function (data) {
            if(typeof data['message'] !== "undefined"){
                helpers.alert(data['message'], "success");
            }
            loadRecommendations(userId);
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    })

}