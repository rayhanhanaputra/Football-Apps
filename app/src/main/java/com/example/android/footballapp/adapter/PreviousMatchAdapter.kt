package com.example.android.footballapp.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.footballapp.Events
import com.example.android.footballapp.MatchesDetailsActivity
import com.example.android.footballapp.PrevMatch
import com.example.android.footballapp.R
import kotlinx.android.synthetic.main.previous_match_item_row.view.*

class PreviousMatchAdapter(private val prevMatch: PrevMatch): RecyclerView.Adapter<CustomViewHolderPreviousMatch>(){

    override fun getItemCount(): Int {
        return prevMatch.events.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderPreviousMatch {
        val layoutInflater = LayoutInflater.from(parent.context)
        val callForRow = layoutInflater.inflate(R.layout.previous_match_item_row,parent,false)
        return CustomViewHolderPreviousMatch(callForRow, null)
    }

    override fun onBindViewHolder(holder: CustomViewHolderPreviousMatch, position: Int) {
        val events = prevMatch.events[position]
        holder.view.team_homes.text = events.strHomeTeam
        holder.view.team_aways.text = events.strAwayTeam
        holder.view.score_homes.text = events.intHomeScore.toString()
        holder.view.score_aways.text = events.intAwayScore.toString()
        holder.view.time_schedules.text = events.strDate
        holder.events = events
    }

}

class CustomViewHolderPreviousMatch(val view : View, var events: Events?): RecyclerView.ViewHolder(view) {
    init{
        view.setOnClickListener {
            val intent = Intent(view.context, MatchesDetailsActivity::class.java)
            intent.putExtra("idEvent", events?.idEvent)
            intent.putExtra("eventNameStr", events?.strEvent)
            intent.putExtra("teamHome", events?.strHomeTeam)
            intent.putExtra("teamAway", events?.strAwayTeam)
            intent.putExtra("scoreHome", events?. intHomeScore)
            intent.putExtra("scoreAway", events?.intAwayScore)
            intent.putExtra("idHome", events?.idHomeTeam)
            intent.putExtra("idAway", events?.idAwayTeam)
            if(events?.strHomeGoalDetails==null){
                intent.putExtra("homeGoalDetail", "")
            }else{
                intent.putExtra("homeGoalDetail", events?.strHomeGoalDetails)
            }
            if(events?.strAwayGoalDetails==null){
                intent.putExtra("awayGoalDetail","")
            }else{
                intent.putExtra("awayGoalDetail", events?.strAwayGoalDetails)
            }
            if(events?.strDate==null){
                intent.putExtra("dateTime", "Date Unknown")
            }else{
                intent.putExtra("dateTime", events?.strDate)
            }
            view.context.startActivity(intent)
        }
    }
}