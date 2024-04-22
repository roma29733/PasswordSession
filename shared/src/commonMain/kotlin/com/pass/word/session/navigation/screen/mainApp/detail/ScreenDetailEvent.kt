package com.pass.word.session.navigation.screen.mainApp.detail

import com.pass.word.session.data.DriverFactory

interface ScreenDetailEvent {
    data object ClickButtonBack: ScreenDetailEvent
    data class ShowToast(val message: String): ScreenDetailEvent
    data class ChangeStateOpenedAlertDialog(val newState: Boolean) : ScreenDetailEvent
    data class DeleteItemPass(val databaseDriverFactory: DriverFactory): ScreenDetailEvent
    data object EditItemPass: ScreenDetailEvent
    data object HideDialog: ScreenDetailEvent
}