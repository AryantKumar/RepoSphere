package com.aryant.github.search

import com.aryant.github.data.Repository
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items") val items: List<Repository>?
)
