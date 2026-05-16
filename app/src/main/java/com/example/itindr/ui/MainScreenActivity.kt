package com.example.itindr.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chat.ui.Chats
import com.example.chat.ui.ChatsScreen
import com.example.chat.ui.signlechat.Chat
import com.example.chat.ui.signlechat.ChatScreen
import com.example.itindr.ui.aboutme.edit.AboutMeEdit
import com.example.itindr.ui.aboutme.edit.AboutMeEditScreen
import com.example.itindr.ui.people.People
import com.example.itindr.ui.people.PeopleScreen
import com.example.itindr.ui.people.person.PersonInfo
import com.example.itindr.ui.people.person.PersonInfoScreen
import com.example.itindr.ui.profile.Profile
import com.example.itindr.ui.profile.ProfileScreen
import com.example.itindr.ui.stream.Stream
import com.example.itindr.ui.stream.StreamScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainScreenActivity : AppCompatActivity() {

    private val currentUserId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        supportActionBar?.hide()

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Stream::class
            ) {
                composable<Stream> {
                    StreamScreen(
                        onNavigateToPeople = { navController.navigate(People) },
                        onNavigateToChats =
                        { navController.navigate(Chats(userId = currentUserId.toString())) },
                        onNavigateToProfile = { navController.navigate(Profile) }
                    )
                }

                composable<People> {
                    PeopleScreen(
                        onNavigateToStream = { navController.navigate(Stream) },
                        onNavigateToChats =
                        { navController.navigate(Chats(userId = currentUserId.toString())) },
                        onNavigateToProfile = { navController.navigate(Profile) },
                        onPersonClick = { navController.navigate(PersonInfo) }
                    )
                }

                composable<Chats> {
                    ChatsScreen(
                        onNavigateToStream = { navController.navigate(Stream) },
                        onNavigateToPeople = { navController.navigate(People) },
                        onNavigateToProfile = { navController.navigate(Profile) },
                        onChatClick = { chatId: String ->
                            navController.navigate(Chat(chatId = chatId))
                        }
                    )
                }

                composable<Profile> {
                    ProfileScreen(
                        onNavigateToStream = { navController.navigate(Stream) },
                        onNavigateToPeople = { navController.navigate(People) },
                        onNavigateToChats =
                        { navController.navigate(Chats(userId = currentUserId.toString())) },
                        onEditClick = { navController.navigate(AboutMeEdit) }
                    )
                }

                composable<AboutMeEdit> {
                    AboutMeEditScreen(onBackClick = { navController.navigateUp() })
                }

                composable<PersonInfo> {
                    PersonInfoScreen(onBackClick = { navController.navigateUp() })
                }

                composable<Chat> {
                    ChatScreen(
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
        }
    }

    data class Person(
        val name: String,
        val bio: String,
        val tags: List<String>,
        val photoUrl: Int
    )
}
