package com.example.android.footballapp

import android.app.Activity
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.android.footballapp.adapter.PlayerListAdapter
import com.example.android.footballapp.api.ApiRepository
import com.example.android.footballapp.api.TheSportDBApi
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.matches_details_layout.*
import kotlinx.android.synthetic.main.team_details_activity_layout.*
import okhttp3.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import java.io.IOException

class TeamsDetailsActivity : AppCompatActivity() {

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var idTeam:Int = 0

    private var strTeam: String? = null
    private var strTeamBadge: String? = null
    private var intFormedYear: String? = null
    private var strStadium: String? = null
    private var strDescriptionEN: String? = null

    private val teamOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.team_players -> {
                scrollview_overview_team_details.visibility = View.GONE
                recyclerView_player_list_team_details_layout.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.team_overview -> {
                scrollview_overview_team_details.visibility = View.VISIBLE
                recyclerView_player_list_team_details_layout.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.team_details_activity_layout)


        strTeamBadge = intent.getStringExtra("teamBadgeLink")
        strTeam = intent.getStringExtra("strTeam")
        intFormedYear = intent.getStringExtra("intFormedYearTeam")
        strStadium = intent.getStringExtra("strStadium")
        strDescriptionEN = intent.getStringExtra("strDescription")
        idTeam = intent.getStringExtra("idTeamClicked")!!.toInt()

        favoriteState()

        Picasso.get().load(strTeamBadge).into(team_logo_details)
        team_year_details.text = intFormedYear
        team_name_details.text = strTeam
        team_stadion_details.text = strStadium
        overview_text_layout.text = strDescriptionEN

        scrollview_overview_team_details.visibility = View.VISIBLE
        recyclerView_player_list_team_details_layout.visibility = View.GONE
        recyclerView_player_list_team_details_layout.layoutManager = LinearLayoutManager(this)

        fetchJsonTeamPlayer(ApiRepository())

        team_navigation.setOnNavigationItemSelectedListener(teamOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()
                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                    .whereArgs(FavoriteTeam.TEAM_ID + " = {idTeam}",
                            "idTeam" to idTeam)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                this.insert(FavoriteTeam.TABLE_FAVORITE_TEAM,
                        FavoriteTeam.TEAM_ID to idTeam,
                        FavoriteTeam.TEAM_NAME to strTeam,
                        FavoriteTeam.TEAM_BADGE to strTeamBadge,
                        FavoriteTeam.TEAM_FORMED_YEAR to intFormedYear,
                        FavoriteTeam.TEAM_STADIUM to strStadium,
                        FavoriteTeam.TEAM_DESCRIPTION to strDescriptionEN)
            }
            Snackbar.make(getWindow().getDecorView(), "Added to favorite", Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException){
            Snackbar.make(getWindow().getDecorView(), e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                this.delete(FavoriteTeam.TABLE_FAVORITE_TEAM, FavoriteTeam.TEAM_ID + " = {idTeam}",
                        "idTeam" to idTeam)
            }
            Snackbar.make(getWindow().getDecorView(), "Removed from favorite", Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException){
            Snackbar.make(getWindow().getDecorView(), e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorite)
        }else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorite)
        }
    }

    private fun fetchJsonTeamPlayer(apiRepository: ApiRepository) {
        val id = intent.getStringExtra("idTeamClicked")
        val request = Request.Builder().url(TheSportDBApi.getTeamPlayerList(id)).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val gson = GsonBuilder().create()
                val playerLists = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamPlayerList(id)),
                        PlayerList::class.java)
                runOnUiThread { recyclerView_player_list_team_details_layout.adapter = PlayerListAdapter(playerLists) }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                println("failed request")
            }
        })
    }
}

class PlayerList(val player: List<Player>)
class Player(val strPlayer: String, val strCutout: String, val strThumb: String, val strDescriptionEN: String,
             val strHeight: String, val strWeight: String, val strPosition: String)