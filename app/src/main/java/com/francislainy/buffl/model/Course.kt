package com.francislainy.buffl.model

class Course(val courseTitle: String) {
    constructor() : this("")
}

data class Card(var courseId: String, var cardQuestion: String, var cardAnswer: String) {
    constructor() : this("", "", "")
}

class User(val uid: String?, val username: String, val profileImageUrl: String) {

    constructor() : this("", "", "")
}