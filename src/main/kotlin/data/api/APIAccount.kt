package data.api

import kotlinx.serialization.Serializable

@Serializable
data class APIAccount(
    val id: String,
    val accountId: String,
    val puuid: String,
    val name: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Long
)


