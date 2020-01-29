

$(function(){
    loadProjectRows();
});

function loadProjectRows(){

    let table = $("#list-of-rows");
    helpers.clearTableHeaders(table);
    helpers.clearTableData(table);

    let apiKey = navigator.parameterManager.get("api-key");
    helpers.ajax({
        method: "GET",
        url: "/projects/"+apiKey+"/rows",
        success: function (data) {
            if( typeof data["objects"] !== "undefined" &&
                typeof data["objects"][0] !== "undefined" &&
                typeof data["objects"][0]["cells"] !== "undefined" &&
                helpers.objectSize(typeof data["objects"][0]["cells"]) > 0){

                let columns = [];
                let rows = data["objects"];
                let cellsOfFirstObject = rows[0]["cells"];
                let headOfTable = table.find("thead");
                headOfTable.html("");
                let headRow = $("<tr></tr>");

                // Id column
                headRow.append("<th>#</th>");
                columns[columns.length] = "id";

                for(let i in cellsOfFirstObject){
                    columns[columns.length] = cellsOfFirstObject[i]["columnName"];
                    headRow.append("<th>"+cellsOfFirstObject[i]["columnName"]+"</th>");
                }

                // Reactions column
                headRow.append("<th>Reactions</th>");
                columns[columns.length] = "reactions";

                // Actions column
                headRow.append("<th>Actions</th>");
                columns[columns.length] = "actions";


                headOfTable.append(headRow);

                let tableData = [];
                for(let i in rows){
                    if(!rows.hasOwnProperty(i)) continue;

                    let cellObjects = rows[i]["cells"];
                    let cells = {};

                    cells["id"] = rows[i]["id"];
                    cells["reactions"] = rows[i]["reactions"];
                    cells["actions"] = $("<div class='actions'></div>");
                    cells["actions"].append(getEditBtn(rows[i]["id"], cellObjects));
                    cells["actions"].append(getDeleteBtn(rows[i]["id"]));

                    // Add all other field data
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
            console.log(response);
        }
    });

}


function getEditBtn(id, cells){

    let btn = $("<button class='btn btn-sm btn-primary'><i class='fa fa-edit'></i></button>");
    btn.click(function(){
        editRow($(this).data('id'), $(this).data('cells'));
    });
    btn.data('id', id);
    btn.data('cells', cells);
    return btn;
}
function getDeleteBtn(id){

    let btn = $("<button class='btn btn-sm btn-danger'><i class='fa fa-times'></i></button>");
    btn.click(function(){

        if(confirm("Are you sure you want to delete this row?")){
            deleteRow($(this).data('id'));
        }
    });
    btn.data('id', id);
    return btn;
}

function editRow(id, cells){
    let projectModal = $("#edit-project-modal");
    projectModal.find(".modal-title").html("Edit row");

    let contentElement = projectModal.find(".edit-cells-content");
    contentElement.html("");
    for (let i in cells){
        let column = cells[i]["columnName"];
        let value = cells[i]["value"];
        contentElement.append(
            "<div class='form-group'>" +
            "<label for='" + column + "-value'>"+column + "</label>" +
            "<input type='text' class='form-control' name='" + column + "-value' id='" + column + "-value' value='" + value + "' />" +
            "</div>"
        );
    }

    projectModal.find(".modal-save-btn").off("click");
    projectModal.find(".modal-save-btn").on("click", function(e){

        projectModal.modal("hide");
        let data = { cells: [] };

        for (let i in cells){
            let column = cells[i]["columnName"];
            let cellId = cells[i]["id"];
            // let oldValue = cells[i]["value"];
            let input = contentElement.find("#" + column + "-value");
            if(typeof input !== "undefined"){
                data.cells[data.cells.length] = {
                    id: cellId,
                    columnName: column,
                    value: input.val()
                }
            }
        }
        helpers.ajax({
            method: "PUT",
            url: "/rows/"+id,
            data: data,
            success: function (data) {
                projectModal.modal("hide");
                if(typeof data['message'] !== "undefined"){
                    helpers.alert(data['message'], "success");
                }
                loadProjectRows();
            },
            error: function (response) {
                helpers.alert("Something went wrong", "danger", 5000, projectModal.find(".modal-errors"));
            }
        });
    });
    projectModal.modal("show");
}
function deleteRow(id){
    helpers.ajax({
        method: "DELETE",
        url: "/rows/"+id,
        success: function (data) {
            if(typeof data["message"] !== "undefined"){
                helpers.alert(data["message"], "success", 3000);
            }
            loadProjectRows();
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });
}