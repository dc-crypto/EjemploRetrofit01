package com.diegocastro.ejemploretrofit01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.diegocastro.ejemploretrofit01.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    //Activamos el viewBinding pasos 1 . 2 . 3

    //.1
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages= mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //.2
        binding= ActivityMainBinding.inflate(layoutInflater)

        //.3 cambiamos R.layout.activity_main por binding.root
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter= DogAdapter(dogImages)
        binding.rvDogs.layoutManager=LinearLayoutManager(this)
        binding.rvDogs.adapter=adapter
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<DogsResponse> =getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppies: DogsResponse? =call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    val images:List<String> = puppies?.images ?:emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()
                }
                else{
                    showError()
                }
            }

        }


    }
    private fun showError(){
        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query : String?): Boolean {
        if(!query.isNullOrEmpty())
            searchByName(query.toLowerCase())
        return true
    }


    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}