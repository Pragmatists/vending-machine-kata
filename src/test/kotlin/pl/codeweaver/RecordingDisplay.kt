package pl.codeweaver

import java.util.*

class RecordingDisplay(val messages: MutableList<String> = ArrayList<String>()) : Display, Iterable<String> by messages {

    override fun display(message: String) {
        messages += message
    }
}