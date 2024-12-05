"use strict"


//функція для визначення динаміної поведінки форми "Додати нового користувача"  
function handleAddFormSubmit(event) {
        event.preventDefault();  
        
        let addingForm = event.target;
        trimNameInput(addingForm);
        //для передачі даних форми без оновлення сторінки
        let action = addingForm.getAttribute("action");    //посилання яке охоплююється by UserListController.java
        let formData = new FormData(addingForm);          //дані форми
        formData.append("page", getCurrentPage());       //додатково передамо значення поточної сторінки користувачів

        //передача даних в UserListController.java за допомогою вбудованого методу fetch та очікування на відповідь (response та data)
        fetch(action, {
            method: 'POST',
            body: formData 
        })
        .then(response => response.json())
        .then(data => {                                 //після отримання відповіді обробляємо дані відповідно до типу переданих даних із UserListController.java           
            
            if(data.type == "User") {
                let isFullyFilled = isPageFullyFilledByUsers();
                let isOnFirstPage = isCurrentlyOnFirstPage();
                const usersContainer = document.getElementById("users-container")
                if( ! isFullyFilled && isOnFirstPage){
                    addUserOnUI(data.user,  usersContainer, true);
                }
                else if( data.firstUserOnPage && ! isOnFirstPage && ! isFullyFilled ){
                    addUserOnUI(data.firstUserOnPage,  usersContainer, true);
                } 
                else if(isFullyFilled){
                    removeLastUserCard(usersContainer);
                    addUserOnUI(data.user,  usersContainer, true);
                    makeButton_Next_Active();    
                }

                setFormErrorSpanByText(addingForm, "");
                setInputsOfUserFormEmpty(addingForm);
                sendSuccesMesage(data.user.name);
            }

            else if(data.type == "error"){
                setFormErrorSpanByText(addingForm, data.errorMessage);
            }
        })
        .catch(error => console.log("Error:", error));
}

export function addUserOnUI(user, usersContainer, setAsFirstChild){
    const userRow = document.createElement("div");
    userRow.classList.add("user-container");
    const updatedOn = user.formattedCreatedOn != user.formattedUpdatedOn ? `Дата оновлення: ${user.formattedUpdatedOn}` : "";
    userRow.innerHTML = `
        <h3 class='user-name' data-name='${user.name}'>Ім'я користувача: ${user.name}</h3>
        <span class='user-email' data-email='${user.email}'>Email користувача: ${user.email}</span>
        <span class='createdOn' >Дата створення: ${user.formattedCreatedOn}</span>
        <span class='updatedOn' >${updatedOn}</span>
        <div class="buttons-container">
            <button class="user-edit-form-opener-button"
                    data-user-id = "${user.id}"
                    onclick="openEditForm(event)">Редагувати</button>
            <button class="user-remove-button"
                    href-action="/user-list/remove-user/${user.id}"
                    onclick="openAttentionWindow(event)">Видалити</button>
            <button class="user-message-sender"
                    data-user-id = "${user.id}"
                    onclick="callMessageSending(event)">Надіслати лист</button>
        </div>
    `;


    

   if(setAsFirstChild == true){ usersContainer.prepend(userRow); }
                        else{ usersContainer.append(userRow); }
   
}



function isPageFullyFilledByUsers(){
    let usersContainer = document.getElementById("users-container");
    let maxPageCapacity = usersContainer.dataset.maxPageCapacity;
    let currentPageCapacity = usersContainer.childElementCount;

    return currentPageCapacity < maxPageCapacity ? false : true;
}

function isCurrentlyOnFirstPage(){
    let currentPage = getCurrentPage();
    
    let result = currentPage == 0 ? true : false
    return result;
}

// function isCurrentlyOnLastPage(totalPages){
//     let currentPage = getCurrentPage();
    
//     let result = currentPage == (totalPages - 2) ? true : false
//     return result;
// }

function makeButton_Next_Active(){
   let anchorNext = document.getElementById("Next");
   anchorNext.classList.remove("disabled");
}

function getCurrentPage(){
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get("page") || 0;
    return currentPage;
}

function setFormErrorSpanByText(form, text){
    const errorSpan = form.querySelector(".error-message");
    errorSpan.textContent = text;
}


function trimNameInput(form){
   let inputName = form.querySelector("input[name='name']");
   inputName.value = inputName.value.trim();
}

 function sendSuccesMesage(userName){
    let messageBlock = document.createElement("div");
    messageBlock.classList.add("message-block");
    messageBlock.textContent = `Користувача ${userName} додано`;
    const main = document.querySelector("main");
    main.appendChild(messageBlock);
    setTimeout(()=>{
        messageBlock.remove();
    }, 3000);
}


function removeLastUserCard(usersContainer){
    let lastElementChild =  usersContainer.lastChild.previousElementSibling;
    lastElementChild.remove();
}














function openAttentionWindow(event){
   const enteredRemoveButton = event.target;
   const userName = enteredRemoveButton.closest(".user-container").firstElementChild.textContent.replace("Ім'я користувача: ", "");

   const attentionWindow = document.getElementById("attention-window");
   const message = attentionWindow.querySelector("#message");
   const cancelButton = attentionWindow.querySelector("#cancel-button");
   const removeButton = attentionWindow.querySelector("#remove-button");

   let overlay = document.createElement("div");
   overlay.classList.add("overlay");
   overlay.classList.add("show");
   document.querySelector('main').appendChild(overlay);
   message.innerHTML = "Видалення користувача призведе до видалення <b>статистики</b> про нього! Ви впевнені, що хочете видалити користувача <b>" + userName + "</b>"; 
   attentionWindow.classList.add("show")

   overlay.addEventListener("click", e=>{
    attentionWindow.classList.remove("show");
    overlay.remove()
    message.innerHTML = "";
   });
   cancelButton.addEventListener("click", e => {
    attentionWindow.classList.remove("show");
    overlay.remove();
    message.innerHTML = "";
   });

   removeButton.addEventListener("click", e => {
    removeUser(enteredRemoveButton);
    attentionWindow.classList.remove("show");
    overlay.remove();
    message.innerHTML = "";
   });

   
}


function removeUser(button){

    let removeButton = button;
    let url = removeButton.getAttribute("href-action");
    url += "/" + getCurrentPage();


    fetch(url, {method: "POST"})
    .then(response=>response.json())
    .then(data => {
       let userContainer = removeButton.closest(".user-container");
       userContainer.remove();
       if(data.firstUserFromNextPage){ addUserOnUI(data.firstUserFromNextPage, document.getElementById("users-container"), false); }
    });
}











function openAddForm(){
    const addForm = document.getElementById("user-adding-form");
    const addOverlay = document.getElementById("add-overlay");
    switchForms(addForm, addOverlay);// закриває editForm та його overlay та відображає addForm та його overlay
 }
 



function openEditForm(event){
   const editForm = document.getElementById("user-editing-form");
   const editOverlay = document.getElementById("edit-overlay");

   const nameInput = document.getElementById("user-name-input-edit");
   const emailInput = document.getElementById("user-email-input-edit");

   const editButton = event.target;
   const userContainer = editButton.closest(".user-container");
   const h3UserName = userContainer.querySelector(".user-name");
   const spanUserEmail = userContainer.querySelector(".user-email");
   
   const userName = h3UserName.dataset.name;
   const userEmail = spanUserEmail.dataset.email;

   


   let userId = editButton.dataset.userId;
   editForm.setAttribute("data-user-id", userId);
   
   nameInput.value = userName;
   emailInput.value = userEmail;

   switchForms(editForm, editOverlay);// закриває addForm та його overlay та відображає editForm та його overlay
}

function closeForm(event){
    const overlay = event.target;
    const form = overlay.previousElementSibling;
    const nameInput = form.querySelector("input[name='name']");
    const emailInput = form.querySelector("input[name='email']");
    const errorSpan = form.querySelector(".error-message");

    form.classList.remove("show");
    overlay.classList.remove("show");
    nameInput.value = "";
    emailInput.value = "";              
    errorSpan.textContent = "";
}



function switchForms(form, overlay){

    switch(form.getAttribute("id")){

        case "user-adding-form":
           const editForm = document.getElementById("user-editing-form");
           const editOverlay = editForm.nextElementSibling;
           editForm.classList.remove("show");
           editOverlay.classList.remove("show");
        break;

        case "user-editing-form":
            const addForm = document.getElementById("user-adding-form");
            const addOverlay = addForm.nextElementSibling;
            addForm.classList.remove("show");
            addOverlay.classList.remove("show");
        break;
    }

    form.classList.add("show");
    overlay.classList.add("show");
}





function handleEditFormSubmit(event){
    event.preventDefault();

    const editForm = document.getElementById("user-editing-form");
    trimNameInput(editForm);
    const userId = editForm.dataset.userId;
    let formData = new FormData(editForm);
    let url = editForm.getAttribute("action");
    formData.append("id", userId);
    
    fetch(url, {
        method:"POST",
        body:formData
    })
    .then(response => response.json())
    .then(data => {
        if(data.type == "User"){
            updateUserContainerByUser(data.user.id, data);
            setFormErrorSpanByText(editForm, "");
            setInputsOfUserFormEmpty(editForm);
            hideForm(editForm);
        }
        else if(data.type == "error"){
            setFormErrorSpanByText(editForm, data.errorMessage);
        }
    });
}

function hideForm(form){
    const overlay = form.nextElementSibling;
    form.classList.remove("show");
    overlay.classList.remove("show");
}

function setInputsOfUserFormEmpty(form){
    form.querySelector("input[name='name']").value = "";
    form.querySelector("input[name='email']").value = "";
}

 function updateUserContainerByUser(userId, data){
   const enteredEditButton = document.querySelector(`.user-edit-form-opener-button[data-user-id="${userId}"]`);
   const appropriateUserContainer = enteredEditButton.closest(".user-container");
   

   const h3UserName = appropriateUserContainer.querySelector(".user-name");
   const spanUserEmail = appropriateUserContainer.querySelector(".user-email");
   const spanUpdatedOn = appropriateUserContainer.querySelector(".updatedOn");

   
   h3UserName.textContent = "Ім'я користувача: " + data.user.name;
   spanUserEmail.textContent = "Email користувача: " + data.user.email;
   spanUpdatedOn.textContent = "Дата оновлення: " + data.user.formattedUpdatedOn;
   h3UserName.dataset.name = `${data.user.name}`;
   spanUserEmail.dataset.email = `${data.user.email}`;
}



window.openEditForm = openEditForm;
window.removeUser = removeUser;
window.openAddForm = openAddForm;
window.handleAddFormSubmit = handleAddFormSubmit;
window.handleEditFormSubmit = handleEditFormSubmit;
window.closeForm = closeForm;
window.openAttentionWindow = openAttentionWindow;


















