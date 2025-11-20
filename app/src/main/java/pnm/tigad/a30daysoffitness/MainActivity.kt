package pnm.tigad.a30daysoffitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pnm.tigad.a30daysoffitness.Data.datasource
import pnm.tigad.a30daysoffitness.Model.Fitness
import pnm.tigad.a30daysoffitness.ui.theme._30_Days_AppsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _30_Days_AppsTheme {

                thirtyDayApp ()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun thirtyDayApp() {
    Surface(modifier = Modifier.fillMaxSize()) {

        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
        val mutableList = datasource.listOfFitness.toMutableList()
        mutableList.shuffle()
        val ideasList = mutableList.toList()

        Scaffold(
            topBar = { TitleBar(scrollBehavior) },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { paddingValues ->

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.ten_dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(dimensionResource(R.dimen.ten_dp)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(ideasList) { idea ->

                    val dayOfMonth = (ideasList.indexOf(idea).inc())

                    DayItem(dayOfMonth, item = idea)
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.title_bar),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
fun DayItem(
    dayOfMonth: Int,
    item: Fitness,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.elevatedCardElevation(dimensionResource(R.dimen.two_dp)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CardImage(item)
            CardInformation(dayOfMonth, item, expanded = expanded)
        }
    }
}


@Composable
fun CardImage(
    datasource: Fitness,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.image_height))
    ) {
        Image(
            painter = painterResource(datasource.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
        )
    }
}

@Composable
private fun CardInformation(
    dayOfMonth: Int,
    datasource: Fitness,
    modifier: Modifier = Modifier,
    expanded: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.twenty_dp))
    ) {

        CardHeadline(dayOfMonth, datasource)

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + androidx.compose.animation.expandVertically(),
            exit = fadeOut() + androidx.compose.animation.shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(dimensionResource(R.dimen.ten_dp)))

                Text(
                    text = stringResource(datasource.title),
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Visible
                )

                Spacer(Modifier.height(dimensionResource(R.dimen.ten_dp)))

                Text(
                    text = stringResource(datasource.desc),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

    }
}


/**
 *  Day of month and idea title composable
 */
@Composable
private fun CardHeadline(
    dayOfMonth: Int,
    datasource: Fitness
) {
    Row {
        Text(
            text = stringResource(R.string.day_text, dayOfMonth),
            style = MaterialTheme.typography.headlineMedium,
            overflow = TextOverflow.Visible
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.ten_dp)))

        Text(
            text = stringResource(datasource.title),
            style = MaterialTheme.typography.headlineMedium,
            overflow = TextOverflow.Visible
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ThirtyDayAppPreview() {
    _30_Days_AppsTheme(darkTheme = true) {
        thirtyDayApp()
    }
}

@Preview(showBackground = true)
@Composable
fun DayItemLightPreview() {
    _30_Days_AppsTheme(darkTheme = false) {
        DayItem(1, datasource.listOfFitness.first())
    }
}

@Preview(showBackground = true)
@Composable
fun DayItemDarkPreview() {
    _30_Days_AppsTheme(darkTheme = true) {
        DayItem(1, datasource.listOfFitness.first())
    }
}