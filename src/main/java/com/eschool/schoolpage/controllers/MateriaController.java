package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.MateriaDTO;
import com.eschool.schoolpage.repositories.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/materias")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllMaterias(){
        List<MateriaDTO> materiaDTOS = materiaRepository.findAll().stream().map(materia -> new MateriaDTO(materia)).collect(Collectors.toList());
        return new ResponseEntity<>(materiaDTOS, HttpStatus.OK);
    }
}
