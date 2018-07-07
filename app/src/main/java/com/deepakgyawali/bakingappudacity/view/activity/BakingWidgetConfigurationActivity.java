package com.deepakgyawali.bakingappudacity.view.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.deepakgyawali.bakingappudacity.App;
import com.deepakgyawali.bakingappudacity.R;
import com.deepakgyawali.bakingappudacity.controller.RecipeController;
import com.deepakgyawali.bakingappudacity.event.RecipeEvent;
import com.deepakgyawali.bakingappudacity.model.Ingredient;
import com.deepakgyawali.bakingappudacity.model.Recipe;
import com.deepakgyawali.bakingappudacity.view.adapter.SimpleRecipesAdapter;
import com.deepakgyawali.bakingappudacity.view.callback.SimpleRecipeOnClickListener;

import static com.deepakgyawali.bakingappudacity.util.Constant.Data.EXTRA_RECIPE;
import static com.deepakgyawali.bakingappudacity.util.Constant.Data.LIST_DATA;
import static com.deepakgyawali.bakingappudacity.util.Constant.Data.LIST_STATE;
import static com.deepakgyawali.bakingappudacity.util.Constant.Data.WIDGET_ID;

/**
 * BakingWidgetConfiguration Class
 * Created by Deepak Gyawali, www.deepakgyawali.com.np
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 2018
 */

public class BakingWidgetConfigurationActivity extends AppCompatActivity implements SimpleRecipeOnClickListener {
    @BindView(R.id.baking_recipe_list)
    RecyclerView mBakingRecipeList;
    private SimpleRecipesAdapter mAdapter;
    private EventBus eventBus;
    private int mAppWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_widget_configuration);

        eventBus = App.getInstance().getEventBus();

        initView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBakingRecipeList.setLayoutManager(layoutManager);
        mBakingRecipeList.setHasFixedSize(true);

        mAdapter = new SimpleRecipesAdapter(this);
        mBakingRecipeList.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mBakingRecipeList.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE));
            mAdapter.setDataAdapter(Arrays.asList(App.getInstance().getGson().fromJson(savedInstanceState.getString(LIST_DATA), Recipe[].class)));
            mAppWidgetId = savedInstanceState.getInt(WIDGET_ID);
            return;
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        RecipeController controller = new RecipeController();
        controller.getRecipes();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        putRecipeToWidget(recipe);
    }

    private void putRecipeToWidget(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RECIPE, App.getInstance().getGson().toJson(recipe));
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.widget_ingredient_layout);

        views.setTextViewText(R.id.widget_ingredients_title, getString(R.string.widget_ingredients_title, recipe.getName()));

        String strIngredient = "";
        for (Ingredient ingredient : recipe.getIngredients()) {
            DecimalFormat format = new DecimalFormat("#.##");

            strIngredient += "- " + format.format(ingredient.getQuantity())
                    + " " + ingredient.getMeasure() + " of " + ingredient.getIngredient() + ".";
            strIngredient += "\n";
        }

        views.setTextViewText(R.id.widget_detail_ingredients, strIngredient);

        views.setOnClickPendingIntent(R.id.widget_detail_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecipes(RecipeEvent event) {
        if (event.isSuccess()) {
            mAdapter.setDataAdapter(event.getRecipes());
        } else {
            Toast.makeText(this, getString(R.string.error_adding_widget), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE, mBakingRecipeList.getLayoutManager().onSaveInstanceState());
        outState.putString(LIST_DATA, App.getInstance().getGson().toJson(mAdapter.getDataAdapter()));
        outState.putInt(WIDGET_ID, mAppWidgetId);
    }
}
