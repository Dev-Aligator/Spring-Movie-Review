package com.dsi.spring.dao;

import com.dsi.spring.model.Actor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorDao extends JpaRepository<Actor, Long> {

}
