package com.example.meuprimeiroapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meuprimeiroapp.R
import com.example.meuprimeiroapp.model.Item
import com.example.meuprimeiroapp.ui.loadUrl

/**
 * Adapter for the list of items.
 *
 * @param items The list of items to be displayed.
 * @param onItemClick The callback to be invoked when an item is clicked.
 */
class ItemAdapter(
    private val items: List<Item>,
    private val onItemClick: (Item) -> Unit,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    /**
     * ViewHolder for the item view.
     *
     * @param view The view for the item.
     */
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById<ImageView>(R.id.image)
        val fullNameTextView = view.findViewById<TextView>(R.id.name)
        val ageTextView = view.findViewById<TextView>(R.id.age)
        val professionTextView = view.findViewById<TextView>(R.id.profession)
    }

    /**
     * Creates a new ViewHolder for the item view.
     *
     * @param parent The parent view group.
     * @param viewType The type of the view.
     * @return The new ViewHolder.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    /**
     * Binds the data to the ViewHolder.
     *
     * @param holder The ViewHolder to be bound.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.fullNameTextView.text = item.value.fullName
        holder.ageTextView.text = holder.itemView.context.getString(R.string.item_age, item.value.age)
        holder.professionTextView.text = item.value.profession
        holder.imageView.loadUrl(item.value.imageUrl)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    /**
     * Returns the number of items in the list.
     *
     * @return The number of items in the list.
     */
    override fun getItemCount(): Int = items.size

}
