package com.example.android.footballapp.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.footballapp.R
import com.example.android.footballapp.TeamList
import com.example.android.footballapp.Teams
import com.example.android.footballapp.TeamsDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.team_lists_row.view.*

class TeamListSearchAdapter(private val teamLists: TeamList): RecyclerView.Adapter<CustomViewHolderTeamSearch>(){
    override fun getItemCount(): Int {
        return when(teamLists.teams.size){
            null -> {0}
            else -> {teamLists.teams.size}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderTeamSearch {
        val layoutInflater = LayoutInflater.from(parent.context)
        val callForRow = layoutInflater.inflate(R.layout.team_lists_row,parent,false)
        return CustomViewHolderTeamSearch(callForRow, null)
    }

    override fun onBindViewHolder(holder: CustomViewHolderTeamSearch, position: Int) {
        val teams = teamLists.teams[position]
        Picasso.get().load(teams.strTeamBadge).into(holder.view.image_badge_teams_list)
        holder.view.team_list_text_name?.text = teams.strTeam
        holder.teams = teams
    }
}
class CustomViewHolderTeamSearch(val view: View, var teams: Teams?): RecyclerView.ViewHolder(view){
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, TeamsDetailsActivity::class.java)
            intent.putExtra("strTeam", teams?.strTeam)
            intent.putExtra("teamBadgeLink", teams?.strTeamBadge)
            intent.putExtra("intFormedYearTeam", teams?.intFormedYear)
            intent.putExtra("strStadium", teams?.strStadium)
            intent.putExtra("strDescription", teams?.strDescriptionEN)
            intent.putExtra("idTeamClicked", teams?.idTeam)
            view.context.startActivity(intent)
        }
    }
}