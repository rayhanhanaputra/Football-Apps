package com.example.android.footballapp.api

object TheSportDBApi {
    fun searchTeam(teamName: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/searchteams.php?t=$teamName"
    }
    fun searchEvent(strEvent: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/searchevents.php?e=$strEvent"
    }
    fun getMatchPast(id: String?): String {
        return "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=$id"
    }
    fun getMatchNext(id: String?): String {
        return "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=$id"
    }
    fun getTeamDetail(teamId: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=$teamId"
    }
    fun getTeamList(leagueString: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=$leagueString"
    }
    fun getTeamPlayerList(teamId: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/lookup_all_players.php?id=$teamId"
    }
    fun getLeagueId(teamLeague: String?): Int{
        return when(teamLeague){
            "English Premier League" -> 4328
            "English League Championship" -> 4329
            "German Bundesliga" -> 4331
            "Italian Serie A" -> 4332
            "French Ligue 1" -> 4334
            "Spanish La Liga" -> 4335
            else -> 1
        }
    }
    fun getLeagueString(teamLeague: String?): String{
        return when(teamLeague){
            "English Premier League" -> "English_Premier_League"
            "English League Championship" -> "English_League_Championship"
            "German Bundesliga" -> "German_Bundesliga"
            "Italian Serie A" -> "Italian_Serie_A"
            "French Ligue 1" -> "French_Ligue_1"
            "Spanish La Liga" -> "Spanish_La_Liga"
            else -> ""
        }
    }
}