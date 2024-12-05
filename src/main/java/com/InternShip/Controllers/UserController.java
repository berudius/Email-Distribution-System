package com.InternShip.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.InternShip.Models.User;
import com.InternShip.Services.UserService;

@Controller
public class UserController {
    @Autowired
    UserService userService;



    @GetMapping("/user-list")
    public String getUserList(Model model, @RequestParam(defaultValue = "0") int page){
        Page<User> users = userService.getUsersByPage(page);
        model.addAttribute("users", users);
        model.addAttribute("maxPageCapacity", userService.getMaxPageCapacity());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());

        return "user-list";
    }



    @PostMapping("/user-list/add-user")
    @ResponseBody
    public Map<String, Object> handleAddingUser(@RequestParam String name, @RequestParam String email, @RequestParam int page){
        return userService.addUser(name, email, page);
    }

    @PostMapping("/user-list/remove-user/{id}/{page}")
    @ResponseBody
    public Map<String, Object> handleRemovingUser(@PathVariable(value = "id") long id, @PathVariable(value = "page")  int currentPage){
        return userService.removeUser(id, currentPage);
    }

    @PostMapping("/user-list/edit-user")
    @ResponseBody
    public Map<String, Object> handleEditingUser(@RequestParam long id, @RequestParam String name, @RequestParam String email){
        return userService.editUser(id, name, email);
    }


    @PostMapping("/user-list/search-user")
    @ResponseBody
    public Map<String, Object> findUsersByNameOrEmail(@RequestParam(defaultValue = "") String name,
                                                      @RequestParam(defaultValue = "") String email,
                                                      @RequestParam(defaultValue = "0") int page){

        return userService.processSearch(name, email, page);
    }

    @PostMapping("/user-list/send-message")
    @ResponseBody
    public Map<String, String> sendMessageForUser(@RequestParam (defaultValue = "0") long id){
       return userService.processMessageSending(id);
    }
    

    

}
