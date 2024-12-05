"user strict"
import {addUserOnUI} from './UserCrudOperationsHandler.js';

function setSearchInputEventHandlers(){
    const searchInput =  document.getElementById("search-input");
    searchInput.addEventListener("keydown", event =>{
        if(event.key == "Enter"){
            doUserSearch()
        }
    });
}



function doUserSearch(){
    const searchInput =  document.getElementById("search-input");


    let words = searchInput.value.trim().replace("  ", " ").split(" ").filter(Boolean);
    let name = "";
    let email = "";
    switch(words.length){
        case 1:
            name = words[0];
            email = words[0];
            break;
        case 2:
            name = words[0];
            email = words[1];
            break;
    }
    
    let formData = new FormData();
    formData.append("name", name);
    formData.append("email", email);

    fetch("/user-list/search-user", {method: "POST", body: formData})
    .then(responce => responce.json())
    .then(data => {

        const usersContainer = document.getElementById("users-container");
        usersContainer.classList.add("hidden");
        const usersSearchContainer = document.getElementById("users-search-container");
        if(data.type == "User"){
            removeChilds(usersSearchContainer);
            data.users.content.forEach(user => {
                addUserOnUI(user, usersSearchContainer);
            });
        }
        else if(data.type == "message"){
            removeChilds(usersSearchContainer);
            let spanMessage = document.createElement("span");
            // spanMessage.classList.add("");
            spanMessage.textContent = data.message;
            usersSearchContainer.appendChild(spanMessage);
        }
        
    });
}

function removeChilds(usersSearchContainer){
    usersSearchContainer.innerHTML = "";
}






window.doUserSearch = doUserSearch;