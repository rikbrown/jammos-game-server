package net.jammos.gameserver

data class Position(val x: Float, val y: Float, val z: Float) {
    companion object {
        val ZERO = Position(0F, 0F, 0F)
    }
}