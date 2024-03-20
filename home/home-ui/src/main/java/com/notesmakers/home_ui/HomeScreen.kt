package com.notesmakers.home_ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notesmakers.common_ui.buttons.BaseIconButton
import com.notesmakers.common_ui.composables.ChipItem
import com.notesmakers.common_ui.inputs.SearchBar
import com.notesmakers.home_ui.composables.BaseTopAppBar
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            BaseTopAppBar(
                accountIconAction = {},
            )
        },
    ) { innerPadding ->
        HomeScreen(innerPadding)
    }
}

@Composable
private fun HomeScreen(innerPadding: PaddingValues) {
    val listState = rememberLazyStaggeredGridState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    Box {
        NoteGridLayout(listState, innerPadding)
        ScrollToTopButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            showButton = showButton,
            listState = listState,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteGridLayout(
    listState: LazyStaggeredGridState,
    innerPadding: PaddingValues
) {
    var isCategoryVisible by remember {
        mutableStateOf(false)
    }
    LazyVerticalStaggeredGrid(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(10.dp),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SearchBar(hint = "Search", modifier = Modifier.weight(1f))
                BaseIconButton(
                    onClick = { isCategoryVisible = !isCategoryVisible },
                    painterResource = R.drawable.collections_bookmark,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        isCategoryVisible.takeIf { it }?.let {
            item(span = StaggeredGridItemSpan.FullLine) {
                FlowRow(modifier = Modifier.padding(8.dp)) {
                    ChipItem("Price: High to Low")
                    ChipItem("Avg rating: 4+")
                    ChipItem("Free breakfast")
                    ChipItem("Free cancellation")
                    ChipItem("£50 pn")
                }
            }
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.keep),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
                Text(text = "Pinned", fontSize = 20.sp)
            }

        }
        items(2) { photo ->
            ItemNote(
                title = getTitleContent() ?: "",
                textContent = getTextContent(),
                categoryContent = getCategoryContent() ?: "",
                dateTime = "10 march 2021"
            )
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.note_stack),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
                Text(text = "My notes", fontSize = 20.sp)
            }
        }
        items(5) { photo ->
            ItemNote(
                title = getTitleContent() ?: "",
                textContent = getTextContent(),
                categoryContent = getCategoryContent() ?: "",
                dateTime = "10 march 2021"
            )
        }
    }
}

@Composable
private fun ScrollToTopButton(
    modifier: Modifier,
    showButton: Boolean,
    listState: LazyStaggeredGridState,
) {
    val coroutineScope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = showButton,
        modifier = modifier,
        enter = slideInVertically(
            initialOffsetY = { -it }
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically(
            targetOffsetY = { -it }
        ) + fadeOut()
    ) {
        BaseIconButton(
            painterResource = R.drawable.expand_less,
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.primary,
            onClick = {
                coroutineScope.launch {
                    listState.scrollToItem(index = 0)
                }
            },
        )
    }
}

@Composable
private fun ItemNote(
    title: String,
    textContent: String,
    categoryContent: String,
    dateTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(color = (MaterialTheme.colorScheme.tertiaryContainer).copy(alpha = 0.8f))
            .padding(12.dp)
    ) {
        Row {
            Text(
                text = title,
                fontSize = 18.sp,
                maxLines = 1,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = textContent,
            fontSize = 14.sp,
            maxLines = 7,
            fontWeight = FontWeight.Light,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = categoryContent,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = dateTime, fontSize = 12.sp)
        }
    }
}

fun getTextContent() =
    "Jaja nosy niezasnąć moździerza Pani Opatrzonych roli Gors kapeluszach. Mój trzy boku Białopiotrowiczowi wiem inne woń dwie Wara. Batystową przyjezdny gęstość wstydu zwycięsca włościan dział Szabli świat wejrzeniem dzień. Stare przyszłą kołnierzy Jaszczurem wyraz pomniejszy wróciwszy pasem powrócił serce organ Godna Jutro. Dwoma Nauka dojeżdżaczów dziad pokrewieństwem mogę Worończańskim. Woń Najpiękniejszego Rzeczypospolitéj najpiękniejszéj żyt izba bór dali piki rogi. Ludu Jego uczy psem najpiękniejszéj krew wnet znak Rzeczypospolitéj Najpiękniejszego.".let {
        it.substring(0, (70..200).random())
    }

fun getTitleContent() =
    listOf("title", "twotwotwotwotwotwotwo", "three", "four", "five").asSequence().shuffled()
        .find { true }

fun getCategoryContent() =
    listOf("#Todos,#Todos,#Todos,#Todos", "#Todos", "#Todos,#Todos").asSequence().shuffled()
        .find { true }