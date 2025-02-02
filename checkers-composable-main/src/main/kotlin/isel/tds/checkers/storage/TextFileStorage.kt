package isel.tds.checkers.storage

import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.io.path.*

/**
 * Storage system based on text files.
 * Each entry corresponds to a text file.
 * The key is the file name with ".txt" extension.
 * The value is the contents of the file serialized to text.
 * @param baseFolderName the name of the folder where the files are stored.
 * @param serializer the serializer to convert data to text and vice-versa.
 */
class TextFileStorage<Key, Data : Any>(
    baseFolderName: String,
    private val serializer: Serializer<Data>
) : Storage<Key, Data> {
    private val basePath = Path(baseFolderName)

    // Create base folder if it does not exist
    init {
        with(basePath) {
            if (!exists()) createDirectory()
            else check(isDirectory()) { "$name is not a directory" }
        }
    }

    private inline fun <T> with(key: Key, fx: Path.() -> T): T =
        (basePath / "$key.txt").fx()

    override fun create(key: Key, data: Data) = with(key) {
        check(!exists()) { "File $key exists" }
        writeText(serializer.serialize(data))
    }

    override fun read(key: Key): Data? = with(key) {
        try {
            serializer.deserialize(readText())
        } catch (e: NoSuchFileException) {
            null
        }
    }

    override fun update(key: Key, data: Data) = with(key) {
        check(exists()) { "File $key does not exist" }
        writeText(serializer.serialize(data))
    }

    override fun delete(key: Key) = with(key) {
        check(deleteIfExists()) { "File $key does not exist" }
    }
}