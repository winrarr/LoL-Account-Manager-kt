package data

import data.api.Account
import kotlinx.serialization.Serializable

@Serializable
class Data(
    var defaultPlayer: String?,
    var defaultServer: String?,
    var accounts: MutableMap<String, MutableMap<String, MutableList<Account>>>,
    var puuids: MutableList<String>
)