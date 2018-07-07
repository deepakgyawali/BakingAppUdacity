package com.deepakgyawali.bakingappudacity.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Recipe Class
 * Created by Deepak Gyawali, www.deepakgyawali.com.np
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 2018
 */


public class Recipe {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private List<Ingredient> ingredients;
    @SerializedName("steps")
    private List<Step> steps;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
