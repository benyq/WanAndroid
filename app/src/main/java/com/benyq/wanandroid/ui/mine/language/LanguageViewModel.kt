package com.benyq.wanandroid.ui.mine.language

import androidx.appcompat.app.AppCompatDelegate
import com.benyq.wanandroid.base.BaseViewModel

/**
 *
 * @author benyq
 * @date 12/25/2023
 *
 */
class LanguageViewModel: BaseViewModel() {

    val supportLanguage = arrayListOf(
        LanguageModel("中文", "zh"),
        LanguageModel("English", "en")
    )

    val currentLanguage: LanguageModel?
        get() {
            val appLocale = AppCompatDelegate.getApplicationLocales()
            return supportLanguage.find { it.language == appLocale.toLanguageTags() }
        }

}