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

class DiscountAdapter (private val discountList: List<Discount>
) : RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>() {

    class DiscountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val disLogo: ImageView = itemView.findViewById(R.id.discImg)
        val tvDiscName: TextView = itemView.findViewById(R.id.txtDiscName)
        val tvDiscountDetails: TextView = itemView.findViewById(R.id.tvDiscDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discount, parent, false)
        return DiscountViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        val discount = discountList[position]
        holder.tvDiscName.text = discount.disName
        holder.tvDiscountDetails.text = discount.details
        holder.disLogo.setImageResource(discount.disImageResId)

        // Handle item click events to launch the detail activity
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("discount_image", discount.disImageResId)
            holder.itemView.findNavController().navigate(R.id.navigation_rewards_discountdetail, bundle)

        }
    }

    override fun getItemCount(): Int = discountList.size
}
