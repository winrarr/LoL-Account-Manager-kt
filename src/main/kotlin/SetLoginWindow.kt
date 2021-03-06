import data.DataHandler
import data.Encryption
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.View
import java.io.File

class SetLoginWindow : View("My View") {
    override val root: VBox by fxml()

    private val passwordTxt: TextField by fxid("passwordTxt")

    fun setPassword() {
        val hashedPassword = Encryption.hash(passwordTxt.text)
        File("password").writeText(Encryption.hash(hashedPassword))

        DataHandler.key = Encryption.hexStringToByteArray(hashedPassword)
        find<MainWindow>().openWindow(owner = null)
        close()
    }
}
