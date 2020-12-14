import data.DataHandler
import javafx.scene.control.Alert
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.View
import tornadofx.alert

class AddAccountWindow : View("My View") {
    override val root: VBox by fxml()

    private val serverCmb: ComboBox<Any> by fxid("serverCmb")
    private val playerTxt: TextField by fxid("playerTxt")
    private val nameTxt: TextField by fxid("nameTxt")
    private val usernameTxt: TextField by fxid("usernameTxt")
    private val passwordTxt: TextField by fxid("passwordTxt")

    init {
        serverCmb.items.addAll("EUW", "NA", "EUNE", "KR", "TR", "OCE", "LAN", "LAS", "RU", "JP", "BR")
    }

    fun addAccount() {
        when (DataHandler.addAccount(
            playerTxt.text,
            usernameTxt.text,
            passwordTxt.text,
            "EUW1",
            nameTxt.text
        )) {
            AccountStatus.FAIL -> alert(Alert.AlertType.ERROR, "Error", "Something went wrong")
            AccountStatus.ALREADY_ADDED -> alert(Alert.AlertType.INFORMATION, "Account Already Added", "This account is already added")
            AccountStatus.SUCCESS -> {
                find<MainWindow>().updatePlayerCmb()
                close()
            }
        }
    }
}
