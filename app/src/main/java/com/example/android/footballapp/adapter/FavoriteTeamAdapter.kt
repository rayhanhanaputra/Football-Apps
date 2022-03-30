package com.example.android.footballapp.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.android.footballapp.FavoriteTeam
import com.example.android.footballapp.R
import com.example.android.footballapp.TeamsDetailsActivity
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find

class FavoriteTeamAdapter(private val favoriteTeams: List<FavoriteTeam>)
    : RecyclerView.Adapter<FavoriteTeamViewHolder>(){


    override fun getItemCount(): Int {
        return favoriteTeams.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTeamViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val callForRow = layoutInflater.inflate(R.layout.team_lists_row,parent,false)
        return FavoriteTeamViewHolder(callForRow)
    }

    override fun onBindViewHolder(holder: FavoriteTeamViewHolder, position: Int) {
        holder.bindItem(favoriteTeams[position])
    }

}

class FavoriteTeamViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val teamLogo: ImageView = view.find(R.id.image_badge_teams_list)
    private val teamName: TextView = view.find(R.id.team_list_text_name)

    fun bindItem(favoriteTeams: FavoriteTeam){

        Picasso.get().load(favoriteTeams.strTeamBadge).into(teamLogo)
        teamName.text = favoriteTeams.strTeam

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, TeamsDetailsActivity::class.java)
            intent.putExtra("strTeam", favoriteTeams.strTeam)
            intent.putExtra("teamBadgeLink", favoriteTeams.strTeamBadge)
            intent.putExtra("intFormedYearTeam", favoriteTeams.intFormedYear)
            intent.putExtra("strStadium", favoriteTeams.strStadium)
            intent.putExtra("strDescription", favoriteTeams.strDescriptionEN)
            intent.putExtra("idTeamClicked", favoriteTeams.idTeam)
            itemView.context.startActivity(intent)
        }
    }
}