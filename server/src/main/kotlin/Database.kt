import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.FileNotFoundException
import java.util.*


val properties = Properties().apply {
        load(Resources.resolve("local.properties")?.openStream() ?: throw FileNotFoundException())
}

fun <T> database(statement: Transaction.() -> T): T {
    Database.connect(
        url = "jdbc:postgresql://localhost/electa",
        driver = "org.postgresql.Driver",
        user = properties["user"] as? String ?: error( "invalid postgres user"),
        password = properties["password"] as? String ?: error( "invalid postgres password")
    )

    return transaction {
        addLogger(StdOutSqlLogger)
        statement()
    }
}

