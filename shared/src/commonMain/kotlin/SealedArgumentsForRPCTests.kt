import kotlinx.serialization.Serializable


@Serializable
sealed class SealedArgumentsForRPCTests {
    @Serializable
    class Const(val num: Int) : SealedArgumentsForRPCTests()
}