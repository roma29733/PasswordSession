package com.pass.word.session.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.pass.word.session.navigation.screen.main.initialGreeting.InitialGreetingRootComponent

@Composable
fun InitialGreetingScreen(component: InitialGreetingRootComponent) {

    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
//        when (val instance = child.instance) {
//            is InitialGreetingRootComponent.Child.ScreenFirstInitial -> FirstInitialScreen(
//                component = instance.component
//            )
//
//            is InitialGreetingRootComponent.Child.ScreenEnterInitialPassAuth -> EnterPassScreen(
//                component = instance.component
//            )
//
//            is InitialGreetingRootComponent.Child.ScreenSecondInitial -> SecondInitialScreen(
//                component = instance.component
//            )
//
//            is InitialGreetingRootComponent.Child.ScreenImportPassword -> ImportPasswordScreen(
//                component = instance.component,
//            )
//        }
    }

}