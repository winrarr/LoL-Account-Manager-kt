import com.sun.javafx.collections.ObservableListWrapper
import data.DataHandler
import data.Encryption
import data.api.Account
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.binding.ListBinding
import javafx.beans.property.ListProperty
import javafx.beans.value.ObservableListValue
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.layout.VBox
import javafx.util.Duration
import tornadofx.*
import kotlin.math.roundToInt


class MainWindow : View() {
    override val root: VBox by fxml()

    private val playerCmb: ComboBox<String> by fxid("playerCmb")
    private val serverCmb: ComboBox<String> by fxid("serverCmb")
    private val showPassChk: CheckBox by fxid("showPassChk")
    private val accountsLst: ListView<Any> by fxid("accountsLst")
    private val usernameBtn: Button by fxid("usernameBtn")
    private val usernameTxt: TextField by fxid("usernameTxt")
    private val passwordBtn: Button by fxid("passwordBtn")
    private val passwordTxt: TextField by fxid("passwordTxt")
    private val passwordPsw: PasswordField by fxid("passwordPsw")
    private val rankLbl: Label by fxid("rankLbl")
    private val winrateLbl: Label by fxid("winrateLbl")

    init {
        setUp()
    }

    private fun setUp() {
        updatePlayerCmb()
    }

    fun updatePlayerCmb() {
        playerCmb.items .clear()
        playerCmb.items.addAll(DataHandler.getPlayers())
    }

    fun updateServerCmb() {
        serverCmb.items.clear()
        playerCmb.value?.let {
            serverCmb.items.addAll(DataHandler.getServers(it))
        }
    }

    fun updateListView() {
        accountsLst.items.clear()
        val player = playerCmb.value ?: return
        val server = serverCmb.value ?: return
        accountsLst.items.addAll(elements = DataHandler.getAccountList(player, server))
    }

    fun addAccount() {
        find<AddAccountWindow>().openWindow()
    }

    fun showPassword() {
        if (showPassChk.isSelected) {
            passwordTxt.isVisible = true
            passwordPsw.isVisible = false
        } else {
            passwordTxt.isVisible = false
            passwordPsw.isVisible = true
        }
    }

    fun accountSelected() {
        val account = (accountsLst.selectedItem ?: return) as Account

        usernameTxt.text = account.username
        val password = Encryption.decrypt(account.password)
        passwordTxt.text = password
        passwordPsw.text = password

        rankLbl.text = account.apiRank.tier + " " + account.apiRank.rank + " (" + account.apiRank.leaguePoints + ")"
        winrateLbl.text = (account.apiRank.wins.toDouble() / (account.apiRank.wins + account.apiRank.losses).toDouble() * 100.toDouble()).round().toString() + " %"
    }

    private fun Double.round(): Double {
        return (this * 100.toDouble()).roundToInt() / 100.toDouble()
    }

    fun copyUsername() {
        copyFromTextfield(usernameTxt)
        showCopiedOnButton(usernameBtn)
    }

    fun copyPassword() {
        copyFromTextfield(passwordTxt)
        showCopiedOnButton(passwordBtn)
    }

    private fun copyFromTextfield(textfield: TextField) {
        val clipboard = Clipboard.getSystemClipboard()
        val content = ClipboardContent()
        content.putString(textfield.text)
        clipboard.setContent(content)
    }

    private fun showCopiedOnButton(button: Button) {
        button.text = "Copied"
        val timeline = Timeline(
            KeyFrame(
                Duration.millis(1500.0),
                { button.text = "Username:" })
        )
        timeline.play()
    }

    fun updateSelectedAccount() {
        val account = (accountsLst.selectedItem ?: return) as Account
        DataHandler.updateAccount(account)
        updateListView()
    }

    fun removeSelectedAccount() {
        val account = (accountsLst.selectedItem ?: return) as Account
        DataHandler.removeAccount(account)
        updatePlayerCmb()
    }

    fun updateAllAccounts() {
        DataHandler.updateAllAccounts()
        updateListView()
    }
}
