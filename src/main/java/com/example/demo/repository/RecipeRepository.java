package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
