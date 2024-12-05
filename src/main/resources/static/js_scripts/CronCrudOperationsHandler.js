"use strict"

 
function handleAddFormSubmit(event) {
        event.preventDefault();  
        
        let addingForm = event.target;
        trimExpressionInput(addingForm);
       
        let action = addingForm.getAttribute("action");    
        let formData = new FormData(addingForm);         
        formData.append("page", getCurrentPage());      
       
        fetch(action, {
            method: 'POST',
            body: formData 
        })
        .then(response => response.json())
        .then(data => {                                        
            
            if(data.type == "Cron") {
                let isFullyFilled = isPageFullyFilledByCrons();
                let isOnFirstPage = isCurrentlyOnFirstPage();
                const cronsContainer = document.getElementById("crons-container")
                if( ! isFullyFilled && isOnFirstPage){
                    addCronOnUI(data.cron,  cronsContainer, true);
                }
                else if( data.firstCronOnPage && ! isOnFirstPage && ! isFullyFilled ){
                    addCronOnUI(data.firstCronOnPage,  cronsContainer, true);
                } 
                else if(isFullyFilled){
                    removeLastCronCard(cronsContainer);
                    addCronOnUI(data.cron,  cronsContainer, true);
                    makeButton_Next_Active();    
                }

                setFormErrorSpanByText(addingForm, "");
                setInputOfCronFormEmpty(addingForm);
                sendSuccesMesage(data.cron.expression);
            }

            else if(data.type == "error"){
                setFormErrorSpanByText(addingForm, data.errorMessage);
            }
        })
        .catch(error => console.log("Error:", error));
}

function addCronOnUI(cron, cronsContainer, setAsFirstChild){
    const cronRow = document.createElement("div");
    cronRow.classList.add("cron-container");

    cronRow.innerHTML = `
        <h3 data-expression="${cron.expression}"
            class="cron-expression">Вираз: ${cron.expression}</h3>
        <span class="createdOn">Дата створення: ${cron.formattedCreatedOn}</span>
        <div class="buttons-container">
            <button class="cron-edit-form-opener-button"
                    data-cron-id = "${cron.id}"
                    onclick="openEditForm(event)">Редагувати</button>
            <button class="cron-remove-button"
                    href-action='/cron-list/remove-cron/'${cron.id}"
                    onclick="removeCron(event)">Видалити</button>
        </div>
    `;


    

   if(setAsFirstChild == true){ cronsContainer.prepend(cronRow); }
                        else{ cronsContainer.append(cronRow); }
   
}



function isPageFullyFilledByCrons(){
    let cronsContainer = document.getElementById("crons-container");
    let maxPageCapacity = cronsContainer.dataset.maxPageCapacity;
    let currentPageCapacity = cronsContainer.childElementCount;

    return currentPageCapacity < maxPageCapacity ? false : true;
}

function trimExpressionInput(form){
    let str = "";
    let inputExpression = form.querySelector("input[name='expression']");
    inputExpression.value = inputExpression.value.trim().replace(/\s+/g, " ");
    let i = 0;
 }



function isCurrentlyOnFirstPage(){
    let currentPage = getCurrentPage();
    
    let result = currentPage == 0 ? true : false
    return result;
}



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




 function sendSuccesMesage(cronExpression){
    let messageBlock = document.createElement("div");
    messageBlock.classList.add("message-block");
    messageBlock.textContent = `Cron ${cronExpression} додано`;
    const main = document.querySelector("main");
    main.appendChild(messageBlock);
    setTimeout(()=>{
        messageBlock.remove();
    }, 3000);
}


function removeLastCronCard(cronsContainer){
    let lastElementChild =  cronsContainer.lastChild.previousElementSibling;
    lastElementChild.remove();
}












function removeCron(event){

    let removeButton = event.target;
    let url = removeButton.getAttribute("href-action");
    url += "/" + getCurrentPage();


    fetch(url, {method: "POST"})
    .then(response=>response.json())
    .then(data => {
       let cronContainer = event.target.closest(".cron-container");
       cronContainer.remove();
       if(data.firstCronFromNextPage){ addCronOnUI(data.firstCronFromNextPage, document.getElementById("crons-container"), false); }
    });
}











function openAddForm(){
    const addForm = document.getElementById("cron-adding-form");
    const addOverlay = document.getElementById("add-overlay");
    switchForms(addForm, addOverlay);// закриває editForm та його overlay та відображає addForm та його overlay
 }
 



function openEditForm(event){
   const editForm = document.getElementById("cron-editing-form");
   const editOverlay = document.getElementById("edit-overlay");

   const expressionInput = document.getElementById("cron-expression-input-edit");

   const editButton = event.target;
   const cronContainer = editButton.closest(".cron-container");
   const h3Expression = cronContainer.querySelector(".cron-expression");
   
   const cronExpression = h3Expression.dataset.expression;

   let cronId = editButton.dataset.cronId;
   editForm.setAttribute("data-cron-id", cronId);
   
   expressionInput.value = cronExpression;

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

        case "cron-adding-form":
           const editForm = document.getElementById("cron-editing-form");
           const editOverlay = editForm.nextElementSibling;
           editForm.classList.remove("show");
           editOverlay.classList.remove("show");
        break;

        case "cron-editing-form":
            const addForm = document.getElementById("cron-adding-form");
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

    const editForm = document.getElementById("cron-editing-form");
    trimExpressionInput(editForm);
    const cronId = editForm.dataset.cronId;
    let formData = new FormData(editForm);
    let url = editForm.getAttribute("action");
    formData.append("id", cronId);
    
    fetch(url, {
        method:"POST",
        body:formData
    })
    .then(response => response.json())
    .then(data => {
        if(data.type == "Cron"){
            updateCronsContainerByCron(data.cron.id, data);
            setFormErrorSpanByText(editForm, "");
            setInputOfCronFormEmpty(editForm);
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

function setInputOfCronFormEmpty(form){
    form.querySelector("input[name='expression']").value = "";
}

 function updateCronsContainerByCron(cronId, data){
   const enteredEditButton = document.querySelector(`.cron-edit-form-opener-button[data-cron-id="${cronId}"]`);
   const appropriateCronContainer = enteredEditButton.closest(".cron-container");
   

   const h3CronExpression = appropriateCronContainer.querySelector(".cron-expression");
   const spanCreatedOn = appropriateCronContainer.querySelector(".createdOn");

   
   h3CronExpression.textContent = "Вираз " + data.cron.expression;
   spanCreatedOn.textContent = "Дата створення: " + data.cron.formattedCreatedOn;
   h3CronExpression.dataset.expression = `${data.cron.expression}`;
}




















