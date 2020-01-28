

$(function(){
    loadRecommendations();
});

function loadRecommendations(){

    // console.log(navigator.parameterManager.all());
    // console.log(navigator.parameterManager.get("api-key"));
    // console.log("--------");


    //
    // helpers.ajax({
    //     method: "GET",
    //     url: "/recommendation",
    //     "api-key": navigator.parameterManager.get("api-key"),
    //     data: {
    //         "amount": 15
    //     },
    //     success: function (data) {
    //         if( typeof data["rows"] !== "undefined" &&
    //             typeof data["rows"][0] !== "undefined" &&
    //             typeof data["rows"][0]["cells"] !== "undefined" &&
    //             helpers.objectSize(typeof data["rows"][0]["cells"]) > 0){
    //
    //             let columns = [];
    //             let rows = data["rows"];
    //             let cellsOfFirstObject = data["rows"][0]["cells"];
    //             let headOfTable = $("#list-of-recommendations").find("thead");
    //             let headRow = $("<tr></tr>");
    //             for(let i in cellsOfFirstObject){
    //                 columns[columns.length] = cellsOfFirstObject[i]["columnName"];
    //                 headRow.append("<th>"+cellsOfFirstObject[i]["columnName"]+"</th>");
    //             }
    //             headOfTable.append(headRow);
    //
    //             let tableData = [];
    //             for(let i in rows){
    //                 if(!rows.hasOwnProperty(i)) continue;
    //                 let cellObjects = rows[i]["cells"];
    //                 let cells = {};
    //                 for(let j in cellObjects){
    //                     if(!cellObjects.hasOwnProperty(j)) continue;
    //                     cells[cellObjects[j]["columnName"]] = cellObjects[j]["value"];
    //                 }
    //                 tableData[tableData.length] = cells;
    //             }
    //
    //             helpers.addTableData($("#list-of-recommendations"), tableData, columns,true);
    //         }
    //     },
    //     error: function (response) {
    //         console.log(response);
    //     }
    // })

}