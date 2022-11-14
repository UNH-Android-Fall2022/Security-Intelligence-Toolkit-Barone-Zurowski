package com.toolkit.sit.shodan

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