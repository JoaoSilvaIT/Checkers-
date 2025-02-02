package isel.tds.checkers.ttt.ui

import isel.tds.checkers.ttt.model.*

class Command(
    val syntaxArgs: String = "",
    val toTerminate: Boolean = false,
    val execute: (args: List<String>, clash: Clash)-> Clash = { _, clash -> clash }
)

val playCommand = Command("<from> <to>") { args, clash ->
    val from  = requireNotNull(args.firstOrNull()) {"Missing from position"}
    val to = requireNotNull(args.getOrNull(1)) {"Missing to position"}
    return@Command clash.play(from.toSquare(), to.toSquare())
}

fun nameCmd(fx: Clash.(Name)-> Clash) = Command("<name>") { args, clash ->
    val arg = requireNotNull(args.firstOrNull()) { "Missing name" }
    clash.fx(Name(arg))
}

fun getCommands() = mapOf(
    "start" to nameCmd(Clash::joinOrStart),
    "play" to playCommand,
    "grid" to Command {_, clash -> clash.also { it.show() } },
    "refresh" to Command {_, clash -> clash.refresh() },
    "exit" to Command(toTerminate = true),
)