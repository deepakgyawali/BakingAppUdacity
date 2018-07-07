package com.deepakgyawali.bakingappudacity.event;

/**
 * RecipeStepEvent Class
 * Created by Deepak Gyawali, www.deepakgyawali.com.np
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 2018
 */


public class RecipeStepEvent {

    private int selectedPosition;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
