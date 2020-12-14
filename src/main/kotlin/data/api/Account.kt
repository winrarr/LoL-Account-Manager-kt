package data.api

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    var player: String,
    var username: String,
    var password: String,
    var server: String,
    var apiAccount: APIAccount,
    var apiRank: APIRank
) {
    override fun toString(): String {
        return apiAccount.name
    }
}


