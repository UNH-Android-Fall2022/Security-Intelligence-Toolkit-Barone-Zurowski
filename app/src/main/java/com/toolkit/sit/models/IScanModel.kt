package com.toolkit.sit.models

import com.google.firebase.Timestamp

interface IScanModel {
    val createdTime: Timestamp
    val uid: String
}