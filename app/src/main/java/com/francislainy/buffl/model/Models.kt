package com.francislainy.buffl.model

class Course(val courseId: String, val courseTitle: String)

data class Card(var courseId: String, var cardQuestion: String, var cardAnswer: String, var cardPosition: Int)

data class Cards(val cards: List<Card>)

class User(val uid: String?, val username: String, val profileImageUrl: String)