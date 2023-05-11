package com.dsi.spring.service.impl;

import java.util.List;

import com.dsi.spring.dao.ActorDao;
import com.dsi.spring.model.Actor;
import com.dsi.spring.service.ActorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorDao actorDao;

    @Override
    public void saveActor(Actor cast) {
        actorDao.save(cast);

    }

    @Override
    public List<Actor> getActors() {
        return actorDao.findAll();
    }

    @Override
    public void deleteActor(Actor actor) {

        actorDao.delete(actor);

    }

    @Override
    public Actor getActorById(Long id) {

        return actorDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Cast id: " + id));
    }

}
