package com.example.android.footballapp

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.android.footballapp.R.drawable.ic_add_to_favorite
import com.example.android.footballapp.R.drawable.ic_added_to_favorite
import com.example.android.footballapp.R.menu.favorite_menu
import com.example.android.footballapp.api.ApiRepository
import com.example.android.footballapp.api.TheSportDBApi
import com.example.android.footballapp.api.TheSportDBApi.getTeamDetail
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.matches_details_layout.*
import okhttp3.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import java.io.IOException

class MatchesDetailsActivity : AppCompatActivity() {

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    private var idEvent:Int = 0
    private lateinit var nameTeamHome: String
    private lateinit var nameTeamAway: String
    private lateinit var eventName: String
    private lateinit var dateTime: String
    private lateinit var homeGoalDetails: String
    private lateinit var awayGoalDetails: String
    private lateinit var homeScore: String
    private lateinit var awayScore: String
    private lateinit var idHome: String
    private lateinit var idAway: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matches_details_layout)

        fetchJSONS(ApiRepository())

        nameTeamHome = intent.getStringExtra("teamHome")
        nameTeamAway = intent.getStringExtra("teamAway")
        eventName = intent.getStringExtra("eventNameStr")
        homeScore = intent.getIntExtra("scoreHome",-1).toString()
        awayScore = intent.getIntExtra("scoreAway",-1).toString()
        dateTime = intent.getStringExtra("dateTime")
        homeGoalDetails = intent.getStringExtra("homeGoalDetail")
        awayGoalDetails = intent.getStringExtra("awayGoalDetail")
        idEvent = intent.getStringExtra("idEvent").toInt()
        idHome = intent.getStringExtra("idHome")
        idAway = intent.getStringExtra("idAway")


        //Set text

        tim_kandang.text = nameTeamHome
        tim_lawan.text = nameTeamAway
        score_kandang.text = homeScore
        score_lawan.text = awayScore

        if(score_kandang.text=="-1"){
            score_kandang.text = ""
        }

        if(score_lawan.text=="-1"){
            score_lawan.text = ""
        }

        home_scorer.text = homeGoalDetails
        away_scorer.text = awayGoalDetails
        tanggal_main.text = dateTime

        favoriteState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(favorite_menu, menu)
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
            val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
                    .whereArgs(FavoriteEvent.EVENT_ID + " = {idEvent}",
                            "idEvent" to idEvent)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                this.insert(FavoriteEvent.TABLE_FAVORITE_EVENT,
                        FavoriteEvent.EVENT_ID to idEvent,
                        FavoriteEvent.EVENT_NAME to eventName,
                        FavoriteEvent.EVENT_HOME_NAME to nameTeamHome,
                        FavoriteEvent.EVENT_AWAY_NAME to nameTeamAway,
                        FavoriteEvent.EVENT_HOME_SCORE to homeScore,
                        FavoriteEvent.EVENT_AWAY_SCORE to awayScore,
                        FavoriteEvent.EVENT_HOME_GOAL_DETAILS to homeGoalDetails,
                        FavoriteEvent.EVENT_AWAY_GOAL_DETAILS to awayGoalDetails,
                        FavoriteEvent.EVENT_TIME to dateTime,
                        FavoriteEvent.EVENT_ID_HOME to idHome,
                        FavoriteEvent.EVENT_ID_AWAY to idAway)
            }
            snackbar(coordinatorLayout, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(coordinatorLayout, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                this.delete(FavoriteEvent.TABLE_FAVORITE_EVENT, FavoriteEvent.EVENT_ID + " = {idEvent}",
                        "idEvent" to idEvent)
            }
            snackbar(coordinatorLayout, "Removed from favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(coordinatorLayout, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorite)
        }else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorite)
        }
    }

    private fun fetchJSONS(apiRepository: ApiRepository){
        val idHome = intent.getStringExtra("idHome")
        val idAway = intent.getStringExtra("idAway")
        val homeTeamUrl = getTeamDetail(idHome)
        val awayTeamUrl = getTeamDetail(idAway)
        val requestHome = Request.Builder().url(homeTeamUrl).build()
        val requestAway = Request.Builder().url(awayTeamUrl).build()
        val client = OkHttpClient()

        //request untuk home team
        client.newCall(requestHome).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()
                val homeTeam = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamDetail(idHome)),
                        HomeTeam::class.java)
                runOnUiThread {
                    Picasso.get().load(setupImageHome(homeTeam)).into(logo_home)
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("failed request")
            }
        })

        // request untuk away team
        client.newCall(requestAway).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()
                val awayTeam = gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamDetail(idAway)),
                        AwayTeam::class.java)
                runOnUiThread {
                    Picasso.get().load(setupImageAway(awayTeam)).into(logo_away)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("failed request")
            }
        })
    }
}

class HomeTeam(val teams: List<Teams>)
class AwayTeam(val teams: List<Teams>)
class Teams(val strTeam: String, val strTeamBadge: String, val intFormedYear: String, val strStadium: String,
            val strDescriptionEN: String, val idTeam: String)

fun setupImageHome(homeTeam: HomeTeam): String{
    return homeTeam.teams[0].strTeamBadge
}

fun setupImageAway(awayTeam: AwayTeam): String{
    return awayTeam.teams[0].strTeamBadge
}