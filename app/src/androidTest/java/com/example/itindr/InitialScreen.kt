package com.example.itindr

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object InitialScreen : KScreen<InitialScreen>() {
    override val layoutId: Int = R.layout.fragment_initial
    override val viewClass: Class<*>? = null

    val logo = KImageView { withId(R.id.itindr_image_text) }
    val subtitle = KTextView { withId(R.id.text) }
    val background = KImageView { withId(R.id.background) }
    val signUpButton = KTextView { withId(R.id.sign_up_button) }
    val signInButton = KTextView { withId(R.id.sign_in_button) }
}
