package com.example.pokedex.pokeapi;

import com.example.pokedex.models.PokemonAnswer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonAnswer> getListPokemon(@Query("limit") int limit, @Query("offset") int offset);
}
