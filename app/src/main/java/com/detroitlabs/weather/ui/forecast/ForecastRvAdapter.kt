package com.detroitlabs.weather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.detroitlabs.weather.databinding.ItemForecastBinding

class ForecastRvAdapter : RecyclerView.Adapter<ForecastRvAdapter.ForecastRvViewHolder>() {
    private var items: List<ForecastItemDisplay> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastRvViewHolder {
        val binding =
            ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastRvViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastRvViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<ForecastItemDisplay>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ForecastRvViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastItemDisplay) {
            binding.display = item
        }
    }
}