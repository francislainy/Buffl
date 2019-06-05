package com.francislainy.buffl.model

class Course(val courseId: String, val courseTitle: String, var mostRecent: Boolean)

class MySet(val courseId: String, val setId: String, val setTitle: String)

data class Card(var cardId: String, var setId: String, var cardQuestion: String, var cardAnswer: String, var boxNumber: Int, var guessed: Boolean = false)

class User(val uid: String?, val username: String, val profileImageUrl: String)