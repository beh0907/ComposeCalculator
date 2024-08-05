package com.skymilk.composecalculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skymilk.composecalculator.ui.theme.ComposeCalculatorTheme

@Composable
fun HomeScreen(
    calculatorViewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = calculatorViewModel.uiState

    Box(modifier = modifier.fillMaxSize()) {
        val contentMargin = 16.dp

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {

            //결과 및 현재 입력 정보
            Column(
                modifier = Modifier
                    .padding(horizontal = contentMargin)
                    .align(Alignment.End)
            ) {
                Text(
                    text = uiState.infix,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(contentMargin))

                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = uiState.result,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(contentMargin))
            }

            //괄호 버튼 영역
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(contentMargin))

                CharacterItem(
                    char = "(",
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f, true)
                ) { calculatorViewModel.onInfixChange("(") }

                Spacer(modifier = Modifier.height(contentMargin))

                CharacterItem(
                    char = ")",
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f, true)
                ) { calculatorViewModel.onInfixChange(")") }

                Spacer(modifier = Modifier.height(contentMargin))
            }

            Spacer(modifier = Modifier.height(contentMargin))

            //키패드 영역
            Row {
                val numKeyPad = listOf("7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "C")

                //번호 키패드 그리드
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(1f)
                ) {
                    items(numKeyPad) { num ->
                        CharacterItem(
                            char = num,
                            modifier = Modifier
                                .padding(contentMargin)
                                .weight(1f, false)
                        ) {
                            //C일경우 초기화처리
                            if (num == "C") {
                                calculatorViewModel.clearInfixExpression()
                                return@CharacterItem
                            }

                            calculatorViewModel.onInfixChange(num)
                        }
                    }
                }

                //연산자 키패드
                ConstraintLayout(modifier = Modifier.weight(.8f)) {
                    val (add, multiple, division, minus, power, equal) = createRefs()

                    CharacterItem(
                        char = "-",
                        modifier = Modifier
                            .height(50.dp)
                            .constrainAs(minus) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(division.start)
                                bottom.linkTo(add.top)
                            }
                            .aspectRatio(1f),
                        color = MaterialTheme.colorScheme.secondary
                    ) { calculatorViewModel.onInfixChange("-") }

                    CharacterItem(
                        char = "/",
                        modifier = Modifier
                            .height(50.dp)
                            .constrainAs(division) {
                                top.linkTo(parent.top)
                                start.linkTo(minus.end, contentMargin)
                                end.linkTo(parent.end, contentMargin)
                            }
                            .aspectRatio(1f),
                        color = MaterialTheme.colorScheme.secondary
                    ) { calculatorViewModel.onInfixChange("/") }

                    CharacterItem(
                        char = "*",
                        modifier = Modifier
                            .height(50.dp)
                            .constrainAs(multiple) {
                                top.linkTo(division.bottom, contentMargin)
                                start.linkTo(add.end)
                                end.linkTo(parent.end, contentMargin)
                                bottom.linkTo(power.top)
                            }
                            .aspectRatio(1f),
                        color = MaterialTheme.colorScheme.secondary
                    ) { calculatorViewModel.onInfixChange("*") }

                    CharacterItem(
                        char = "^",
                        modifier = Modifier
                            .height(50.dp)
                            .constrainAs(power) {
                                top.linkTo(multiple.bottom, contentMargin)
                                start.linkTo(add.end, contentMargin)
                                end.linkTo(parent.end, contentMargin)
                                bottom.linkTo(equal.top)
                            }
                            .aspectRatio(1f),
                        color = MaterialTheme.colorScheme.secondary
                    ) { calculatorViewModel.onInfixChange("^") }

                    CharacterItem(
                        char = "+",
                        modifier = Modifier
                            .width(50.dp)
                            .constrainAs(add) {
                                top.linkTo(minus.bottom, contentMargin)
                                start.linkTo(parent.start)
                                end.linkTo(multiple.start, contentMargin)
                                bottom.linkTo(equal.top, contentMargin)
                            }
                            .aspectRatio(1f / 2f),
                        color = MaterialTheme.colorScheme.secondary
                    ) { calculatorViewModel.onInfixChange("+") }

                    CharacterItem(
                        char = "=",
                        modifier = Modifier
                            .height(50.dp)
                            .constrainAs(equal) {
                                top.linkTo(power.bottom, contentMargin)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end, contentMargin)
                                bottom.linkTo(parent.bottom)
                            }
                            .aspectRatio(2f),
                        color = MaterialTheme.colorScheme.secondary
                    ) { calculatorViewModel.evaluateExpression() }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    char: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = color,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick.invoke() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = char,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HomeScreenPreview() {

    ComposeCalculatorTheme {
        val calculatorViewModel = CalculatorViewModel()
        HomeScreen(calculatorViewModel)
    }

}