package com.aryant.github.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aryant.github.R
import com.aryant.github.data.Repository

class SearchAdapter(private var repoList: List<Repository>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repoName: TextView = itemView.findViewById(R.id.repoName)
        val repoDescription: TextView = itemView.findViewById(R.id.repoDescription)
        val repoStars: TextView = itemView.findViewById(R.id.repoStars) // ✅ Ensure this exists in XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val repo = repoList[position]
        holder.repoName.text = repo.name
        holder.repoDescription.text = repo.description ?: "No description available"
        holder.repoStars.text = "⭐ ${repo.stargazersCount}" // ✅ Use stargazersCount (updated field name)
    }

    override fun getItemCount(): Int = repoList.size

    fun updateData(newRepos: List<Repository>) {
        repoList = newRepos
        notifyDataSetChanged()
    }
}
