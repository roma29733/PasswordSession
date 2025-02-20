package com.pass.word.session.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.pass.word.session.data.getParamsBoolean
import com.pass.word.session.data.getParamsString
import com.pass.word.session.data.keyAuthPass
import com.pass.word.session.data.model.PasswordItemModel
import com.pass.word.session.navigation.screen.main.authentication.ScreenAuthenticationComponent
import com.pass.word.session.navigation.screen.main.bottomMain.ScreenBottomMainComponent
import com.pass.word.session.navigation.screen.main.changePassword.ChangePasswordRootComponent
import com.pass.word.session.navigation.screen.main.detail.ScreenDetailComponent
import com.pass.word.session.navigation.screen.main.edit.ScreenEditComponent
import com.pass.word.session.navigation.screen.main.initialGreeting.InitialGreetingRootComponent
import com.pass.word.session.navigation.screen.main.initialGreeting.screenImportPassword.ScreenImportPasswordComponent
import com.pass.word.session.utilits.getThisLocalTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.serialization.Serializable

class RootComponent constructor(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = if (getStateAuthParams()) Configuration.ScreenAuthentication else Configuration.ScreenInitialGreeting,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun getStateAuthParams(): Boolean {
        getThisLocalTime()
        val itemPass = getParamsString(keyAuthPass)
        return itemPass?.isNotEmpty() ?: false
    }

    @OptIn(ExperimentalDecomposeApi::class, DelicateCoroutinesApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when (config) {
            Configuration.ScreenBottomMain -> Child.ScreenBottomMain(
                ScreenBottomMainComponent(
                    componentContext = context,
                    onNavigateToDetailComponent = { model ->
                        navigation.pushNew(Configuration.ScreenDetail(model))
                    },
                    onNavigateToChangePasswordComponent = {
                        navigation.pushNew(Configuration.ScreenChangePasswordRootComponent)
                    },
                    onNavigateToImportPasswordComponent = {
                        navigation.pushNew(Configuration.ScreenImportPassword)
                    }
                )
            )

            is Configuration.ScreenDetail -> Child.ScreenDetail(
                ScreenDetailComponent(
                    componentContext = context,
                    passDetailModel = config.passDetailModel,
                    onGoBack = {
                        navigation.pop()
                    },
                    onGoEditScreen = { navigation.pushNew(Configuration.ScreenEdit(it)) }
                )
            )

            is Configuration.ScreenAuthentication -> Child.ScreenAuthentication(
                ScreenAuthenticationComponent(
                    componentContext = context,
                    onNavigateToMainScreen = {
                        navigation.replaceCurrent(Configuration.ScreenBottomMain)
                    }
                )
            )

            is Configuration.ScreenEdit -> Child.ScreenEdit(
                ScreenEditComponent(
                    componentContext = context,
                    passDetailModel = config.passDetailModel,
                    onGoBack = { navigation.pop() }
                )
            )

            is Configuration.ScreenInitialGreeting -> Child.ScreenInitialGreeting(
                InitialGreetingRootComponent(
                    componentContext = context,
                    navigateToAuthScreen = {
                        navigation.replaceCurrent(Configuration.ScreenAuthentication)
                    }
                )
            )

            is Configuration.ScreenChangePasswordRootComponent -> Child.ScreenChangePasswordRootComponent(
                ChangePasswordRootComponent(
                    componentContext = context,
                    onBackNavigation = { navigation.pop() },
                    onNextNavigation = {
                        navigation.replaceAll(Configuration.ScreenAuthentication)
                    }
                )
            )

            is Configuration.ScreenImportPassword -> Child.ScreenImportPassword(
                ScreenImportPasswordComponent(
                    componentContext = context,
                    onNextScreen = { navigation.replaceAll(Configuration.ScreenBottomMain) },
                    onBackHandler = { navigation.pop() }
                )
            )
        }
    }

    sealed class Child {
        data class ScreenBottomMain(val component: ScreenBottomMainComponent) : Child()
        data class ScreenDetail(val component: ScreenDetailComponent) : Child()
        data class ScreenAuthentication @OptIn(DelicateCoroutinesApi::class) constructor(val component: ScreenAuthenticationComponent) :
            Child()

        data class ScreenEdit(val component: ScreenEditComponent) : Child()
        data class ScreenInitialGreeting(val component: InitialGreetingRootComponent) : Child()
        data class ScreenChangePasswordRootComponent(val component: ChangePasswordRootComponent) :
            Child()

        data class ScreenImportPassword(val component: ScreenImportPasswordComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object ScreenBottomMain : Configuration()

        @Serializable
        data class ScreenDetail(val passDetailModel: PasswordItemModel) : Configuration()

        @Serializable
        data object ScreenAuthentication : Configuration()

        @Serializable
        data class ScreenEdit(val passDetailModel: PasswordItemModel) : Configuration()

        @Serializable
        data object ScreenInitialGreeting : Configuration()

        @Serializable
        data object ScreenChangePasswordRootComponent : Configuration()

        @Serializable
        data object ScreenImportPassword : Configuration()
    }
}