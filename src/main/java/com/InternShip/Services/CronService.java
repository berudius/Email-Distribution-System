package com.InternShip.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.InternShip.Models.Cron;
import com.InternShip.Repository.CronRepository;
import com.InternShip.Validators.CronValidator;


@Service
public class CronService {
    @Autowired
    CronRepository cronRepository;
    private final int maxPageCapacity = 10;

    public Page<Cron> getAllCrons(int page){
        PageRequest request = PageRequest.of(page, maxPageCapacity, Sort.by("createdOn").descending());
        Page<Cron> crons = cronRepository.findAll(request);
        return crons;
    }

    public Map<String, Object> addCron(String expression, int page){
      Map<String, Object> responce = new HashMap<>();
    StringBuilder errorMessage = new StringBuilder("");

        if(CronValidator.isCronExpressionValid(expression, errorMessage)){
            Cron cron = Cron.builder()
                        .expression(expression)
                        .build();
            cron = saveCron(cron);
           
            Page<Cron> crons = getCronsByPage(page);
            Cron firstCronOnPage =  crons.getContent().getFirst();
            responce.put("type", "Cron");
            responce.put("cron", cron);
            responce.put("firstCronOnPage", firstCronOnPage);
            responce.put("totalPages", crons.getTotalPages());
            
            return responce;
        }


        responce.put("type", "error");
        responce.put("errorMessage", errorMessage.toString());
        return responce;
    }


    public Cron saveCron(Cron cron){
        return cronRepository.save(cron);
    }


    public Map<String, Object> removeCron(long id, int currentPage){
        Cron firstCronFromNextPage = removeCronAndGetFirstFromNextPage(id, currentPage + 1);
        Map<String, Object> responce = new HashMap<>();
        responce.put("type", "Cron");
        responce.put("firstCronFromNextPage", firstCronFromNextPage);
        return responce;
      }

      public Cron removeCronAndGetFirstFromNextPage(long cronId, int page){
        Cron firstCronFromNextPage = getFirstCronFromPage(page);
        cronRepository.deleteById(cronId);
        return firstCronFromNextPage;
    }

    public Cron getFirstCronFromPage(int page){
        PageRequest pageRequest = PageRequest.of(page, maxPageCapacity, Sort.by("createdOn").descending());
        Page<Cron> cronsPage = cronRepository.findAll(pageRequest);
        return cronsPage.hasContent() ? cronsPage.getContent().get(0) : null;
    }

    public Page<Cron> getCronsByPage(int page){
        PageRequest pageRequest = PageRequest.of(page, maxPageCapacity, Sort.by("createdOn").descending());
          Page<Cron> cron = cronRepository.findAll(pageRequest);
          return cron;
      }

       

    public Map<String, Object> editCron(long id, String expression){
        StringBuilder errorMessage = new StringBuilder("");
        Map<String, Object> responce = new HashMap<>();

        if(CronValidator.isCronExpressionValid(expression, errorMessage)){
            Cron cron = getCronById(id);
            cron.setExpression(expression);
            cron = saveCron(cron);
            responce.put("type", "Cron");
            responce.put("cron", cron);

            return responce;
        } 

        responce.put("type", "error");
        responce.put("errorMessage", errorMessage.toString());

        return responce;
    }


    public Cron getCronById(long id){
       return cronRepository.findById(id).get();
    }

   public int getMaxPageCapacity(){
    return this.maxPageCapacity;
   }
}