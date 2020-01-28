

$(function(){
    navigator.parameterManager.remove("api-key");
    loadProjects();
});
function loadProjects(){

    helpers.clearTableData($("#list-of-projects"));
    helpers.ajax({
        method: "GET",
        url: "/projects",
        success: function (data) {

            let rows = data["objects"];

            for(let row in rows){
                if(!rows.hasOwnProperty(row)) continue;
                rows[row]['refresh-key'] = getRefreshKeyBtn(rows[row]['apiKey']);
                rows[row]['show'] = getShowRowsBtn(rows[row]['apiKey']);
                rows[row]['recommendations'] = getRecommendationsBtn(rows[row]['apiKey']);
                rows[row]['edit'] = getEditBtn(rows[row]['name'], rows[row]['apiKey']);
                rows[row]['delete'] = getDeleteBtn(rows[row]['apiKey']);
            }
            helpers.addTableData($("#list-of-projects"), data["objects"], ["name", "apiKey", "refresh-key", "show", "recommendations", "edit", "delete"],true);
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });

    $("#add-project-btn").click(addProject);
}
function getRefreshKeyBtn(key){
    let editBtn = $("<button class='btn btn-sm btn-primary'><i class='fa fa-redo'></i></button>");
    editBtn.click(function(){
        if(confirm("Are you sure you want to refresh the api key for this project?")){
            refreshKeyForProject($(this).data('key'))
        }
    });
    editBtn.data('key', key);
    return editBtn;
}
function getShowRowsBtn(key){
    let btn = $("<button class='btn btn-sm btn-primary'><i class='fa fa-eye'></i></button>");
    btn.click(function(){
        navigator.parameterManager.set("api-key", $(this).data('key'));
        navigator.load('show-project');
    });
    btn.data('key', key);
    return btn;
}
function getRecommendationsBtn(key){
    let btn = $("<button class='btn btn-sm btn-primary'><i class='fa fa-list-ol'></i></button>");
    btn.click(function(){
        navigator.parameterManager.set("api-key", $(this).data('key'));
        navigator.load('recommendations');
    });
    btn.data('key', key);
    return btn;
}
function getEditBtn(name, key){
    let editBtn = $("<button class='btn btn-sm btn-primary'><i class='fa fa-edit'></i></button>");
    editBtn.click(function(){
        editProject($(this).data('name'), $(this).data('key'));
    });
    editBtn.data('name', name);
    editBtn.data('key', key);
    return editBtn;
}
function getDeleteBtn(key){
    let removeBtn = $("<button class='btn btn-sm btn-danger'><i class='fa fa-trash-alt'></button>");
    removeBtn.click(function(){
        if(confirm("Are you sure you want to delete this project?")){
            deleteProject($(this).data('key'));
        }
    });
    removeBtn.data('key', key);
    return removeBtn;
}
function deleteProject(key){

    helpers.ajax({
        method: "DELETE",
        url: "/projects/" + key,
        success: function (data) {
            if(typeof data["message"] !== "undefined"){
                helpers.alert(data["message"], "success", 3000);
            }
            loadProjects();
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });
}
function refreshKeyForProject(key){

    helpers.ajax({
        method: "PATCH",
        url: "/projects/" + key + "/refresh-key",
        success: function (data) {
            helpers.alert("Project api key changed from '" + key + "' to '" + data["apiKey"] + "'", "success", 5000);
            loadProjects();
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });
}

function addProject(){
    let projectModal = $("#project-modal");
    projectModal.find(".modal-title").html("Add project");
    projectModal.find("#project-name-input").val("");
    projectModal.find(".modal-save-btn").off("click");
    projectModal.find(".modal-save-btn").on("click", function(e){
        helpers.ajax({
            method: "POST",
            url: "/projects",
            data: { name: projectModal.find("#project-name-input").val() },
            success: function (data) {
                projectModal.modal("hide");
                helpers.alert("Created project '" + data["name"] + "' with key '" + data["apiKey"] + "'", "success", 5000);
                loadProjects();
            },
            error: function (response) {
                helpers.alert("Something went wrong", "danger", 3000, projectModal.find(".modal-errors"));
            }
        });
    });
    projectModal.modal("show");
}
function editProject(name, key){
    let projectModal = $("#project-modal");
    projectModal.find(".modal-title").html("Edit project");
    projectModal.find("#project-name-input").val(name);
    projectModal.find(".modal-save-btn").off("click");
    projectModal.find(".modal-save-btn").on("click", function(e){
        helpers.ajax({
            method: "PATCH",
            url: "/projects/"+key,
            data: { name: projectModal.find("#project-name-input").val() },
            success: function (data) {
                projectModal.modal("hide");
                helpers.alert("Changed project name '" + name + "' to '" + data["name"] + "'", "success");
                loadProjects();
            },
            error: function (response) {
                helpers.alert("Something went wrong", "danger", 5000, projectModal.find(".modal-errors"));
            }
        });
    });
    projectModal.modal("show");
}