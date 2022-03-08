import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import utils.Resources
import java.io.FileNotFoundException
import java.util.*


val properties = Properties().apply {
    load(Resources.resolve("local.properties")?.openStream() ?: throw FileNotFoundException())
}

fun connectToDatabase(){
    Database.connect(
        url = "jdbc:postgresql://localhost/electa",
        driver = "org.postgresql.Driver",
        user = properties["user"] as? String ?: error("invalid postgres user"),
        password = properties["password"] as? String ?: error("invalid postgres password")
    )
}

fun <T> transactionWithLogger(statement: Transaction.() -> T): T {
    return transaction {
        addLogger(StdOutSqlLogger)
        statement()
    }
}