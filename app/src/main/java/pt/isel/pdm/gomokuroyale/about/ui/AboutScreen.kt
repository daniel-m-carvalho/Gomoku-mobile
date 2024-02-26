package pt.isel.pdm.gomokuroyale.about.ui


import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


data class SocialInfo(val link: Uri, @DrawableRes val imageId: Int)

data class AuthorInfo(
    val number: String,
    val name: String,
    val email: String,
    val image : Int,
    val socials: List<SocialInfo>
)

const val AboutScreenTestTag = "AboutScreenTestTag"


@Composable
fun AboutScreen(
    onBackRequested: () -> Unit = { },
    onSendEmailRequested: (String) -> Unit = { },
    onOpenUrlRequested: (Uri) -> Unit = { },
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(AboutScreenTestTag),
            topBar = { TopBar(navigation = NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Author(
                    authorsList, onSendEmailRequested = onSendEmailRequested,
                    onOpenUrlRequested = onOpenUrlRequested
                )
            }
        }
    }
}


@Composable
private fun Author(
    authors: List<AuthorInfo>,
    onSendEmailRequested: (String) -> Unit = { },
    onOpenUrlRequested: (Uri) -> Unit = { },
) =
    authors.forEach { author ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = author.image),
                contentDescription = null,
                modifier = Modifier.sizeIn(100.dp, 100.dp, 150.dp, 150.dp).clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(text = author.name, style = MaterialTheme.typography.titleLarge)
                Text(text = author.number, style = MaterialTheme.typography.titleLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Social(id = R.drawable.ic_email, onClick = { onSendEmailRequested(author.email) })
                    author.socials.forEach {
                        Social(id = it.imageId, onClick = { onOpenUrlRequested(it.link) })
                    }
                }
            }
        }
    }

@Composable
private fun Social(@DrawableRes id: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = Modifier
            .sizeIn(maxWidth = 32.dp)
            .clickable { onClick() }
    )
}

private val authorsList: List<AuthorInfo> = listOf(
    AuthorInfo(
        number = "49495",
        name = "Gonçalo Frutuoso",
        email = "a49495@alunos.isel.pt",
        image = R.drawable.ic_frutuoso,
        socials = listOf(
            SocialInfo(
                link = Uri.parse("https://www.linkedin.com/in/gonçalo-frutuoso/"),
                imageId = R.drawable.ic_linkedin
            ),
            SocialInfo(
                link = Uri.parse("https://github.com/Gongamax"),
                imageId = R.drawable.ic_github
            )
        )
    ),
    AuthorInfo(
        number = "49462",
        name = "Francisco Saraiva",
        email = "49462@alunos.isel.pt",
        image = R.drawable.ic_saraiva,
        socials = listOf(
            SocialInfo(
                link = Uri.parse("https://www.linkedin.com/in/francisco-saraiva-507119236/"),
                imageId = R.drawable.ic_linkedin
            ),
            SocialInfo(
                link = Uri.parse("https://github.com/saraiva22"),
                imageId = R.drawable.ic_github
            )
        )
    ),
    AuthorInfo(
        number = "49419",
        name = "Daniel Carvalho",
        email = "49419@alunos.isel.pt",
        image = R.drawable.ic_carvalho,
        socials = listOf(
            SocialInfo(
                link = Uri.parse("https://www.linkedin.com/in/daniel-martinho-de-carvalho-3b0619207/"),
                imageId = R.drawable.ic_linkedin
            ),
            SocialInfo(
                link = Uri.parse("https://github.com/DanielMartinhoCarvalho"),
                imageId = R.drawable.ic_github
            )
        )
    )
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InfoScreenPreview() {
    AboutScreen()
}