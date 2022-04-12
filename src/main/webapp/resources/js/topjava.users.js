const userAjaxUrl = "admin/users/";
const userRestUrl = "rest/admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    let datatable = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    })
    makeEditable(datatable);
    $(".enable").click(function () {
        let id = $(this).closest('tr').attr("id");
        let enabled = $(this).closest('input').is(":checked");
        $.ajax({
            type: "PATCH",
            url: userRestUrl + id + "/?enabled=" + enabled
        }).done(function () {
            updateTable();
            successNoty("Saved");
        });
    });
});