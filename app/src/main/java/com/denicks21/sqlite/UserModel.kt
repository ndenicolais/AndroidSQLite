package com.denicks21.sqlite

import java.util.*

data class UserModel (
    var id: Int = getAutoId(),
    var name: String = "",
    var city: String = ""
) {
    companion object {
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}