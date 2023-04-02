package com.imams.opencurrency.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imams.data.model.param.CurrencyViewParam
import com.imams.opencurrency.R
import com.imams.opencurrency.databinding.ItemGridBinding

class CurrencyBoxAdapter(
    private var list: List<CurrencyViewParam>,
    private val callback: (String) -> Unit?,
): RecyclerView.Adapter<CurrencyBoxAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<CurrencyViewParam>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemGridBinding = ItemGridBinding.bind(itemView)

        fun bind(data: CurrencyViewParam) {
            binding.apply {
                textViewTitle.text = data.code
                textViewDesc.text = data.rate.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

}