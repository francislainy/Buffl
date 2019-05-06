package com.francislainy.buffl.model

class Course(val courseTitle: String) {
    constructor() : this("")
}

data class Card(var courseId: String, var cardQuestion: String, var cardAnswer: String, var cardPosition: Int) {
    constructor() : this("", "", "", -1)
}

data class Cards(val cards: List<Card>)

class User(val uid: String?, val username: String, val profileImageUrl: String) {

    constructor() : this("", "", "")
}