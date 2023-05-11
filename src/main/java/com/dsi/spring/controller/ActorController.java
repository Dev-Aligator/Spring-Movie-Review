package com.dsi.spring.controller;

import com.dsi.spring.model.Actor;
import com.dsi.spring.service.ActorService;
import com.dsi.spring.utility.FileUpload;
import com.dsi.spring.utility.constants.ImageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("actors", actorService.getActors());
        return "admin/movie/actor/actors";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createActorForm(Model model) {
        model.addAttribute("actorForm", new Actor());
        return "admin/movie/actor/actor_form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createActor(Actor actor, BindingResult result, @RequestParam(value = "file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(actor);
            return "admin/movie/actor/actor_form";
        }
        try {
            String path = FileUpload.saveImage(ImageType.CAST_DP, actor.getName(), file);
            actor.setImageUrl(path);
            actorService.saveActor(actor);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(actor);
            return "admin/movie/actor/actor_form";
        }
        return "redirect:/admin/actors/";
    }

    // shows update form
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String updateActorForm(@PathVariable("id") long id, Model model) {

        try {
            Actor actor = actorService.getActorById(id);
            model.addAttribute("actorForm", actor);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "admin/movie/actor/actor_form";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String updateActor(@PathVariable("id") long id, @Validated Actor actor, BindingResult result, Model model,
            @RequestParam(value = "file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            actor.setId(id);
            redirectAttributes.addFlashAttribute(actor);
            return "admin/movie/actor/actor_form";
        }

        try {
            if (!file.isEmpty()){
                String path = FileUpload.saveImage(ImageType.CAST_DP, actor.getName(), file);
                actor.setImageUrl(path);
            }
            else {
                actor.setImageUrl(actorService.getActorById(id).getImageUrl());
            }
            actorService.saveActor(actor);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(actor);
            return "admin/movie/actor/actor_form";
        }
        return "redirect:/admin/actors/";
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteActor(@PathVariable("id") long id, RedirectAttributes redirAttr) {
        try {
            Actor actor = actorService.getActorById(id);
            actorService.deleteActor(actor);
        } catch (Exception e) {
            redirAttr.addFlashAttribute("error", "Unable to delete cast assigned in a movie.");
            return "redirect:/admin/actors/";
        }

        return "redirect:/admin/actors/";
    }
}
