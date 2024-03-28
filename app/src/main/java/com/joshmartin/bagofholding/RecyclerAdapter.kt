package com.joshmartin.bagofholding

import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//Creating a custom adapter for an arraylist of Item objects
class RecyclerAdapter(val itemList: ArrayList<Item>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //Initialize itemClicked as null until an item has been clicked
    var itemClicked: Item? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView
        var itemWeight: TextView
        var itemPrice: TextView

        init {
            itemName = itemView.findViewById(R.id.itemNameText)
            itemWeight = itemView.findViewById(R.id.ItemWeightText)
            itemPrice = itemView.findViewById(R.id.ItemCostText)

            //Set the onClickListener which will initialize the itemClicked variable
            itemView.setOnClickListener { v: View ->
                itemClicked = itemList[adapterPosition]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = itemList[position].name
        holder.itemWeight.text = itemList[position].weight.toString() + " lbs"
        holder.itemPrice.text = itemList[position].cost
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //Function to get the itemClicked variable to add it to inBagList arrayList in BagViewModel from MainFragment
    fun getItem(): Item? {
        if (itemClicked != null) { //If itemClicked is not null, return it as an Item. Else return null.
            return itemClicked as Item
        } else
            return null
    }
}