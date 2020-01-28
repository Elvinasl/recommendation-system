

$(function(){
    loadRecommendations();
});

function loadRecommendations(){

    // console.log(navigator.parameterManager.all());
    // console.log(navigator.parameterManager.get("api-key"));
    // console.log("--------");



    helpers.ajax({
        method: "GET",
        url: "/recommendation/user/1",
        "api-key": navigator.parameterManager.get("api-key"),
        data: {
            "amount": 10
        },
        success: function (data) {
            console.log(data);
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