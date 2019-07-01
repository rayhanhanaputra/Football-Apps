package com.example.android.footballapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.android.footballapp.adapter.EventListSearchAdapter
import com.example.android.footballapp.adapter.TeamListSearchAdapter
import com.example.android.footballapp.api.ApiRepository
import com.example.android.footballapp.api.TheSportDBApi
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_search_result.*
import okhttp3.*
import java.io.IOException

class SearchResultsActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        team_list_search_recyclerView.layoutManager = LinearLayoutManager(this)
        event_list_search_recyclerView.layoutManager = LinearLayoutManager(this)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent){
            val query = intent.getStringExtra("TITLE")
            val searchData = intent.getBooleanExtra("condition", false)
            if(searchData){
                fetchJsonSearchTeam(ApiRepository(), query)
            }else{
                fetchJsonSearchEvent(ApiRepository(), query)
            }

    }

    private fun fetchJsonSearchEvent(apiRepository: ApiRepository,query: String?){
        val request2 = Request.Builder().url(TheSportDBApi.searchEvent(query)).build()
        val client = OkHttpClient()

        client.newCall(request2).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?){
                val gson = GsonBuilder().create()
                val eventList = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.searchEvent(query)),
                        EventList::class.java)
                runOnUiThread { event_list_search_recyclerView.adapter = EventListSearchAdapter(eventList) }
            }
            override fun onFailure(call: Call?, e: IOException?){
                println("failed request")
            }
        })
    }

    private fun fetchJsonSearchTeam(apiRepository: ApiRepository, query: String?){
        val request = Request.Builder().url(TheSportDBApi.searchTeam(query)).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?){
                val gson = GsonBuilder().create()
                val teamList = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.searchTeam(query)),
                        TeamList::class.java)
                runOnUiThread { team_list_search_recyclerView.adapter = TeamListSearchAdapter(teamList) }
            }
            override fun onFailure(call: Call?, e: IOException?){
                println("failed request")
            }
        })
    }
}