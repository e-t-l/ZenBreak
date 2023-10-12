package dev.giuliopime.shared_compose_settings.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.giuliopime.shared_compose_settings.FeatureFlags
import dev.giuliopime.shared_core.data.model.ZbSettings
import dev.giuliopime.shared_core.viewmodel.IZenBreakViewModel
import dev.giuliopime.shared_compose_settings.components.BooleanSetting
import dev.giuliopime.shared_compose_settings.components.DoubleChoiceSetting
import dev.giuliopime.shared_compose_settings.components.MinuteInputSetting

@Composable
fun BehaviourPage(
    settings: ZbSettings,
    viewModel: IZenBreakViewModel,
    featureFlags: FeatureFlags
) {
    MinuteInputSetting(
        time = settings.breakFrequency,
        onTimeChange = {
            viewModel.setBreakFrequency(it)
        },
        name = "Break frequency"
    )

    Spacer(Modifier.height(16.dp))

    DoubleChoiceSetting(
        name = "Notification type",
        value = settings.popupNotification,
        onValueChange = {
            viewModel.setPopupNotification(it)
        },
        positiveName = "Popup",
        negativeName = "Notification"
    )

    Spacer(Modifier.height(16.dp))

    AnimatedVisibility(settings.popupNotification) {
        Column {
            MinuteInputSetting(
                time = settings.breakDuration,
                onTimeChange = {
                    viewModel.setBreakDuration(it)
                },
                name = "Break duration"
            )

            Spacer(Modifier.height(16.dp))
        }
    }

    BooleanSetting(
        checked = settings.breakSkip,
        onCheckedChange = {
            viewModel.setBreakSkip(it)
        },
        name = "Allow to skip break"
    )

    Spacer(Modifier.height(16.dp))

    BooleanSetting(
        checked = settings.breakSnooze,
        onCheckedChange = {
            viewModel.setBreakSnooze(it)
        },
        name = "Allow to snooze break"
    )

    Spacer(Modifier.height(8.dp))

    AnimatedVisibility(settings.breakSnooze) {
        MinuteInputSetting(
            time = settings.breakSnoozeDuration,
            onTimeChange = {
                viewModel.setBreakSnoozeDuration(it)
            },
            name = "Snooze length"
        )
    }
}