

$(function(){

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
                rows[row]['edit'] = getEditBtn(rows[row]['apiKey']);
                rows[row]['delete'] = getDeleteBtn(rows[row]['apiKey']);
            }
            helpers.addTableData($("#list-of-projects"), data["objects"], ["name", "apiKey", "refresh-key", "edit", "delete"],false);
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });
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
function getEditBtn(key){
    let editBtn = $("<button class='btn btn-sm btn-primary'><i class='fa fa-edit'></i></button>");
    editBtn.click(function(){
        // TODO: open modal
    });
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
            loadProjects();
        },
        error: function (response) {
            helpers.alert("Something went wrong", "danger", 5000);
        }
    });
}