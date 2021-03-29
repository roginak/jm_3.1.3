const url = 'http://localhost:8080/api';

function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}
const current_user_id = getCookie('currentUserId');
const current_user_login = getCookie('currentUserLogin');



const renderHeaderWithUserTable = function () {
    fetch(url+'/users/getLoginAndRolesString/'+current_user_id).then(function(response) {
        response.json().then(function(text) {
            $('#current_user_string').text(text[0] + ' with roles: ' + text[1]);
            renderUserTable();
        });
    });
}

const renderUserTable = function () {
    fetch(url+'/users/'+current_user_id).then(function(response) {
        response.json().then(function(text) {
            let temp = "";
            let string_roles = "";
            temp += "<tr>";
            temp += "<td>" + text.id + "</td>";
            temp += "<td>" + text.name + "</td>";
            temp += "<td>" + text.surname + "</td>";
            temp += "<td>" + text.position + "</td>";
            temp += "<td>" + text.salary + "</td>";
            temp += "<td>" + text.contact + "</td>";
            temp += "<td>" + text.login + "</td>";
            text.roles.forEach(role=>{
                string_roles += role.roleName + " ";
            })
            temp += "<td>" + string_roles + "</td></tr>";
            $('#user-table-content').html(temp);
        });
    });
}

const renderRolesInNewUserForm = function () {
    fetch(url+'/roles/').then(function(response) {
        response.json().then(function(allroles) {
            allroles.forEach(role => {
                $('#new_roles').append('<option value='+role.roleName+'>'+role.roleName+'</option>');
            });
        });
    });
}

const renderAllUsersTable = function () {
    fetch(url+'/users').then(function(response) {
        response.json().then(function(text) {
            let string_roles = "";
            if(text.length > 0) {
                let temp = "";
                text.forEach(el=>{
                    string_roles = "";
                    temp += "<tr>";
                    temp += "<td>" + el.id + "</td>";
                    temp += "<td>" + el.name + "</td>";
                    temp += "<td>" + el.surname + "</td>";
                    temp += "<td>" + el.position + "</td>";
                    temp += "<td>" + el.salary + "</td>";
                    temp += "<td>" + el.contact + "</td>";
                    temp += "<td>" + el.login + "</td>";
                    el.roles.forEach(role=>{
                        string_roles += role.roleName + " ";
                    })
                    temp += "<td>" + string_roles + "</td>";
                    temp += "<td><button type='button' class='btn btn-info text-light' data-bs-toggle='modal' data-bs-target='#editModal' data-user-id='"+ el.id+ "'  data-bs-whatever='@edit'>Edit</button></td>";
                    temp += "<td><button type='button' class='btn btn-danger' data-bs-toggle='modal' data-bs-target='#deleteModal' data-user-id='"+ el.id+ "' data-bs-whatever='@delete'>Delete</button></td></tr>";
                });
                $('#table-content').html(temp);
            }
        });
    });
}

const renderAllUsersTableWithModals = function () {
    fetch(url+'/users').then(function(response) {
        response.json().then(function(text) {
            let string_roles = "";
            if(text.length > 0) {
                let temp = "";
                text.forEach(el=>{
                    string_roles = "";
                    temp += "<tr>";
                    temp += "<td>" + el.id + "</td>";
                    temp += "<td>" + el.name + "</td>";
                    temp += "<td>" + el.surname + "</td>";
                    temp += "<td>" + el.position + "</td>";
                    temp += "<td>" + el.salary + "</td>";
                    temp += "<td>" + el.contact + "</td>";
                    temp += "<td>" + el.login + "</td>";
                    el.roles.forEach(role=>{
                        string_roles += role.roleName + " ";
                    })
                    temp += "<td>" + string_roles + "</td>";
                    temp += "<td><button type='button' class='btn btn-info text-light' data-bs-toggle='modal' data-bs-target='#editModal' data-user-id='"+ el.id+ "'  data-bs-whatever='@edit'>Edit</button></td>";
                    temp += "<td><button type='button' class='btn btn-danger' data-bs-toggle='modal' data-bs-target='#deleteModal' data-user-id='"+ el.id+ "' data-bs-whatever='@delete'>Delete</button></td></tr>";
                });
                $('#table-content').html(temp);

                $('#deleteModal, #editModal').on('show.bs.modal', function (event) {
                    $('.form-control').val('');
                    $('#roles').find('option').remove().end();
                    $('#delete_roles').find('option').remove().end();

                    var button = $(event.relatedTarget)
                    var user_id = button.data('user-id')
                    fetch(url+'/users/'+user_id).then(function(response) {
                        response.json().then(function(user) {
                            $('input[id="id"]').val(user_id);
                            $('input[id="name"]').val(user.name);
                            $('input[id="surname"]').val(user.surname);
                            $('input[id="position"]').val(user.position);
                            $('input[id="salary"]').val(user.salary);
                            $('input[id="contact"]').val(user.contact);
                            $('input[id="login"]').val(user.login);

                            if(button.attr('data-bs-target') === '#deleteModal') {
                                user.roles.forEach(role => {
                                    $('#delete_roles').append('<option value='+role.roleName+'>'+role.roleName+'</option>');
                                })
                            } else {
                                fetch(url+'/roles/').then(function(response) {
                                    response.json().then(function(allroles) {
                                        allroles.forEach(role => {
                                            $('#roles').append('<option value='+role.roleName+'>'+role.roleName+'</option>');
                                        });
                                    });
                                });
                            }
                        });
                    });
                });
            }
        });
    });
}

function getListRolesFromForm(formRoles) {
    return Array.from(formRoles)
        .filter(option => option.selected)
        .map(option => option.value)
}

function updateUser(form, action) {
    if(action === 'POST') {

        let user = {
            login: form.login.value,
            name: form.name.value,
            surname: form.surname.value,
            contact: form.contact.value,
            salary: form.salary.value,
            position: form.position.value,
            password: form.password.value,
            listRoles: getListRolesFromForm(form.roles)
        };


        fetch(url + '/users', {method: 'POST', headers: {'Content-Type': 'application/json;charset=utf-8'},
            body: JSON.stringify(user)}).then(function (response) {
                response.text().then(function(text) {
                    if(response.status === 200) {
                        $('#table-content').remove('tr');
                        renderAllUsersTable();
                        $('#create_form')[0].reset();
                        $('a[href="#allUsers"]').tab('show');
                    } else {
                        alert(response.status + " " + response.statusText);
                    }
                })
        });
    } else if(action === 'PATCH') {

        let user_id = form.id.value;
        let user = {
            id: user_id,
            login: form.login.value,
            name: form.name.value,
            surname: form.surname.value,
            contact: form.contact.value,
            salary: form.salary.value,
            position: form.position.value,
            password: form.password.value,
            listRoles: getListRolesFromForm(form.roles)
        };

        fetch(url + '/users/' + user_id, {method: 'PATCH', headers: {'Content-Type': 'application/json;charset=utf-8'},
            body: JSON.stringify(user)}).then(function (response) {
            response.text().then(function(text) {
                if(response.status === 200) {
                    if(user_id === current_user_id ) {
                        if(user.login !== current_user_login) {
                            window.location.replace('/logout');
                        } else {
                            $('#current_user_string').text('');
                            $('#user-table-content').remove('tr');
                            renderHeaderWithUserTable();
                        }

                    }
                    $('#table-content').remove('tr');
                    renderAllUsersTable();
                    $('#editModal').modal('hide');
                } else {
                    alert(response.status + " " + response.statusText);
                }
            })
        });
    } else if(action === 'DELETE') {
        let user_id = form.id.value;
        fetch(url + '/users/' + user_id, {method: 'DELETE', headers: {'Content-Type': 'application/json;charset=utf-8'}})
            .then(function (response) {
            response.text().then(function(text) {
                if(response.status === 200) {
                    if(current_user_id === user_id) {
                        window.location.replace('/logout');
                    }
                    $('#table-content').remove('tr');
                    renderAllUsersTable();
                    $('#deleteModal').modal('hide');
                } else {
                    alert(response.status + " " + response.statusText);
                }
            })
        });
    }
}

renderHeaderWithUserTable();
renderRolesInNewUserForm();
renderAllUsersTableWithModals();









