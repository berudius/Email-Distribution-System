package com.InternShip.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.InternShip.Models.Log;
import com.InternShip.Models.User;
import com.InternShip.Models.Non_Saved_In_DB.LogType;
import com.InternShip.Repository.UserRepository;
import com.InternShip.Validators.UserValidator;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    LogService logService;

    private final int maxPageCapacity = 10;


    public Page<User> getUsersByPage(int page){
      PageRequest pageRequest = PageRequest.of(page, maxPageCapacity, Sort.by("createdOn").descending());
        Page<User> user = userRepository.findAll(pageRequest);
        return user;
    }

    public Map<String, Object> addUser(String name, String email, int page){
      Map<String, Object> responce = new HashMap<>();
        StringBuilder errorMessage = new StringBuilder("");

        if(UserValidator.isEmailValidToAdd(this, email, errorMessage)){
            User user = User.builder()
                        .name(name)
                        .email(email)
                        .build();
            saveUser(user);
            user = getUserById(user.getId());

           
            Page<User> users = getUsersByPage(page);
            User firstUserOnPage =  users.getContent().getFirst();
            responce.put("type", "User");
            responce.put("user", user);
            responce.put("firstUserOnPage", firstUserOnPage);
            responce.put("totalPages", users.getTotalPages());
            
            return responce;
        }


        responce.put("type", "error");
        responce.put("errorMessage", errorMessage.toString());
        return responce;
    }


    public User saveUser(User user){
      userRepository.save(user);
      User savedUser = userRepository.findById(user.getId()).get();
      return savedUser;
    }


    public Map<String, Object> removeUser(long id, int currentPage){
      User firstUserFromNextPage = removeUserAndGetFirstFromNextPage(id, currentPage + 1);
      Map<String, Object> responce = new HashMap<>();
      responce.put("type", "User");
      responce.put("firstUserFromNextPage", firstUserFromNextPage);
      return responce;
    }

    public User removeUserAndGetFirstFromNextPage(long userId, int page){
      User firstUserFromNextPage = getFirstUserFromPage(page);
      userRepository.deleteById(userId);
      return firstUserFromNextPage;
  }

  public User getFirstUserFromPage(int page){
    PageRequest pageRequest = PageRequest.of(page, maxPageCapacity, Sort.by("createdOn").descending());
    Page<User> usersPage = userRepository.findAll(pageRequest);
    return usersPage.hasContent() ? usersPage.getContent().get(0) : null;
  }

  public Map<String, Object> editUser(long id, String name, String email){
    StringBuilder errorMessage = new StringBuilder("");
    Map<String, Object> responce = new HashMap<>();

    if(UserValidator.isEmailValidToEdit(this, email, id, errorMessage)){
      User user = getUserById(id);
      user.setName(name);
      user.setEmail(email);
      user = saveUser(user);
      responce.put("type", "User");
      responce.put("user", user);

      return responce;
    } 

    responce.put("type", "error");
    responce.put("errorMessage", errorMessage.toString());

    return responce;
  }



  

    public boolean isEmailUnique(String email){
       return ! userRepository.existsByEmail(email);
    }

    public boolean isEmailUniqueExludingId(String email, long id){
      return ! userRepository.existsByEmailAndIdNot(email, id);
   }

   public User getUserById(long id){
    return userRepository.findById(id).get();
   }





   public Map<String, Object> processSearch(String name, String email, int page){
    Page<User> users = getUsersByNameOrEmail(name, email, page);
    Map<String, Object> responce = new HashMap<>();

    if (users.getNumberOfElements() != 0 &&  ! name.isBlank() && ! email.isBlank()) {
        responce.put("type", "User");
        responce.put("users", users);
        return responce;
    }

    responce.put("type", "message");
    responce.put("message", "Пошук не дав результатів");
    
    return responce;
   }

   public Page<User> getUsersByNameOrEmail(String name, String email, int page){
    PageRequest request = PageRequest.of(page, maxPageCapacity, Sort.by("createdOn").descending());
  
    if(name.equals(email)){
      return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, email, request);
    }

   Page<User> users1 = userRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(name, email, request);
   Page<User> users2 = userRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(email, name, request);

    return users1.getNumberOfElements() > 0 ? users1 : users2;
      
    }




   public Map<String, String> processMessageSending(long id){
      Map<String, String> responce = new HashMap<>();

      User user = userRepository.findById(id).get();
      String messageText = "Ім'я користувача: " + user.getName() + "\nДата та час створення: " + user.getFormattedCreatedOn();            
      boolean isMessageSended = emailService.sendEmail(user.getEmail(), "Вітання", messageText);

      responce.put("isMessageSended", "false");
      if(isMessageSended){ 
        saveLog(user);
        responce.put("isMessageSended", "true");
      }

      responce.put("userName", user.getName());
      responce.put("userEmail", user.getEmail());

      return responce;
    }

    private void saveLog(User user){
      Log log = Log.builder().user(user).type(LogType.REST).build();
      logService.saveLog(log);
    }



   public int getMaxPageCapacity() {
       return maxPageCapacity;
   }





}
