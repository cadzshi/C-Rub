package com.bangkit.capstoneproject.cleanrubbish.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstoneproject.cleanrubbish.data.local.Article
import com.bangkit.capstoneproject.cleanrubbish.databinding.ItemArticleBinding
import com.bangkit.capstoneproject.cleanrubbish.ui.detail.DetailArticleActivity
import com.bangkit.capstoneproject.cleanrubbish.ui.home.HomeFragment.Companion.KEY_DETAIL

class ArticleAdapter(private val listArticle: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val (title, _, photo) = listArticle[position]
        holder.binding.ivArticlePhoto.setImageResource(photo)
        holder.binding.tvArticleTitle.text = title


        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailArticleActivity::class.java)
            intentDetail.putExtra(KEY_DETAIL, listArticle[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listArticle.size

    class ListViewHolder(var binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)


}