package com.iie.st10320489.marene.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.databinding.ItemTransactionBinding
import com.iie.st10320489.marene.data.entities.TransactionWithCategory

class TransactionAdapter(
    private var transactions: List<TransactionWithCategory>,
    private val onItemClick: (TransactionWithCategory) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionWithCategory) {
            val transaction = item.transaction
            val category = item.category

            binding.txtTransactionName.text = transaction.name
            binding.txtTransactionMethod.text = transaction.transactionMethod
            binding.txtTransactionDate.text = transaction.dateTime

            setTransactionAmount(transaction)

            binding.imgCategoryIcon.setImageResource(category.icon)
            binding.imgCategoryIconBackground.background.setTint(
                ContextCompat.getColor(binding.root.context, category.colour)
            )

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun setTransactionAmount(transaction: com.iie.st10320489.marene.data.entities.Transaction) {
            val context = binding.root.context
            if (transaction.expense) {
                binding.txtTransactionAmount.text = String.format("-R%.2f", transaction.amount)
                binding.txtTransactionAmount.setTextColor(
                    ContextCompat.getColor(context, R.color.outcome)
                )
            } else {
                binding.txtTransactionAmount.text = String.format("+R%.2f", transaction.amount)
                binding.txtTransactionAmount.setTextColor(
                    ContextCompat.getColor(context, R.color.income)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    fun updateTransactions(newTransactions: List<TransactionWithCategory>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
