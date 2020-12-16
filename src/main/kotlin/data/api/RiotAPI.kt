package data.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL
import java.net.URLEncoder

object RiotAPI {
    private const val apiKey = "?api_key=RGAPI-6cb7df72-3d8f-4285-917e-3c14507306f2"
    val serverStrings = mapOf(
        "EUW" to "EUW1",
        "NA" to "NA1",
        "EUNE" to "EUN1",
        "KR" to "KR",
        "TR" to "TR1",
        "OCE" to "OC1",
        "LAN" to "LA1",
        "LAS" to "LA2",
        "RU" to "RU",
        "JP" to "JP1",
        "BR" to "BR1"
    )

    fun getAccountFromName(server: String, name: String): APIAccount {
        val url = "https://${serverStrings[server]}.api.riotgames.com/lol/summoner/v4/summoners/by-name/${URLEncoder.encode(name, "UTF-8")}$apiKey"
        val accountJson = downloadJson(url)
        return Json.decodeFromString(accountJson)
    }

    fun getAccountFromPuuId (server: String, puuId: String): APIAccount {
        val url = "https://${serverStrings[server]}.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/$puuId$apiKey"
        val accountJson = downloadJson(url)
        return Json.decodeFromString(accountJson)
    }

    fun getRankFromId (server: String, id: String): APIRank {
        val url = "https://${serverStrings[server]}.api.riotgames.com/lol/league/v4/entries/by-summoner/$id$apiKey"
        val rankJson = downloadJson(url)
        return Json.decodeFromString(rankJson.substring(1, rankJson.length - 1))
    }

    private fun downloadJson(url: String): String {
        return URL(url).readText()
    }
}