package com.example.movieapp

import android.net.DnsResolver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.api.ApiClient
import com.example.movieapp.api.ApiService
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.response.MoviesListResponse
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val movieAdapter by lazy { MovieAdapter() }
    private val api by lazy {
        ApiClient().getClient().create(ApiService:: class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            prgBarMovie.visibility=View.VISIBLE

            val callMovieApi = api.getPopularMovie(1)
            callMovieApi.enqueue(object: retrofit2.Callback<MoviesListResponse> {
                override fun onResponse(
                    call: Call<MoviesListResponse>,
                    response: Response<MoviesListResponse>
                ) {
                    prgBarMovie.visibility=View.GONE
                    when(response.code()){
                        //successful response
                        in 200..299 ->{
                         response.body().let { itBody ->
                             itBody?.results.let{itData->
                                 if(itData!!.isNotEmpty()){
                                     movieAdapter.differ.submitList(itData)
                                     rvMovie.apply {
                                         layoutManager=LinearLayoutManager(this@MainActivity)
                                         adapter=movieAdapter
                                     }
                                 }

                             }
                         }
                        }
                        //Redirection Response
                        in 300..399 ->{
                            Log.d("Response Code", " Redirection messages : ${response.code()}")
                        }
                        //Client Error Response
                        in 400..499 ->{
                            Log.d("Response Code", " Client Error messages : ${response.code()}")

                        }
                        //Server Error Response
                        in 500..599 ->{
                            Log.d("Response Code", " Server Error messages : ${response.code()}")
                        }
                    }

                }

                override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                    prgBarMovie.visibility=View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }

            })


        }

    }
}