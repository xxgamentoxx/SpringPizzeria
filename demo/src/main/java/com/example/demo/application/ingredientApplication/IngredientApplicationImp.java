package com.example.demo.application.ingredientApplication;

import java.util.List;
import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;
import com.example.demo.domain.ingredientDomain.IngredientReadRepository;
import com.example.demo.domain.ingredientDomain.IngredientWriteRepository;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientApplicationImp extends ApplicationBase<Ingredient, UUID> implements IngredientApplication {

    private final IngredientWriteRepository ingredientWriteRepository;
    private final IngredientReadRepository ingredientReadRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    @Autowired
    public IngredientApplicationImp(final IngredientWriteRepository ingredientWriteRepository,
            final IngredientReadRepository ingredientReadRepository, final ModelMapper modelMapper,
            final Logger logger) {

        super((id) -> ingredientWriteRepository.findById(id));

        this.ingredientWriteRepository = ingredientWriteRepository;
        this.ingredientReadRepository = ingredientReadRepository;
        this.modelMapper = modelMapper;
        this.logger = logger;
    }

    @Override
    public IngredientDTO add(CreateOrUpdateIngredientDTO dto) {

        Ingredient ingredient = modelMapper.map(dto, Ingredient.class);
        ingredient.setId(UUID.randomUUID());
        ingredient.validate("name", ingredient.getName(), (name) -> this.ingredientWriteRepository.exists(name));

        this.ingredientWriteRepository.add(ingredient);
        logger.info(this.serializeObject(ingredient, "added"));

        return modelMapper.map(ingredient, IngredientDTO.class);
    }

    @Override
    public IngredientDTO get(UUID id) {

        Ingredient ingredient = this.findById(id);
        return this.modelMapper.map(ingredient, IngredientDTO.class);
    }

    @Override
    public IngredientDTO update(UUID id, CreateOrUpdateIngredientDTO dto) {

        Ingredient ingredientToUpdate = this.findById(id);
        Ingredient ingredientUpdated = modelMapper.map(dto, Ingredient.class);
        ingredientUpdated.setId(id);
        
        if (ingredientToUpdate.getName().equals(dto.getName())){

            ingredientUpdated.validate();
        } else {

            ingredientUpdated.validate("name", ingredientUpdated.getName(), (name) -> this.ingredientWriteRepository.exists(name));
        }
        this.ingredientWriteRepository.update(ingredientUpdated);
        logger.info(this.serializeObject(ingredientUpdated, "updated"));

        return this.modelMapper.map(ingredientUpdated, IngredientDTO.class);
    }

    @Override
    public void delete(UUID id) {

        Ingredient ingredient = this.findById(id);
        this.ingredientWriteRepository.delete(ingredient);
        logger.info(this.serializeObject(ingredient, "deleted"));
    }

    @Override
    public List<IngredientProjection> getAll(String name, int page, int size) {
        return this.ingredientReadRepository.getAll(name, page, size);
    }

    private String serializeObject(Ingredient ingredient, String messege) {

        return String.format("Ingredient {id: %s, name: %s, price: %s} %s succesfully.", ingredient.getId(),
                ingredient.getName(), ingredient.getPrice().toString(), messege);
    }
}
