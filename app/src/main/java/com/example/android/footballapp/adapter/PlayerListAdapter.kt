package com.example.android.footballapp.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.footballapp.Player
import com.example.android.footballapp.PlayerDetailsActivity
import com.example.android.footballapp.PlayerList
import com.example.android.footballapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.team_lists_row.view.*

class PlayerListAdapter(private val playerList: PlayerList): RecyclerView.Adapter<CustomViewHolderPlayerList>(){

    override fun getItemCount(): Int {
        return playerList.player.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolderPlayerList, position: Int) {
        val player = playerList.player[position]
        Picasso.get().load(player.strCutout).into(holder.view.image_badge_teams_list)
        holder.view.team_list_text_name?.text = player.strPlayer
        holder.player = player
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderPlayerList {
        val layoutInflater = LayoutInflater.from(parent.context)
        val callForRow = layoutInflater.inflate(R.layout.team_lists_row,parent,false)
        return CustomViewHolderPlayerList(callForRow, null)
    }
}

class CustomViewHolderPlayerList(val view : View, var player: Player?): RecyclerView.ViewHolder(view){
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, PlayerDetailsActivity::class.java)
            intent.putExtra("strThumbnail", player?.strThumb)
            intent.putExtra("strHeightPlayer", player?.strHeight)
            intent.putExtra("strWeightPlayer", player?.strWeight)
            intent.putExtra("strPositionPlayer", player?.strPosition)
            intent.putExtra("strDescriptionPlayer", player?.strDescriptionEN)
            intent.putExtra("strPlayerName", player?.strPlayer)
            view.context.startActivity(intent)
        }
    }
}