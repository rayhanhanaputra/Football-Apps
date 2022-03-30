package com.example.android.footballapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.example.android.footballapp.adapter.*
import com.example.android.footballapp.api.ApiRepository
import com.example.android.footballapp.api.TheSportDBApi
import com.example.android.footballapp.api.TheSportDBApi.getLeagueId
import com.example.android.footballapp.api.TheSportDBApi.getLeagueString
import com.example.android.footballapp.api.TheSportDBApi.getMatchNext
import com.example.android.footballapp.api.TheSportDBApi.getMatchPast
import com.example.android.footballapp.api.TheSportDBApi.getTeamList
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import java.io.IOException

class MainActivity : AppCompatActivity(){
    private lateinit var adapterFavoriteEvents: FavoriteEventAdapter
    private var favoriteEvent: MutableList<FavoriteEvent> = mutableListOf()

    private lateinit var adapterFavoriteTeams: FavoriteTeamAdapter
    private var favoriteTeam: MutableList<FavoriteTeam> = mutableListOf()

    private lateinit var adapterPrevMatch: PreviousMatchAdapter

    private var searchData: Boolean = false
    private lateinit var searchButton: MenuItem

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_matches -> {
                upperToolbar.visibility = View.GONE
                recyclerView_previous_match.visibility = View.VISIBLE
                recyclerView_next_match.visibility = View.GONE
                recyclerView_teams_list.visibility = View.GONE
                matches_navigation.visibility = View.VISIBLE
                league_spinner.visibility = View.VISIBLE
                favorite_navigation_switch.visibility = View.GONE
                layout_favorite.visibility = View.GONE
                searchButton.setVisible(true)
                searchData = false
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_teams -> {
                upperToolbar.visibility = View.VISIBLE
                recyclerView_previous_match.visibility = View.GONE
                recyclerView_next_match.visibility = View.GONE
                recyclerView_teams_list.visibility = View.VISIBLE
                matches_navigation.visibility = View.GONE
                league_spinner.visibility = View.VISIBLE
                favorite_navigation_switch.visibility = View.GONE
                layout_favorite.visibility = View.GONE
                searchButton.setVisible(true)
                searchData = true
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                upperToolbar.visibility = View.GONE
                recyclerView_previous_match.visibility = View.GONE
                recyclerView_next_match.visibility = View.GONE
                recyclerView_teams_list.visibility = View.GONE
                matches_navigation.visibility = View.GONE
                league_spinner.visibility = View.GONE
                favorite_navigation_switch.visibility = View.VISIBLE
                layout_favorite.visibility = View.VISIBLE
                recyclerView_favorite_matches.visibility = View.VISIBLE
                recyclerView_favorite_teams.visibility = View.GONE
                showFavorite()
                searchButton.setVisible(false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchButton = menu.findItem(R.id.search)
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean{
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean{
                getInput(query)
                return true
            }
        })
        return true
    }

    private fun getInput(searchText: String){
        val intent = Intent(this, SearchResultsActivity::class.java)
        intent.putExtra("TITLE", searchText)
        intent.putExtra("condition", searchData)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        upperToolbar.visibility = View.GONE

        recyclerView_previous_match.layoutManager = LinearLayoutManager(this)
//        recyclerView_previous_match.adapter = adapterPrevMatch
        recyclerView_next_match.layoutManager = LinearLayoutManager(this)
        recyclerView_teams_list.layoutManager = LinearLayoutManager(this)
        recyclerView_favorite_matches.layoutManager = LinearLayoutManager(this)
        recyclerView_favorite_teams.layoutManager = LinearLayoutManager(this)

        recyclerView_previous_match.visibility = View.VISIBLE
        recyclerView_next_match.visibility = View.GONE
        recyclerView_teams_list.visibility = View.GONE
        recyclerView_favorite_matches.visibility = View.GONE
        recyclerView_favorite_teams.visibility = View.GONE
        layout_favorite.visibility = View.GONE

        matches_navigation.visibility = View.VISIBLE
        favorite_navigation_switch.visibility = View.GONE

        fetchJson(ApiRepository())

        //item selected listener for spinner
        league_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                fetchJson(ApiRepository())
            }

        }

        adapterFavoriteEvents = FavoriteEventAdapter(favoriteEvent)
        adapterFavoriteTeams = FavoriteTeamAdapter(favoriteTeam)

        showFavorite()

        matches_navigation.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (matches_navigation.selectedTabPosition) {
                    0 -> {
                        recyclerView_previous_match.visibility = View.VISIBLE
                        recyclerView_next_match.visibility = View.GONE
                        fetchJson(ApiRepository())
                    }
                    1 -> {
                        recyclerView_previous_match.visibility = View.GONE
                        recyclerView_next_match.visibility = View.VISIBLE
                        fetchJson(ApiRepository())
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (matches_navigation.selectedTabPosition) {
                    0 -> {
                        recyclerView_previous_match.visibility = View.VISIBLE
                        recyclerView_next_match.visibility = View.GONE
                        fetchJson(ApiRepository())
                    }
                    1 -> {
                        recyclerView_previous_match.visibility = View.GONE
                        recyclerView_next_match.visibility = View.VISIBLE
                        fetchJson(ApiRepository())
                    }
                }
            }
        })
        favorite_navigation_switch.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (favorite_navigation_switch.selectedTabPosition) {
                    0 -> {
                        recyclerView_favorite_matches.visibility = View.VISIBLE
                        recyclerView_favorite_teams.visibility = View.GONE
                        showFavorite()
                    }
                    1 -> {
                        recyclerView_favorite_matches.visibility = View.GONE
                        recyclerView_favorite_teams.visibility = View.VISIBLE
                        showFavorite()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (favorite_navigation_switch.selectedTabPosition) {
                    0 -> {
                        recyclerView_favorite_matches.visibility = View.VISIBLE
                        recyclerView_favorite_teams.visibility = View.GONE
                        showFavorite()
                    }
                    1 -> {
                        recyclerView_favorite_matches.visibility = View.GONE
                        recyclerView_favorite_teams.visibility = View.VISIBLE
                        showFavorite()
                    }
                }
            }
        })
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun showFavorite(){
        database.use {
            val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            favoriteEvent.clear()
            favoriteEvent.addAll(favorite)
            adapterFavoriteEvents.notifyDataSetChanged()
            recyclerView_favorite_matches.adapter = adapterFavoriteEvents
        }
        database.use{
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            favoriteTeam.clear()
            favoriteTeam.addAll(favorite)
            adapterFavoriteTeams.notifyDataSetChanged()
            recyclerView_favorite_teams.adapter = adapterFavoriteTeams
        }
    }

    private fun fetchJson(apiRepository: ApiRepository){
        val id = getLeagueId(league_spinner.selectedItem.toString()).toString()
        val leagueName = getLeagueString(league_spinner.selectedItem.toString())
        val request = Request.Builder().url(getMatchPast(id)).build()
        val request2 = Request.Builder().url(getMatchNext(id)).build()
        val request3 = Request.Builder().url(getTeamList(leagueName)).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?){
                val gson = GsonBuilder().create()
                val prevMatch = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getMatchPast(id)),
                        PrevMatch::class.java)

                runOnUiThread {
                    recyclerView_previous_match.adapter = PreviousMatchAdapter(prevMatch)
                }
            }
            override fun onFailure(call: Call?, e: IOException?){
                println("failed request")
            }
        })

        client.newCall(request2).enqueue(object: Callback{
            override fun onResponse(call: Call?, response: Response?){
                val gson = GsonBuilder().create()
                val nextMatch = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getMatchNext(id)),
                        NextMatch::class.java)
                runOnUiThread { recyclerView_next_match.adapter = NextMatchAdapter(nextMatch) }
            }
            override fun onFailure(call: Call?, e: IOException?){
                println("failed request")
            }
        })

        client.newCall(request3).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?){
                val gson = GsonBuilder().create()
                val teamLists = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamList(leagueName)),
                        TeamList::class.java)
                runOnUiThread { recyclerView_teams_list.adapter = TeamListAdapter(teamLists) }
            }
            override fun onFailure(call: Call?, e: IOException?){
                println("failed request")
            }
        })
    }
}

class PrevMatch(val events: List<Events>)
class NextMatch(val events: List<Events>)
class EventList(val event: List<Events>)
class TeamList(val teams: List<Teams>)
class Events(val idEvent: String, val strEvent: String,val strHomeTeam: String, val strAwayTeam: String, val intHomeScore: Int,
             val intAwayScore: Int, val strHomeGoalDetails: String, val strAwayGoalDetails: String,
             val strDate: String, val idHomeTeam: String, val idAwayTeam: String)
