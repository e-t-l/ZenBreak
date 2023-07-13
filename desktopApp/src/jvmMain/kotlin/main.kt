import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import dev.giuliopime.shared.data.model.ZbSettings
import dev.giuliopime.shared.data.repository.impl.DefaultSettingsRepository
import dev.giuliopime.shared.data.source.local.OfflineSettingsStorage
import dev.giuliopime.shared.di.initKoin
import dev.giuliopime.shared.logic.BreakManager
import dev.giuliopime.shared.viewmodel.ZenBreakViewModel
import dev.giuliopime.shared_compose.ZenBreakUi
import dev.giuliopime.shared_compose.components.BreakPopup
import dev.giuliopime.shared_compose.core.toColor
import dev.giuliopime.shared_compose.theme.ZenBreakTheme
import java.awt.Dimension
import java.awt.Toolkit

private val koin = initKoin().koin


fun main() = application {
    val trayState = rememberTrayState()
    var isPopupWindowVisible by remember {
        mutableStateOf(false)
    }
    val popupWindowState = rememberWindowState(
        placement = WindowPlacement.Maximized
    )

    val breakManager: BreakManager = remember {
        DesktopBreakManager(
            breakNotification = {
                if (it.popupNotification) {
                    isPopupWindowVisible = true
                } else {
                    trayState.sendNotification(
                        Notification(
                            title = "Take a quick break",
                            message = it.breakMessage,
                            type = Notification.Type.Info
                        )
                    )
                }
            }
        )
    }

    val viewModel : ZenBreakViewModel = remember {
        ZenBreakViewModel(breakManager)
    }
    val settings = viewModel.zbSettings.collectAsState(ZbSettings())

    var isSettingsWindowVisible by remember(settings.value.hasCompletedFirstRun) {
        mutableStateOf(settings.value.hasCompletedFirstRun)
    }

    Tray(
        state = trayState,
        icon = TrayIcon,
        onAction = {
            isSettingsWindowVisible = true
        },
        menu = {
            Item(
                text = if (settings.value.enabled) "Disable" else "Enable",
                onClick = {
                    viewModel.setEnabled(!settings.value.enabled)
                }
            )

            Separator()

            Item(
                "Settings",
                onClick = {
                    isSettingsWindowVisible = true
                }
            )

            Item(
                "Quit",
                onClick = ::exitApplication
            )
        }
    )

    Window(
        title = "ZenBreak settings",
        visible = isSettingsWindowVisible,
        onCloseRequest = {
            isSettingsWindowVisible = false
        },
        icon = WindowIcon
    ) {
        ZenBreakUi(viewModel)
    }

    Window(
        visible = isPopupWindowVisible,
        onCloseRequest = {
            isPopupWindowVisible = false
            breakManager.planBreak(settings.value)
        },
        undecorated = true,
        resizable = false,
        transparent = true,
        alwaysOnTop = true,
        focusable = false,
        state = popupWindowState
    ) {
        window.toFront()
        window.requestFocus()

        AnimatedVisibility(
            visible = isPopupWindowVisible,
            enter = fadeIn(
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            ),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.5F)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BreakPopup(
                    message = settings.value.breakMessage,
                    duration = settings.value.breakDuration.copy(),
                    onSkipClicked = {
                        isPopupWindowVisible = false
                        breakManager.planBreak(settings.value)
                    },
                    onTimeFinished = {
                        isPopupWindowVisible = false
                        breakManager.planBreak(settings.value)
                    },
                    primaryColor = settings.value.primaryColor.toColor(Color.Black),
                    textColor = settings.value.textColor.toColor(Color.White)
                )
            }
        }
    }
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}

object WindowIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}