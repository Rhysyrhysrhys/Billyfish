data class Position(val san: String) {
    private val columnMap = hashMapOf(
        'A' to 0, 'B' to 1,
        'C' to 2, 'D' to 3,
        'E' to 4, 'F' to 5,
        'G' to 6, 'H' to 7
    )
    val coordinates: Pair<Int, Int>

    init {
        if (san.length != 2) {
            throw InvalidPositionException("SAN input not 2, length: ${san.length}")
        }
        val column = san[0].uppercaseChar()
        val row = san[1].digitToInt()

        val rank = row - 1
        val file = columnMap[column]
        if (file != null && rank >= 1) {
            this.coordinates = Pair(file, rank)
        } else {
            throw InvalidPositionException("$column, $row -> $file, $rank")
        }
    }
}

class Board(fen: FENString) {
    val state: Array<Array<Piece?>> = Array(8) { Array(8) { null } }

    init {
        var rank = 0
        var file = 0
        for (char in fen.value) {
            if (char.isDigit()) {
                file += char.digitToInt()
            } else if (char.isLetter()) {
                state[rank][file] = Piece.fromFEN[char]
                file += 1
            } else if (char == '/') {
                rank += 1
                file = 0
            }
        }
    }

    fun makeMove(source: Position, destination: Position) {
        val (x, y) = source.coordinates
        val (x1, y1) = destination.coordinates
        val sourcePiece = state[x][y]
        val destPiece = state[x1][y1]
        if (sourcePiece != null && destPiece == null) {
            state[x][y] = null
            state[x1][y1] = sourcePiece
        } else {
            throw InvalidMoveException("$sourcePiece -> $destPiece")
        }
    }
}
