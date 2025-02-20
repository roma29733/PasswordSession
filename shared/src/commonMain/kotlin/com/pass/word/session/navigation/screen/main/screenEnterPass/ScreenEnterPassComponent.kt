package com.pass.word.session.navigation.screen.main.screenEnterPass

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.pass.word.session.data.keyAuthPass
import com.pass.word.session.data.putToParams
import com.pass.word.session.utilits.vibrationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScreenEnterPassComponent constructor(
    componentContext: ComponentContext,
    private val clickButtonBack: () -> Unit,
    private val navigateToNext: () -> Unit
) : ComponentContext by componentContext {

    private var _passItem = MutableValue("")
    val passItem: Value<String> = _passItem

    private var _stateEnterPass = MutableStateFlow(false)
    val stateEnterPass get() = _stateEnterPass

    private var firstEnterPass = MutableStateFlow("")

    private val listenersPassEnter = mutableListOf<(String) -> Unit>()

    fun subscribeListenerSnackBar(listener: (String) -> Unit) {
        listenersPassEnter.add(listener)
    }

    fun unsubscribeListenerSnackBar(listener: (String) -> Unit) {
        listenersPassEnter.remove(listener)
    }

    private fun callPassEnter(message: String) {
        listenersPassEnter.forEach { it.invoke(message) }
    }


    fun onEvent(event: ScreenEnterPassEvent) {
        when (event) {
            is ScreenEnterPassEvent.ClickButtonBack -> {
                clickButtonBack()
            }
            is ScreenEnterPassEvent.StateUpdatePassItem -> {
                vibrationResponse(20, event.context)
                val oldValue = passItem.value
                val newValue = oldValue + event.newCod
                _passItem.value = newValue
                if (passItem.value.length == 4) {
                    if(_stateEnterPass.value) {
                        if(_passItem.value == firstEnterPass.value) {
                            passItem.value.putToParams(keyAuthPass)
                            navigateToNext()
                        } else {
                            println("eror pass ${firstEnterPass.value}")
                            callPassEnter("Passwords don't match")
                            vibrationResponse(400, event.context)
                            _passItem.value = ""
                            _stateEnterPass.update { false }
                        }
                    } else {
                        firstEnterPass.update { passItem.value }
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(500)
                            _passItem.value = ""
                            _stateEnterPass.update { true }
                        }
                    }
                }
            }
        }
    }
}