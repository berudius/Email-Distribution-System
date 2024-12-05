package com.InternShip.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.InternShip.Models.Non_Saved_In_DB.UserLogStats;
import com.InternShip.Services.LogService;

@Controller
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping("/log-stats")
    public String getLogs(Model model, @RequestParam(defaultValue = "0") int page){
        Page<UserLogStats> logs = logService.getUserLogStats(page);
        model.addAttribute("logs", logs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logs.getTotalPages());
        return "logs";
    }

}
