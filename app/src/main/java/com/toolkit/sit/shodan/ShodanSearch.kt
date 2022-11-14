package com.toolkit.sit.shodan

// Each query results in multiple matches.
// This contains each match.
data class ShodanSearch(
    val matches: List<ShodanMatches>,
    val total: Int
)
