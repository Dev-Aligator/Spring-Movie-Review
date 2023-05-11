package com.dsi.spring.service;

import java.util.List;

import com.dsi.spring.model.Actor;

import org.springframework.stereotype.Service;

@Service
public interface ActorService {
    void saveActor(Actor actor);

    List<Actor> getActors();

    void deleteActor(Actor actor);

    Actor getActorById(Long id) throws Exception;

}
