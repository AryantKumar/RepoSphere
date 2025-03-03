package com.aryant.github.main

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aryant.github.R
import com.aryant.github.data.Repository

class RepoAdapter(private var repoList: List<Repository>) :
    RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val repoName: TextView = view.findViewById(R.id.repoName)
        val repoDescription: TextView = view.findViewById(R.id.repoDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = repoList[position]
        holder.repoName.text = repo.name
        holder.repoDescription.text = repo.description ?: "No description available"

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = repoList.size

    fun updateList(newRepoList: List<Repository>) {
        repoList = newRepoList
        notifyDataSetChanged()
    }
}
