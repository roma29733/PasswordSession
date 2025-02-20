package com.pass.word.session.android.screen.initialGreeting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.pass.word.session.android.screen.enterPassScreen.EnterPassScreen
import com.pass.word.session.navigation.screen.main.initialGreeting.InitialGreetingRootComponent

@Composable
fun InitialGreetingScreen(component: InitialGreetingRootComponent) {

    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is InitialGreetingRootComponent.Child.ScreenFirstInitial -> FirstInitialScreen(
                component = instance.component
            )

            is InitialGreetingRootComponent.Child.ScreenEnterInitialPassAuth -> EnterPassScreen(
                component = instance.component
            )

            is InitialGreetingRootComponent.Child.ScreenSecondInitial -> SecondInitialScreen(
                component = instance.component
            )

            is InitialGreetingRootComponent.Child.ScreenImportPassword -> ImportPasswordScreen(
                component = instance.component,
            )
        }
    }

}
