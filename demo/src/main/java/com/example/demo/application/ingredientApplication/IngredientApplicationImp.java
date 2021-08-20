package com.example.demo.application.ingredientApplication;

import java.util.UUID;

import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientApplicationImp implements IngredientApplication {

    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public IngredientApplicationImp(IngredientRepository ingredientRepository){
        this.ingredientRepository = ingredientRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public IngredientDTO add(CreateOrUpdateIngredientDTO dto) {

        Ingredient ingredient = modelMapper.map(dto, Ingredient.class);
        ingredient.setId(UUID.randomUUID());
        
        this.ingredientRepository.add(ingredient);

        return modelMapper.map(ingredient, IngredientDTO.class);
    }
}
