package com.example.android.footballapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.footballapp.EventList
import com.example.android.footballapp.Events
import com.example.android.footballapp.MatchesDetailsActivity
import com.example.android.footballapp.R
import kotlinx.android.synthetic.main.previous_match_item_row.view.*

class EventListSearchAdapter(private val eventList: EventList): RecyclerView.Adapter<CustomViewHolderEventSearch>(){
    override fun getItemCount(): Int {
        return when(eventList.event.size){
            null -> {0}
            else -> {eventList.event.size}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderEventSearch {
        val layoutInflater = LayoutInflater.from(parent.context)
        val callForRow = layoutInflater.inflate(R.layout.previous_match_item_row,parent,false)
        return CustomViewHolderEventSearch(callForRow, null)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolderEventSearch, position: Int) {
        val event = eventList.event[position]
        holder.view.team_homes.text = event.strHomeTeam
        holder.view.team_aways.text = event.strAwayTeam
        when(event.intHomeScore){
            null -> {holder.view.score_homes.text = ""}
            else -> {holder.view.score_homes.text = event.intHomeScore.toString()}
        }
        when(event.intAwayScore){
            null -> {holder.view.score_aways.text = ""}
            else -> {holder.view.score_aways.text = event.intAwayScore.toString()}
        }
        when(event.strDate){
            null -> {holder.view.time_schedules.text ="Date Unknown"}
            else -> {holder.view.time_schedules.text = event.strDate}
        }
        holder.event = event
    }

}
class CustomViewHolderEventSearch(val view : View, var event: Events?): RecyclerView.ViewHolder(view){
    init{
        view.setOnClickListener {
            val intent = Intent(view.context, MatchesDetailsActivity::class.java)
            intent.putExtra("idEvent", event?.idEvent)
            intent.putExtra("eventNameStr", event?.strEvent)
            intent.putExtra("teamHome", event?.strHomeTeam)
            intent.putExtra("teamAway", event?.strAwayTeam)
            intent.putExtra("scoreHome", event?. intHomeScore)
            intent.putExtra("scoreAway", event?.intAwayScore)
            intent.putExtra("idHome", event?.idHomeTeam)
            intent.putExtra("idAway", event?.idAwayTeam)
            if(event?.strHomeGoalDetails==null){
                intent.putExtra("homeGoalDetail", "")
            }else{
                intent.putExtra("homeGoalDetail", event?.strHomeGoalDetails)
            }
            if(event?.strAwayGoalDetails==null){
                intent.putExtra("awayGoalDetail","")
            }else{
                intent.putExtra("awayGoalDetail", event?.strAwayGoalDetails)
            }
            if(event?.strDate==null){
                intent.putExtra("dateTime", "Date Unknown")
            }else{
                intent.putExtra("dateTime", event?.strDate)
            }
            view.context.startActivity(intent)
        }
    }
}
