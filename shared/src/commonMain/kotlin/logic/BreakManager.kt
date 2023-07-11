package logic

import data.model.ZbSettings

interface BreakManager {
    /**
     * This will check for [ZbSettings.enabled] automatically before planning a break
     */
    fun planBreak(zbSettings: ZbSettings)

    fun cancelBreak()
}