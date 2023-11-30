package com.example.springkafka

data class Message(val id: Int, val message: String, val author: String) {
    constructor(): this(0,"","")
}