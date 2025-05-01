package com.iie.st10320489.marene.ui.rewards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.iie.st10320489.marene.R

class ClaimsAdapter (private val claimList: List<ClaimItem>) :
RecyclerView.Adapter<ClaimsAdapter.ClaimViewHolder>() {

    inner class ClaimViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageClaim: ImageView = itemView.findViewById(R.id.ClaimImage)
        val claimTitle: TextView = itemView.findViewById(R.id.ClaimRewardTitle)
        val claimPoints: TextView = itemView.findViewById(R.id.ClaimRewardPoints)

        init {
            // the click listener opens the ClaimDetailActivity page
            itemView.setOnClickListener {
                val claim = claimList[adapterPosition]

                // A bundle value is made to pass the image resource ID to the ClaimDetailActivity fragment
                val bundle = Bundle().apply {
                    putInt("IMAGE_RES_ID", claim.clmImageResId)
                    putString("TITLE", claim.clmTitle)
                    putString("POINTS", claim.clmPoints)
                }

                // Find the claim detail page via navigation
                itemView.findNavController().navigate(R.id.navigation_rewards_claimdetail, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val viewClaim = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward_claim, parent, false)
        return ClaimViewHolder(viewClaim)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        val claim = claimList[position]
        holder.imageClaim.setImageResource(claim.clmImageResId)
        holder.claimTitle.text = claim.clmTitle
        holder.claimPoints.text = claim.clmPoints
    }

    override fun getItemCount(): Int = claimList.size
}