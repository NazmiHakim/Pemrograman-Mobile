package com.example.listxml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.databinding.ItemListBinding

class ItemAdapter(
    private val items: List<Item>,
    private val onOpenClick: (String) -> Unit,
    private val onDetailClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            itemImage.setImageResource(item.imageResId)
            titleText.text = item.title
            subtitleText.text = item.author

            openButton.setOnClickListener {
                onOpenClick(item.url)
            }
            detailButton.setOnClickListener {
                onDetailClick(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}