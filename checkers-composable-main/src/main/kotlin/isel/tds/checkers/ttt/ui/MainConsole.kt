package isel.tds.checkers.ttt.ui

import isel.tds.checkers.storage.TextFileStorage
import isel.tds.checkers.ttt.model.*

fun main() {
    val storage = TextFileStorage<Name, Game>("games", GameSerializer)
    var clash = Clash(storage)
    val cmds = getCommands()
    while (true) {
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd == null) println("Unknown command $name")
        else try {
            clash = cmd.execute(args, clash)
            if (cmd.toTerminate) break
            clash.show()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println("${e.message}. Use: $name ${cmd.syntaxArgs}")
        }
    }
}
