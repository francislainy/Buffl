package com.francislainy.buffl.model

class Course(val courseTitle: String) {
    constructor() : this("")
}

class Card(val courseId: String, val cardQuestion: String, val cardAnswer: String) {
    constructor() : this("", "", "")
}

class User(val uid: String?, val username: String, val profileImageUrl: String) {

    constructor() : this("", "", "")
}