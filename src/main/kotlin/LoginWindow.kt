import data.DataHandler
import data.Encryption
import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.layout.VBox
import tornadofx.View
import tornadofx.alert
import java.io.File

class LoginWindow : View() {
    override val root: VBox by fxml()

    private val passwordPsw: PasswordField by fxid("passwordPsw")

    fun login() {
        val storedPassword = File("password").bufferedReader().readText()
        val hashedEnteredPassword = Encryption.hash(passwordPsw.text)

        if (Encryption.hash(hashedEnteredPassword) == storedPassword) {
            DataHandler.key = Encryption.hexStringToByteArray(hashedEnteredPassword)
            find<MainWindow>().openWindow(owner = null)
            close()
        } else {
            alert(Alert.AlertType.WARNING, "Wrong password")
        }
    }
}
