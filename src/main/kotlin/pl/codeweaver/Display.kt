package pl.codeweaver

interface Display {
    fun display(message: String)
}

class ConsoleDisplay : Display {
    override fun display(message: String) = print(message)
}