package com.example.android.footballapp.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.footballapp.Events
import com.example.android.footballapp.MatchesDetailsActivity
import com.example.android.footballapp.NextMatch
import com.example.android.footballapp.R
import kotlinx.android.synthetic.main.next_match_item_row.view.*

class NextMatchAdapter(private val nextMatch: NextMatch): RecyclerView.Adapter<CustomViewHolderNextMatch>(){

    override fun getItemCount(): Int {
        return nextMatch.events.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderNextMatch {
        val layoutInflater = LayoutInflater.from(parent.context)
        val callForRow = layoutInflater.inflate(R.layout.next_match_item_row,parent,false)
        return CustomViewHolderNextMatch(callForRow, null)
    }

    override fun onBindViewHolder(holder: CustomViewHolderNextMatch, position: Int){
        val events = nextMatch.events[position]
        holder.view.team_home.text = events.strHomeTeam
        holder.view.team_away.text = events.strAwayTeam
        holder.view.time_schedule.text = events.strDate
        holder.events = events
    }

}

class CustomViewHolderNextMatch(val view : View, var events: Events?): RecyclerView.ViewHolder(view){
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, MatchesDetailsActivity::class.java)
            intent.putExtra("idEvent", events?.idEvent)
            intent.putExtra("eventNameStr", events?.strEvent)
            intent.putExtra("teamHome", events?.strHomeTeam)
            intent.putExtra("teamAway", events?.strAwayTeam)
            intent.putExtra("scoreHome", -1)
            intent.putExtra("scoreAway", -1)
            intent.putExtra("idHome", events?.idHomeTeam)
            intent.putExtra("idAway", events?.idAwayTeam)
            intent.putExtra("homeGoalDetail", "-")
            intent.putExtra("awayGoalDetail", "-")
            intent.putExtra("dateTime", events?.strDate)
            view.context.startActivity(intent)
        }
    }
}