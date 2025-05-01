package com.iie.st10320489.marene.ui.rewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iie.st10320489.marene.R

class RewardsAdapter (private val rewardList: List<RewardItem>) :
    RecyclerView.Adapter<RewardsAdapter.RewardViewHolder>() {

    inner class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageReward: ImageView = itemView.findViewById(R.id.ImageReward)
        val textTitle: TextView = itemView.findViewById(R.id.textRewardTitle)
        val textPoints: TextView = itemView.findViewById(R.id.textRewardPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewardList[position]
        holder.imageReward.setImageResource(reward.imageResId)
        holder.textTitle.text = reward.title
        holder.textPoints.text = reward.points
    }

    override fun getItemCount(): Int = rewardList.size

}