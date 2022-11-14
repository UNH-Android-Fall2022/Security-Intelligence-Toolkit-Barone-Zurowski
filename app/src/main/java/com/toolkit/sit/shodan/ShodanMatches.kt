package com.toolkit.sit.shodan

// Each API Query will result in multiple matches
// Each Match is this model.
data class ShodanMatches(
    val votes: String?,
    val description: String?,
    val title: String?,
    val timestamp: String?,
    val tags: List<String>?,
    val query: String?,
    val count: Int?,
    val value: String?
)