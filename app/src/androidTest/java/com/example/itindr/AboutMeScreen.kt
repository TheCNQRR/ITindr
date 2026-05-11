package com.example.itindr

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView

object AboutMeScreen : KScreen<AboutMeScreen>() {
    override val layoutId: Int? = R.layout.fragment_about_me
    override val viewClass: Class<*>? = null

    val saveButton = KTextView { withId(R.id.save) }
}