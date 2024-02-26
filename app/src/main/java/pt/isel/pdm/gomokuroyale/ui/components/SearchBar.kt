package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.rankings.ui.SearchBarTestTag
import pt.isel.pdm.gomokuroyale.ui.theme.AlabasterWhite
import pt.isel.pdm.gomokuroyale.ui.theme.Brown
import pt.isel.pdm.gomokuroyale.util.Term
import pt.isel.pdm.gomokuroyale.util.toTermOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    query: String,
    isLoading: Boolean = false,
    onSearchRequested: (Term) -> Unit = { },
    onQueryChanged: (String) -> Unit = { },
    onClearSearch: () -> Unit = { },
    onLocalPlayerSearch: () -> Unit = { },
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .testTag(SearchBarTestTag),
            placeholder = {
                Text(
                    stringResource(R.string.search_hint),
                    textAlign = TextAlign.Center,
                )
            },
            query = query,
            onQueryChange = { onQueryChanged(it) },
            onSearch = { it.toTermOrNull()?.let { term -> onSearchRequested(term) } },
            active = false,
            onActiveChange = { },
            leadingIcon = {
                Image(
                    painterResource(id = R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier.clickable { if (!isLoading) onLocalPlayerSearch() }
                )
            },
            enabled = !isLoading,
            trailingIcon = {
                Image(
                    painterResource(id = R.drawable.cancel_icon),
                    contentDescription = null,
                    modifier = Modifier.clickable { onClearSearch() }
                )
            },
            content = { },
            colors = SearchBarDefaults.colors(
                containerColor = Brown,
                dividerColor = AlabasterWhite,
            )
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    MySearchBar(
        query = "",
        onSearchRequested = { },
        onQueryChanged = { },
        onClearSearch = { },
    )
}