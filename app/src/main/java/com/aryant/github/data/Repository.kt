package com.aryant.github.data

import com.google.gson.annotations.SerializedName

data class Repository(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val url: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("language") val language: String?,  // ✅ Add this field
    @SerializedName("visibility") val type: String?    // ✅ GitHub uses "visibility" instead of "type"
)
