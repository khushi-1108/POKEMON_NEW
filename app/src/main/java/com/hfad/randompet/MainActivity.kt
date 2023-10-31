package com.hfad.randompet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.randompet.PokemonAdapter
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private val pokemonList = mutableListOf<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = PokemonAdapter(pokemonList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch 20 random Pokémon and update the RecyclerView
        for (i in 1..20) {
            fetchRandomPokemonData()
        }
    }

    private fun fetchRandomPokemonData() {
        val client = OkHttpClient()
        val randomPokemonId = (1..807).random() // Generate a random Pokémon ID between 1 and 807

        val url = "https://pokeapi.co/api/v2/pokemon/$randomPokemonId/"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val jsonObject = JSONObject(json)
                    val imageUrl = jsonObject.getJSONObject("sprites").getString("front_default")
                    val name = jsonObject.getString("name")

                    runOnUiThread {
                        // Set a default description or leave it empty
                        val defaultDescription = "This is the character from Pokemon"
                        val newPokemon = Pokemon(name, imageUrl, defaultDescription)
                        pokemonList.add(newPokemon)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle API request failure
            }
        })
    }
}