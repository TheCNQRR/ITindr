package com.example.itindr

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

class MainScreenActivity : AppCompatActivity() {
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

        val mockPerson = Person("Андрей Иванов", "Люблю программировать на питоне. Люблю изучать питон. Люблю всё, что угодно, связанное с питоном. А еще я люблю перл.",
            listOf("Python", "Django", "REST"), R.drawable.ic_mock_user_photo)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Stream::class
            ) {
                composable<Stream> {
                    StreamScreen(
                        mockPerson,
                        onNavigateToPeople = { navController.navigate(People) },
                        onNavigateToChats = { navController.navigate(Chats) },
                        onNavigateToProfile = { navController.navigate(Profile) }
                    )
                }

                composable<People> {
                    PeopleScreen(
                        onNavigateToStream = { navController.navigate(Stream) },
                        onNavigateToChats = { navController.navigate(Chats) },
                        onNavigateToProfile = { navController.navigate(Profile) },
                        onPersonClick = { navController.navigate(PersonInfo) }
                    )
                }

                composable<Chats> {
                    ChatsScreen(
                        onNavigateToStream = { navController.navigate(Stream) },
                        onNavigateToPeople = { navController.navigate(People) },
                        onNavigateToProfile = { navController.navigate(Profile) },
                        onChatClick = { navController.navigate(Chat) }
                    )
                }

                composable<Profile> {
                    ProfileScreen(
                        onNavigateToStream = { navController.navigate(Stream) },
                        onNavigateToPeople = { navController.navigate(People) },
                        onNavigateToChats = { navController.navigate(Chats) },
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
                    ChatScreen(onBackClick = { navController.navigateUp() })
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
