package com.example.itindr

import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.itindr.ui.MainScreenActivity
import com.example.itindr.ui.stream.DarknessAlphaKey
import com.example.itindr.ui.stream.PhotoResourceKey
import com.example.itindr.ui.test.MainScreenTestTag
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class MainScreenAndroidTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainScreenActivity>()

    @Test
    fun testDisplayingMainElements() {
        with(composeTestRule) {
            val python = composeTestRule.activity.getString(R.string.python)
            val django = composeTestRule.activity.getString(R.string.django)
            val rest = composeTestRule.activity.getString(R.string.rest)

            val tags = listOf(python, django, rest)

            onNodeWithTag(MainScreenTestTag.PersonCardPhotoTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.PersonCardNameTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.PersonCardInterestsTag).assertIsDisplayed()
            tags.forEachIndexed { index, _ ->
                onAllNodesWithTag(MainScreenTestTag.PersonCardInterestTag,
                    useUnmergedTree = true)[index]
                    .assertIsDisplayed()
                    .assertTextEquals(tags[index])
            }
            onNodeWithTag(MainScreenTestTag.PersonCardDislikeButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.PersonCardLikeButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.NavBarTag).assertIsDisplayed()
            val streamText = composeTestRule.activity.getString(R.string.stream)
            onNodeWithTag(MainScreenTestTag.NavBarStreamButtonTag)
                .onChildren()
                .filterToOne(hasText(streamText))
                .assertExists()
            onNodeWithTag(MainScreenTestTag.NavBarPeopleButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.NavBarChatsButtonTag).assertIsDisplayed()
            onNodeWithTag(MainScreenTestTag.NavBarProfileButtonTag).assertIsDisplayed()

        }
    }

    @Test
    fun testNavigationToPeopleScreen() {
        with(composeTestRule) {
            onNodeWithTag(MainScreenTestTag.NavBarPeopleButtonTag).performClick()
            onNodeWithTag(MainScreenTestTag.PeopleScreenHeaderTag).assertIsDisplayed()
        }
    }

    @Test
    fun testUserPhotoDarkensOnScroll() {
        with(composeTestRule) {
            val initialAlpha = onNodeWithTag(MainScreenTestTag.DarknessOverlayTag)
                .fetchSemanticsNode().config[DarknessAlphaKey]
            assertTrue("Начальное затемнение должно быть 0", initialAlpha == 0f)

            onNodeWithTag(MainScreenTestTag.PersonCardNameTag)
                .performTouchInput { swipe(Offset(centerX, centerY), Offset(centerX, centerY - 500)) }

            composeTestRule.waitForIdle()

            val finalAlpha = onNodeWithTag(MainScreenTestTag.DarknessOverlayTag)
                .fetchSemanticsNode().config[DarknessAlphaKey]
            assertTrue("Затемнение не применилось! Alpha: $finalAlpha", finalAlpha > 0f)
        }
    }

    @Test
    fun testDislikeUpdatePersonInfo() {
        with(composeTestRule) {
            val persons = composeTestRule.activity.getMockPersons()
            val firstPerson = persons[0]
            val secondPerson = persons[1]

            fun getPhotoResource(): Int {
                return onNodeWithTag(MainScreenTestTag.PersonCardPhotoTag)
                    .fetchSemanticsNode()
                    .config[PhotoResourceKey]
            }

            onNodeWithTag(MainScreenTestTag.PersonCardNameTag)
                .assertTextEquals(firstPerson.name)
            onNodeWithTag(MainScreenTestTag.BioTextTag)
                .assertTextEquals(firstPerson.bio)
            val firstPhoto = getPhotoResource()
            assertTrue(firstPhoto == firstPerson.photoUrl)

            onNodeWithTag(MainScreenTestTag.PersonCardDislikeButtonTag).performClick()
            composeTestRule.waitForIdle()

            onNodeWithTag(MainScreenTestTag.PersonCardNameTag)
                .assertTextEquals(secondPerson.name)
            onNodeWithTag(MainScreenTestTag.BioTextTag)
                .assertTextEquals(secondPerson.bio)
            val secondPhoto = getPhotoResource()
            assertTrue(secondPhoto == secondPerson.photoUrl)

            assertTrue("Фото не изменилось", firstPhoto != secondPhoto)
        }
    }
}
