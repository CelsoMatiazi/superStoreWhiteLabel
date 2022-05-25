package com.crsm.whitelabelstore.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crsm.whitelabelstore.R
import com.crsm.whitelabelstore.domain.model.Product
import com.crsm.whitelabelstore.util.toCurrency


class ProductsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffUtil = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(inflater.inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ProductViewHolder -> holder.bind(diffUtil.currentList[position])
        }
    }

    override fun getItemCount(): Int = diffUtil.currentList.size

    fun updateList(products: List<Product>){
        diffUtil.submitList(products)
    }

    fun getCurrentList(): List<Product>{
        return diffUtil.currentList
    }


    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Product>(){

            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

}


class ProductViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val image : ImageView = view.findViewById(R.id.image_product)
    private val description : TextView = view.findViewById(R.id.text_description)
    private val price : TextView = view.findViewById(R.id.text_price)

    fun bind(product: Product){
        Glide.with(image.context).load(product.imageUrl).fitCenter().into(image)
        description.text = product.description
        price.text = product.price.toCurrency()
    }

}