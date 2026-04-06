package com.example.itindr

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.itindr.ui.MainScreenActivity
import com.example.itindr.ui.test.MainScreenTestTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class MainScreenAndroidTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainScreenActivity>()

    private val tags = listOf("Python", "Django", "REST")

    @Test
    fun testDisplayingMainElements() {
        with(composeTestRule) {
            onNodeWithTag(MainScreenTestTag.PersonCardPhotoTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.PersonCardNameTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.PersonCardInterestsTag).assertIsDisplayed()
            tags.forEachIndexed { index, tag ->
                onAllNodesWithTag(MainScreenTestTag.PersonCardInterestTag, useUnmergedTree = true)[index]
                    .assertIsDisplayed()
                    .assertTextEquals(tags[index])
            }
            onNodeWithTag(MainScreenTestTag.PersonCardDislikeButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.PersonCardLikeButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.NavBarTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.NavBarStreamButtonTag)
                .onChildren()
                .filterToOne(hasText("Поток"))
                .assertExists()
            onNodeWithTag(MainScreenTestTag.NavBarPeopleButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.NavBarChatsButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.NavBarProfileButtonTag).assertIsDisplayed()

        }
    }
}
