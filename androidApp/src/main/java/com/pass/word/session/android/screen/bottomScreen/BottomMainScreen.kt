package com.pass.word.session.android.screen.bottomScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.pass.word.session.android.R
import com.pass.word.session.android.screen.bottomScreen.addPasswordScreen.AppPasswordScreen
import com.pass.word.session.android.screen.bottomScreen.passwordScreen.PasswordScreen
import com.pass.word.session.android.screen.bottomScreen.settingsScreen.SettingsScreen
import com.pass.word.session.navigation.screen.main.bottomMain.ScreenBottomMainComponent
import com.pass.word.session.ui.CustomColor

@Composable
fun BottomMainScreen(component: ScreenBottomMainComponent) {
    val selectedItem by component.selectedItem.subscribeAsState()
    val screens by remember {
        mutableStateOf(
            listOf(
                ScreensBottom("Password", component::openPasswordScreen, false),
                ScreensBottom("Add Password", component::openAddPasswordScreen, false),
                ScreensBottom("Settings", component::openSettingsScreen, false),
            )
        )
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    screens.forEachIndexed { index, screensBottom ->
                        NavigationBarItem(
                            icon = {
                                when (screensBottom.name) {
                                    "Password" -> Icon(
                                        painter = painterResource(id = R.drawable.ic_password),
                                        contentDescription = null,
                                    )

                                    "Add Password" -> Icon(
                                        Icons.Default.Add,
                                        contentDescription = null
                                    )

                                    "Settings" -> Icon(
                                        Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                }
                            },
                            label = { Text(text = screensBottom.name) },
                            selected = selectedItem == index,
                            onClick = {
                                component.updateSelectedItem(index)
                                screensBottom.openScreen()
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CustomColor().brandBlueLight,
                                unselectedTextColor = CustomColor().grayLight,
                                selectedTextColor = CustomColor().brandBlueLight,
                                unselectedIconColor = CustomColor().grayLight,
                                indicatorColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
            )
        },
        content = { innerpadding ->
            Column(modifier = Modifier.padding(innerpadding)) {
                Children(
                    stack = component.childStack,
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    animation = stackAnimation(fade() + scale()),
                ) { child ->
                    when (val instance = child.instance) {
                        is ScreenBottomMainComponent.Child.ScreenPassword -> PasswordScreen(instance.component)
                        is ScreenBottomMainComponent.Child.ScreenAddPassword -> AppPasswordScreen(
                            instance.component
                        )

                        is ScreenBottomMainComponent.Child.ScreenSettings -> SettingsScreen(instance.component)
                    }
                }
            }
        }
    )
}

data class ScreensBottom(val name: String, val openScreen: () -> Unit, val isSelected: Boolean)