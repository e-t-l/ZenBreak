package dev.giuliopime.shared_core.core

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesIgnore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSettingsApi::class)
@NativeCoroutinesIgnore
inline fun <reified T> FlowSettings.decodeValueFlow(
    key: String,
    defaultValue: T
): Flow<T> =  getStringOrNullFlow(key).map {
    it?.let {
        JsonSerialization.json.decodeFromString<T>(it)
    } ?: defaultValue
}

inline fun <reified T> Settings.decodeValue(
    key: String,
    defaultValue: T
): T =  getStringOrNull(key)?.let {
    JsonSerialization.json.decodeFromString(it)
} ?: defaultValue

inline fun <reified T> Settings.encodeValue(
    key: String,
    value: T,
) = set(key, JsonSerialization.json.encodeToString(value))