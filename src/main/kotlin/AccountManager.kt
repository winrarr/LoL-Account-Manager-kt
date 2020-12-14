import data.Data
import data.DataHandler
import data.api.APIRank
import javafx.stage.Stage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.importStylesheet
import java.io.File
import kotlin.reflect.KClass

class AccountManager: App() {
    override lateinit var primaryView: KClass<out UIComponent>

    override fun start(stage: Stage) {
        primaryView = if (File("password").exists()) LoginWindow::class else SetLoginWindow::class
        importStylesheet("/Styles.css")
        super.start(stage)
    }
}