
$(function(){
    loadProjectRows();

    $("#upload-data-input").change(uploadDataInputChange);

    $("#upload-data-modal .modal-save-btn").hide().click(convertFile);
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


                //
                // for(let i in data["objects"]){
                //     delete data["objects"][i]["id"];
                //     delete data["objects"][i]["reactions"];
                //     for(let j in data["objects"][i]["cells"]){
                //         delete data["objects"][i]["cells"][j]["id"];
                //         data["objects"][i]["cells"][j]["weight"] = 50;
                //     }
                // }
                // data["columns"] = [];
                // for(let i=0; i<columns.length-2;i++){
                //     data["columns"][data["columns"].length] = {
                //         name: columns[i],
                //         weight: 50
                //     }
                // }
                //
                // data["rows"] = data["objects"];
                // delete data["objects"];
                // console.log(data);
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


function uploadDataInputChange(e){
    let file = e.target.files[0];
    $("label[for='upload-data-input']").html(file.name);
    let reader = new FileReader();
    reader.onload = function(e){
        try{
            window.uploadData = JSON.parse(reader.result);
            $("#upload-data-modal .modal-save-btn").fadeIn(300);
        }catch (e) {
            $("#upload-data-modal .modal-save-btn").fadeOut(300);
            window.uploadData = null;
            helpers.alert("Can't read file. It must be an JSON file.", "danger", 5000, $(".upload-data-errors"));
        }
    };
    reader.readAsText(file);
}

function convertFile() {
    if(window.uploadData == null){
        helpers.alert("No file to upload", "danger", 3000, $(".upload-data-errors"));
        return;
    }

    helpers.ajax({
        method: "POST",
        url: "/import",
        "api-key": navigator.parameterManager.get("api-key"),
        data: uploadData,
        success: function (data) {
            if(typeof data["message"] !== "undefined"){
                helpers.alert(data["message"], "success", 3000);
            }
            $("#upload-data-modal").modal('hide');
            loadProjectRows();
        },
        error: function(response){
            let text = "Something went wrong";
            if(response.status === 409){

                try{
                    data = JSON.parse(response.responseText);
                    text = data['message'];
                }catch (e) {
                    // Do nothing
                }
            }
            helpers.alert(text, "danger", 5000, $(".upload-data-errors"));
        }
    })


}