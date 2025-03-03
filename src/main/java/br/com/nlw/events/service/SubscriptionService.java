package br.com.nlw.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.exception.UserIndicadorNotFoundException;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repo.EventRepo;
import br.com.nlw.events.repo.UserRepo;
import br.com.nlw.events.repo.SubscriptionRepo;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepo evtRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubscriptionRepo subRepo;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId){
        
        //Recuperar evento pelo nome
        Event evt = evtRepo.findByPrettyName(eventName);
        if (evt == null) { //Caso alternativo 2
            throw new EventNotFoundException("Evento "+eventName+ "nao existe");
            
        }
        User userRec = userRepo.findByEmail(user.getEmail());
        if (userRec == null) { ////Caso alternativo 1
            userRec = userRepo.save(user);
            
        }
        User indicador = userRepo.findById(userId).orElse(null);
        if (indicador == null) {
            throw new UserIndicadorNotFoundException("Usuario"+userId+ "indicador não existe");
        }

        Subscription subs = new Subscription();
        subs.setEvent(evt);
        subs.setSubscriber(userRec);
        subs.setIndication(indicador);

        Subscription tmpSub = subRepo.findByEventAndSubscriber(evt, userRec);  
        if (tmpSub != null) { //Caso alternativo 3
            throw new SubscriptionConflictException("Ja existe inscrição para o Usuario cadastrado"+userRec.getName());
            
        }        

        Subscription res = subRepo.save(subs);
        return new SubscriptionResponse(res.getSubscriptionNumber(), "http://codecraft.com/subscription"+res.getEvent().getPrettyName()+"/"+res.getSubscriber().getId());

    }

}
