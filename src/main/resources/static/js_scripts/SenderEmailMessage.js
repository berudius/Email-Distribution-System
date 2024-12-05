"use strict"

function sendUnsuccesMessage(userName, userEmail){
    let messageBlock = document.createElement("div");
    messageBlock.classList.add("message-block");
    messageBlock.textContent = `Лист надіслано користувачу ${userName} на gmail ${userEmail}`;
    const main = document.querySelector("main");
    main.appendChild(messageBlock);
    setTimeout(()=>{
        messageBlock.remove();
    }, 3000);
}

function sendUnsuccesMessage(){
    let messageBlock = document.createElement("div");
    messageBlock.classList.add("message-block");
    messageBlock.textContent = `Помилка в надсиланні`;
    const main = document.querySelector("main");
    main.appendChild(messageBlock);
    setTimeout(()=>{
        messageBlock.remove();
    }, 3000);
}


function callMessageSending(event){
    const clickedButton = event.target;
    const userId = clickedButton.dataset.userId;
    let formData = new FormData();
    formData.append("id", userId);

    showOverlayAndLoadingAnimation();

    fetch("/user-list/send-message", {method: "POST", body: formData})
    .then(responce => responce.json())
    .then(data =>{
        hideOverlayAndLoadingAnimation();
        if(Boolean(data.isMessageSended)){
            sendUnsuccesMessage(data.userName, data.userEmail);
        }
        else{
            sendUnsuccesMessage();
        }
        
    })
    .finally(()=>{
        hideOverlayAndLoadingAnimation();
    });
}


function showOverlayAndLoadingAnimation(){
    let overlay = document.querySelector(".overlay");
    let spinner = document.getElementById("spinner");
    overlay.classList.add("show");
    spinner.classList.add("show");
}

function hideOverlayAndLoadingAnimation(){
    let overlay = document.querySelector(".overlay");
    let spinner = document.getElementById("spinner");
    overlay.classList.remove("show");
    spinner.classList.remove("show");
}