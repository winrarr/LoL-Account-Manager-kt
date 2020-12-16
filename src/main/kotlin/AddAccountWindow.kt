import data.DataHandler
import data.api.RiotAPI
import javafx.scene.control.Alert
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.View
import tornadofx.alert

class AddAccountWindow : View("My View") {
    override val root: VBox by fxml()

    private val mainWindow = find<MainWindow>()

    private val serverCmb: ComboBox<Any> by fxid("serverCmb")
    private val playerTxt: TextField by fxid("playerTxt")
    private val nameTxt: TextField by fxid("nameTxt")
    private val usernameTxt: TextField by fxid("usernameTxt")
    private val passwordTxt: TextField by fxid("passwordTxt")

    init {
        serverCmb.items.addAll(elements = RiotAPI.serverStrings.keys)
        serverCmb.selectionModel.select(mainWindow.getSelectedServer())
    }

    fun addAccount() {
        when (DataHandler.addAccount(
            playerTxt.text,
            usernameTxt.text,
            passwordTxt.text,
            serverCmb.value as String,
            nameTxt.text
        )) {
            AccountStatus.FAIL -> alert(Alert.AlertType.ERROR, "Error", "Something went wrong")
            AccountStatus.ALREADY_ADDED -> alert(Alert.AlertType.INFORMATION, "Account Already Added", "This account is already added")
            AccountStatus.SUCCESS -> {
                mainWindow.updatePlayerCmb()
                usernameTxt.text = ""
                passwordTxt.text = ""
                close()
            }
        }
    }
}
