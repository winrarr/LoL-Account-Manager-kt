package data

import AccountStatus
import data.api.APIAccount
import data.api.Account
import data.api.RiotAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object DataHandler {
    var data: Data
    lateinit var key: ByteArray
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

    init {
        data = try {
            val inputString = File("data.json").bufferedReader().use { it.readText() }
            Json.decodeFromString(inputString)
        } catch (e: Exception) {
            Data(null, null, mutableMapOf(), mutableListOf())
        }
    }

    fun getAccount(player: String, username: String, password: String, server: String, name: String): Account {
        val apiAccount: APIAccount = RiotAPI.getAccountFromName(server, name)
        val apiRank = RiotAPI.getRankFromId(server, apiAccount.id)
        return Account(player, username, password, server, apiAccount, apiRank)
    }

    fun getAccountList(): List<Account> = data.accounts.flatMap { it -> it.value.flatMap { it.value } }

    fun getAccountList(player: String): List<Account> = data.accounts[player]?.flatMap { it.value } ?: emptyList()

    fun getAccountList(player: String, server: String): List<Account> = data.accounts[player]?.get(server) ?: emptyList()

    fun getPlayers(): Set<String> = data.accounts.keys

    fun getServers(player: String): Set<String> = data.accounts[player]?.keys ?: emptySet()

    fun addAccount(player: String, username: String, password: String, server: String, name: String): AccountStatus {
        try {
            val account = getAccount(player, username, Encryption.encrypt(password), server, name)

            if (data.puuids.contains(account.apiAccount.puuid)) {
                return AccountStatus.ALREADY_ADDED
            }

            data.puuids.add(account.apiAccount.puuid)

            val playerAccountsMap = data.accounts.getOrPut(player) { mutableMapOf() }
            val serverAccountsList = playerAccountsMap.getOrPut(server) { mutableListOf() }
            serverAccountsList.add(account)
        } catch (e: Exception) {
            e.printStackTrace()
            return AccountStatus.FAIL
        }

        serialize()

        return AccountStatus.SUCCESS
    }

    fun removeAccount(account: Account) {
        val player = account.player
        val server = account.server

        val playerAccountsMap = data.accounts[player]!!
        val serverAccountsList = playerAccountsMap[server]!!

        serverAccountsList.remove(account)
        if (serverAccountsList.count() == 0) {
            playerAccountsMap.remove(server)
        }
        if (playerAccountsMap.count() == 0) {
            data.accounts.remove(player)
        }
        data.puuids.remove(account.apiAccount.puuid)

        serialize()
    }

    fun updateAllAccounts() = getAccountList().forEach { updateAccount(it) }

    fun updateAccount(account: Account) {
        account.apiAccount = RiotAPI.getAccountFromPuuId(account.server, account.apiAccount.puuid)
        account.apiRank = RiotAPI.getRankFromId(account.server, account.apiAccount.id)
    }

    fun serialize() {
        val json = Json.encodeToString(data)
        File("data.json").writeText(json)
    }
}