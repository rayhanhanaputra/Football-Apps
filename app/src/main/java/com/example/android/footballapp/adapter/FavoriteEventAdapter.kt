package com.example.android.footballapp.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.footballapp.FavoriteEvent
import com.example.android.footballapp.MatchesDetailsActivity
import com.example.android.footballapp.R
import org.jetbrains.anko.find

class FavoriteEventAdapter(private val favoriteEvent: List<FavoriteEvent>)
    : RecyclerView.Adapter<FavoriteEventViewHolder>(){


    override fun getItemCount(): Int {
        return favoriteEvent.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val callForRow = layoutInflater.inflate(R.layout.previous_match_item_row,parent,false)
        return FavoriteEventViewHolder(callForRow)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bindItem(favoriteEvent[position])
    }

}

class FavoriteEventViewHolder(view: View): RecyclerView.ViewHolder(view){
    private val homeTeam: TextView = view.find(R.id.team_homes)
    private val awayTeam: TextView = view.find(R.id.team_aways)
    private val homeScore: TextView = view.find(R.id.score_homes)
    private val awayScore: TextView = view.find(R.id.score_aways)
    private val dateSchedule: TextView = view.find(R.id.time_schedules)

    fun bindItem(favoriteEvent: FavoriteEvent){
        homeTeam.text = favoriteEvent.strHomeTeam
        awayTeam.text = favoriteEvent.strAwayTeam

        if(favoriteEvent.intHomeScore == "-1"){
            homeScore.text = ""
        }else{
            homeScore.text = favoriteEvent.intHomeScore
        }

        if(favoriteEvent.intAwayScore == "-1"){
            awayScore.text = ""
        }else{
            awayScore.text = favoriteEvent.intAwayScore
        }

        dateSchedule.text = favoriteEvent.strDate

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, MatchesDetailsActivity::class.java)
            intent.putExtra("idEvent", favoriteEvent.idEvent)
            intent.putExtra("eventNameStr", favoriteEvent.strEvent)
            intent.putExtra("teamHome", favoriteEvent.strHomeTeam)
            intent.putExtra("teamAway",favoriteEvent.strAwayTeam)
            intent.putExtra("scoreHome", favoriteEvent.intHomeScore.toInt())
            intent.putExtra("scoreAway", favoriteEvent.intAwayScore.toInt())
            intent.putExtra("idHome", favoriteEvent.idHomeTeam)
            intent.putExtra("idAway", favoriteEvent.idAwayTeam)
            intent.putExtra("homeGoalDetail", favoriteEvent.strHomeGoalDetails)
            intent.putExtra("awayGoalDetail", favoriteEvent.strAwayGoalDetails)
            intent.putExtra("dateTime", favoriteEvent.strDate)
            itemView.context.startActivity(intent)
        }
    }
}