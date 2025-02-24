package br.com.nlw.events.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.exception.EventNotFoundException;
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

    public Subscription createNewSubscription(String eventName, User user){
        
        //Recuperar evento pelo nome
        Event evt = evtRepo.findByPrettyName(eventName);
        if (evt == null) {
            throw new EventNotFoundException("Evento "+eventName+ "nao existe");
            
        }
        User userRec = userRepo.findByEmail(user.getEmail());
        if (userRec == null) {
            user = userRepo.save(user);
            
        }

        Subscription subs = new Subscription();
        subs.setEvent(evt);
        subs.setSubscriber(userRec);

        Subscription res = subRepo.save(subs);
        return res;

    }

}
