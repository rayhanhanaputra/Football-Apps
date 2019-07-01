package com.example.android.footballapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, "FavoriteList", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(context.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.createTable(FavoriteEvent.TABLE_FAVORITE_EVENT, true,
                FavoriteEvent.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteEvent.EVENT_ID to TEXT + UNIQUE,
                FavoriteEvent.EVENT_NAME to TEXT,
                FavoriteEvent.EVENT_HOME_NAME to TEXT,
                FavoriteEvent.EVENT_AWAY_NAME to TEXT,
                FavoriteEvent.EVENT_HOME_SCORE to TEXT,
                FavoriteEvent.EVENT_AWAY_SCORE to TEXT,
                FavoriteEvent.EVENT_HOME_GOAL_DETAILS to TEXT,
                FavoriteEvent.EVENT_AWAY_GOAL_DETAILS to TEXT,
                FavoriteEvent.EVENT_TIME to TEXT,
                FavoriteEvent.EVENT_ID_HOME to TEXT,
                FavoriteEvent.EVENT_ID_AWAY to TEXT)

        database.createTable(FavoriteTeam.TABLE_FAVORITE_TEAM, true,
                FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
                FavoriteTeam.TEAM_NAME to TEXT,
                FavoriteTeam.TEAM_BADGE to TEXT,
                FavoriteTeam.TEAM_FORMED_YEAR to TEXT,
                FavoriteTeam.TEAM_STADIUM to TEXT,
                FavoriteTeam.TEAM_DESCRIPTION to TEXT)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTable(FavoriteEvent.TABLE_FAVORITE_EVENT, true)
        database.dropTable(FavoriteTeam.TABLE_FAVORITE_TEAM, true)
    }
}

val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)

data class FavoriteEvent(val id: Long?,val idEvent: String,  val strEvent: String, val strHomeTeam: String,
                    val strAwayTeam: String, val intHomeScore: String, val intAwayScore: String,
                    val strHomeGoalDetails: String, val strAwayGoalDetails: String,
                    val strDate: String, val idHomeTeam: String, val idAwayTeam: String){
    companion object {
        const val TABLE_FAVORITE_EVENT: String = "TABLE_FAVORITE_EVENT"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "idEvent"
        const val EVENT_NAME: String = "strEvent"
        const val EVENT_HOME_NAME: String = "strHomeName"
        const val EVENT_AWAY_NAME: String = "strAwayTeam"
        const val EVENT_HOME_SCORE: String = "intHomeScore"
        const val EVENT_AWAY_SCORE: String = "intAwayScore"
        const val EVENT_HOME_GOAL_DETAILS: String = "strHomeGoalDetails"
        const val EVENT_AWAY_GOAL_DETAILS: String = "strAwayGoalDetails"
        const val EVENT_TIME: String = "strDate"
        const val EVENT_ID_HOME: String = "idHomeTeam"
        const val EVENT_ID_AWAY: String = "idAwayTeam"
    }
}

data class FavoriteTeam(val id: Long?, val idTeam: String, val strTeam: String, val strTeamBadge: String,
                        val intFormedYear: String, val strStadium: String,val strDescriptionEN: String){
    companion object {
        const val TABLE_FAVORITE_TEAM: String = "TABLE_FAVORITE_TEAM"
        const val ID: String = "ID_"
        const val TEAM_ID: String = "idTeam"
        const val TEAM_NAME: String = "strTeam"
        const val TEAM_BADGE: String = "strTeamBadge"
        const val TEAM_FORMED_YEAR: String = "intFormedYear"
        const val TEAM_STADIUM: String = "strStadium"
        const val TEAM_DESCRIPTION: String = "strDescriptionEN"
    }
}
