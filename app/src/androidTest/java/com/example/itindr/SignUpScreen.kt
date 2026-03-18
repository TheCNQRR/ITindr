package com.example.itindr

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView

object SignUpScreen : KScreen<SignUpScreen>() {
    override val layoutId: Int = R.layout.fragment_sign_up
    override val viewClass: Class<*>? = null

    val backButton = KTextView { withId(R.id.back) }
}
