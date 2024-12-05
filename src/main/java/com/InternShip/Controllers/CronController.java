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

import com.InternShip.Models.Cron;
import com.InternShip.Services.CronService;

@Controller
public class CronController {

    @Autowired
    CronService cronService;

    @GetMapping("/cron-list")
    public String getCronList(Model model, @RequestParam(defaultValue = "0") int page){
        Page<Cron> crons = cronService.getAllCrons(page);

        model.addAttribute("crons", crons);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPageCapacity", cronService.getMaxPageCapacity());
        model.addAttribute("totalPages", crons.getTotalPages());
        
        return "cron-list";
    }

    @PostMapping("/cron-list/add-cron")
    @ResponseBody
    public Map<String, Object> handleAddingCron(@RequestParam String expression, @RequestParam(defaultValue = "0") int page){
        return cronService.addCron(expression, page);
    }

    @PostMapping("/cron-list/remove-cron/{id}/{page}")
    @ResponseBody
    public Map<String, Object> handleRemovingCron(@PathVariable(value = "id") long id, @PathVariable(value = "page")  int currentPage){
        return cronService.removeCron(id, currentPage);
    }


    @PostMapping("/cron-list/edit-cron")
    @ResponseBody
    public Map<String, Object> handleEditingCron(@RequestParam long id, @RequestParam String expression){
        return cronService.editCron(id, expression);
    }


}
