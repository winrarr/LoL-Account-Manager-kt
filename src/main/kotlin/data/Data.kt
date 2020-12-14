package data

import data.api.Account
import kotlinx.serialization.Serializable
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

@Serializable
class Data(
    var defaultPlayer: String?,
    var defaultServer: String?,
    var accounts: MutableMap<String, MutableMap<String, MutableList<Account>>>,
    var puuids: MutableList<String>
)