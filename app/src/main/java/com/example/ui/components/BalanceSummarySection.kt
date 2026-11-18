package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.CurrencyFormatter
import com.example.ui.theme.CurrencyStatStyle
import com.example.ui.theme.LedgerTheme
import com.example.ui.theme.SubMetricStatStyle

@Composable
fun BalanceSummarySection(
  totalBalance: Double,
  totalIncome: Double,
  totalExpenses: Double,
  transactionCount: Int,
  modifier: Modifier = Modifier,
) {
  val colors = LedgerTheme.colors
  val heroBrush =
    Brush.linearGradient(colors = listOf(colors.heroGradientStart, colors.heroGradientEnd))

  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    // Primary Total Balance Hero Card with Deep Emerald-to-Navy Gradient
    Surface(
      modifier = Modifier.fillMaxWidth().testTag("total_balance_card"),
      shape = MaterialTheme.shapes.large,
      color = colors.heroGradientEnd,
      shadowElevation = 2.dp,
    ) {
      Column(modifier = Modifier.background(heroBrush).padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = stringResource(R.string.total_balance).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = colors.heroTextMuted,
          )
          HeroBalanceBadge(totalBalance = totalBalance)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = CurrencyFormatter.formatAmount(totalBalance),
          style = CurrencyStatStyle,
          color = colors.heroTextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.testTag("total_balance_value"),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cash-flow ratio bar (Emerald Income vs Crimson Expense)
        CashFlowRatioBar(totalIncome = totalIncome, totalExpenses = totalExpenses)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          val countLabel =
            if (transactionCount == 1) "1 recorded entry" else "$transactionCount recorded entries"
          Text(
            text = countLabel,
            style = MaterialTheme.typography.bodySmall,
            color = colors.heroTextMuted,
          )
          val totalVolume = totalIncome + totalExpenses
          val savingsRateText =
            if (totalIncome > 0.0) {
              val pct = (((totalIncome - totalExpenses) / totalIncome) * 100.0).toInt()
              "Net ${pct}%"
            } else if (totalVolume > 0.0) {
              "Outflow only"
            } else {
              "Ready to track"
            }
          Text(
            text = savingsRateText,
            style = MaterialTheme.typography.labelSmall,
            color = colors.heroMintAccent,
          )
        }
      }
    }

    // Total Income & Total Expenses 2-Column Tiles with subtle semantic card tints
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      MetricTile(
        label = stringResource(R.string.total_income),
        formattedValue = CurrencyFormatter.formatAmount(totalIncome),
        isIncome = true,
        cardTestTag = "total_income_card",
        valueTestTag = "total_income_value",
        modifier = Modifier.weight(1f),
      )
      MetricTile(
        label = stringResource(R.string.total_expenses),
        formattedValue = CurrencyFormatter.formatAmount(totalExpenses),
        isIncome = false,
        cardTestTag = "total_expenses_card",
        valueTestTag = "total_expenses_value",
        modifier = Modifier.weight(1f),
      )
    }
  }
}

@Composable
private fun CashFlowRatioBar(totalIncome: Double, totalExpenses: Double) {
  val colors = LedgerTheme.colors
  val total = totalIncome + totalExpenses
  val incomeFraction =
    if (total > 0.0) (totalIncome / total).toFloat().coerceIn(0f, 1f) else 0.5f

  Box(
    modifier =
      Modifier.fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(999.dp))
        .background(colors.heroTextPrimary.copy(alpha = 0.14f))
  ) {
    if (total > 0.0) {
      Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        if (incomeFraction > 0f) {
          Box(
            modifier =
              Modifier.weight(incomeFraction.coerceAtLeast(0.02f))
                .fillMaxHeight()
                .background(colors.heroMintAccent)
          )
        }
        val expenseFraction = 1f - incomeFraction
        if (expenseFraction > 0f) {
          Box(
            modifier =
              Modifier.weight(expenseFraction.coerceAtLeast(0.02f))
                .fillMaxHeight()
                .background(colors.expenseBorder)
          )
        }
      }
    } else {
      Box(
        modifier =
          Modifier.fillMaxWidth(0.35f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(999.dp))
            .background(colors.heroMintAccent.copy(alpha = 0.5f))
      )
    }
  }
}

@Composable
private fun HeroBalanceBadge(totalBalance: Double) {
  val colors = LedgerTheme.colors
  val (label, bgColor, borderColor, textColor) =
    when {
      totalBalance > 0.004 ->
        Quadruple(
          "Net Positive",
          colors.heroMintAccent.copy(alpha = 0.18f),
          colors.heroMintAccent.copy(alpha = 0.45f),
          colors.heroMintAccent,
        )
      totalBalance < -0.004 ->
        Quadruple(
          "Net Deficit",
          colors.expenseBright.copy(alpha = 0.22f),
          colors.expenseBorder.copy(alpha = 0.5f),
          colors.expenseBorder,
        )
      else ->
        Quadruple(
          "Balanced",
          colors.heroTextPrimary.copy(alpha = 0.12f),
          colors.heroTextPrimary.copy(alpha = 0.24f),
          colors.heroTextMuted,
        )
    }

  Box(
    modifier =
      Modifier.clip(RoundedCornerShape(999.dp))
        .background(bgColor)
        .border(1.dp, borderColor, RoundedCornerShape(999.dp))
        .padding(horizontal = 10.dp, vertical = 4.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.SemiBold,
      color = textColor,
    )
  }
}

@Composable
private fun MetricTile(
  label: String,
  formattedValue: String,
  isIncome: Boolean,
  cardTestTag: String,
  valueTestTag: String,
  modifier: Modifier = Modifier,
) {
  val colors = LedgerTheme.colors
  val cardSurface = if (isIncome) colors.incomeCardSurface else colors.expenseCardSurface
  val cardBorder = if (isIncome) colors.incomeBorder.copy(alpha = 0.7f) else colors.expenseBorder.copy(alpha = 0.6f)
  val tintFill = if (isIncome) colors.incomeFill else colors.expenseFill
  val accentColor = if (isIncome) colors.incomeAccent else colors.expenseAccent
  val valueColor = if (isIncome) colors.incomeText else colors.expenseText
  val icon = if (isIncome) Icons.Rounded.ArrowDownward else Icons.Rounded.ArrowUpward

  Surface(
    modifier = modifier.testTag(cardTestTag),
    shape = MaterialTheme.shapes.large,
    color = cardSurface,
    border = BorderStroke(1.dp, cardBorder),
    shadowElevation = 1.dp,
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Box(
          modifier = Modifier.size(28.dp).clip(CircleShape).background(tintFill),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(16.dp),
          )
        }
        Text(
          text = label,
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Medium,
          color = colors.textMuted,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = formattedValue,
        style = SubMetricStatStyle,
        color = valueColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.testTag(valueTestTag),
      )
    }
  }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
