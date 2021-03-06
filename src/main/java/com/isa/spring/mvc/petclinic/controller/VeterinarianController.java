package com.isa.spring.mvc.petclinic.controller;

import com.isa.spring.mvc.petclinic.data.model.Clinic;
import com.isa.spring.mvc.petclinic.data.model.Specialty;
import com.isa.spring.mvc.petclinic.data.model.Veterinarian;
import com.isa.spring.mvc.petclinic.data.repository.ClinicRepository;
import com.isa.spring.mvc.petclinic.data.repository.SpecialtyRepository;
import com.isa.spring.mvc.petclinic.data.validator.VeterinarianValidator;
import com.isa.spring.mvc.petclinic.service.VeterinarianService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clinics/{clinicId}/vets/{vetId}")
public class VeterinarianController {
    private final VeterinarianService veterinarianService;

    private final ClinicRepository clinicRepository;

    private final SpecialtyRepository specialtyRepository;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService,
                                  ClinicRepository clinicRepository,
                                  SpecialtyRepository specialtyRepository) {
        this.veterinarianService = veterinarianService;
        this.clinicRepository = clinicRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @ModelAttribute("clinic")
    public Clinic getClinic(@PathVariable("clinicId") long clinicId) {
        return clinicRepository.findOne(clinicId);
    }

    @ModelAttribute("specialties")
    public List<Specialty> getSpecialties(){
        return specialtyRepository.findAll();
    }

    @ModelAttribute("veterinarian")
    public Veterinarian getVeterinarian(@PathVariable("vetId") long vetId) {
        return veterinarianService.findOne(vetId);
    }

    @InitBinder("veterinarian")
    public void setAllowedFields(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }

    @InitBinder("veterinarian")
    public void setValidator(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new VeterinarianValidator());
    }

    @GetMapping("/details")
    public String details() {
        return "veterinarians/details";
    }

    @GetMapping("/edit")
    public String edit() {
        return "veterinarians/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Veterinarian veterinarian, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "veterinarians/edit";
        } else {
            veterinarianService.save(veterinarian);
            return "redirect:/clinics/{clinicId}/vets";
        }
    }

    @GetMapping("/delete")
    public String delete() {
        return "veterinarians/delete";
    }

    @PostMapping("/delete")
    public String delete(@PathVariable("vetId") long vetId) {
        veterinarianService.delete(vetId);
        return "redirect:/clinics/{clinicId}/vets";
    }
}
