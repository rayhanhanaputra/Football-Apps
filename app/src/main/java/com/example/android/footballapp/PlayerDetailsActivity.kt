package com.example.android.footballapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.player_details_layout.*

class PlayerDetailsActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_details_layout)

        val playerHeight = intent.getStringExtra("strHeightPlayer")
        val playerWeight = intent.getStringExtra("strWeightPlayer")
        val playerPosition = intent.getStringExtra("strPositionPlayer")
        val playerDescription = intent.getStringExtra("strDescriptionPlayer")
        val playerName = intent.getStringExtra("strPlayerName")
        val strThumbnailLink = intent.getStringExtra("strThumbnail")

        Picasso.get().load(strThumbnailLink).into(player_thumbnail_image)
        player_name_details.text = playerName
        player_position_details.text = playerPosition
        player_height_details.text = playerHeight
        player_weight_details.text = playerWeight
        player_details_text_description.text = playerDescription
    }

}