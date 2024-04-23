package com.notesmakers.noteapp.presentation.notes.paintnote.models

enum class MotionEvent {
    Idle, Down, Move, Up
}

fun MotionEvent.handleMotionEvent(
    onIdle: () -> Unit = {},
    onDown: () -> Unit = {},
    onMove: () -> Unit = {},
    onUp: () -> Unit = {}
) {
    when (this) {
        MotionEvent.Idle -> onIdle()
        MotionEvent.Down -> onDown()
        MotionEvent.Move -> onMove()
        MotionEvent.Up -> onUp()
    }
}