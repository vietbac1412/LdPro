package tamhoang.ldpro4.ui.custom.customkeyboard.components.keyboard

import tamhoang.ldpro4.ui.custom.customkeyboard.components.keyboard.controllers.KeyboardController

interface KeyboardListener {
    fun characterClicked(c: Char)
    fun specialKeyClicked(key: KeyboardController.SpecialKey)
}