package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonAnswer;
import com.example.pokedex.pokeapi.PokeapiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListPokemonAdapter listPokemonAdapter;
    private int offset;
    private boolean canLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listPokemonAdapter = new ListPokemonAdapter(this);
        recyclerView.setAdapter(listPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()  {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (canLoad) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " Llegamos al final.");

                            canLoad = false;
                            offset += 20;
                            getData(offset);
                        }
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        canLoad = true;
        offset = 0;
        getData(offset);
    }

    private void getData(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonAnswer> pokemonAnswerCall = service.getListPokemon(20, offset);

        pokemonAnswerCall.enqueue(new Callback<PokemonAnswer>() {
            @Override
            public void onResponse(Call<PokemonAnswer> call, Response<PokemonAnswer> response) {
                canLoad = true;
                if (response.isSuccessful()) {
                    PokemonAnswer pokemonAnswer = response.body();
                    ArrayList<Pokemon> listPokemon = pokemonAnswer.getResults();

                    listPokemonAdapter.addListPokemon(listPokemon);
                } else {
                    Log.e(TAG, " onResponse: "  + response.body());
                }
            }

            @Override
            public void onFailure(Call<PokemonAnswer> call, Throwable t) {
                Log.e(TAG, " onFailure: " + t.getMessage());
            }
        });
    }
}