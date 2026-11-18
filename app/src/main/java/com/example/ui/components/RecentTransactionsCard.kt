package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LaptopMac
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.CurrencyFormatter
import com.example.data.Transaction
import com.example.data.TransactionCategory
import com.example.data.TransactionType
import com.example.ui.theme.LedgerTheme
import com.example.ui.theme.TabularAmountStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun categoryIcon(category: TransactionCategory): ImageVector =
  when (category) {
    TransactionCategory.FOOD -> Icons.Outlined.Restaurant
    TransactionCategory.TRANSPORT -> Icons.Outlined.DirectionsCar
    TransactionCategory.HOUSING -> Icons.Outlined.Home
    TransactionCategory.UTILITIES -> Icons.Outlined.ElectricBolt
    TransactionCategory.SHOPPING -> Icons.Outlined.ShoppingBag
    TransactionCategory.HEALTH -> Icons.Outlined.LocalHospital
    TransactionCategory.ENTERTAINMENT -> Icons.Outlined.Movie
    TransactionCategory.SALARY -> Icons.Outlined.Payments
    TransactionCategory.FREELANCE -> Icons.Outlined.LaptopMac
    TransactionCategory.BUSINESS -> Icons.Outlined.BusinessCenter
    TransactionCategory.INVESTMENT -> Icons.Outlined.TrendingUp
    TransactionCategory.GIFT -> Icons.Outlined.CardGiftcard
    TransactionCategory.OTHER -> Icons.Outlined.AccountBalanceWallet
  }

@Composable
fun RecentTransactionsCard(
  transactions: List<Transaction>,
  totalTransactionCount: Int,
  selectedFilter: String,
  filterOptions: List<String>,
  onFilterChange: (String) -> Unit,
  onDeleteTransaction: (Transaction) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LedgerTheme.colors
  val groupedTransactions =
    remember(transactions) { groupTransactionsByDateLabel(transactions) }

  Column(modifier = modifier.fillMaxWidth()) {
    // Section Header + Category Filter Dropdown above the transaction list
    Row(
      modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Text(
          text = stringResource(R.string.recent_transactions),
          style = MaterialTheme.typography.headlineSmall,
          color = colors.textPrimary,
        )
        Box(
          modifier =
            Modifier.clip(RoundedCornerShape(999.dp))
              .background(colors.incomeFill)
              .border(1.dp, colors.incomeBorder, RoundedCornerShape(999.dp))
              .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = transactions.size.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = colors.incomeText,
          )
        }
      }

      CategoryFilterDropdown(
        selectedFilter = selectedFilter,
        options = filterOptions,
        onSelect = onFilterChange,
      )
    }

    // Transaction List Container Card
    Surface(
      modifier = Modifier.fillMaxWidth().testTag("recent_transactions_card"),
      shape = MaterialTheme.shapes.large,
      color = colors.cardSurface,
      border = BorderStroke(1.dp, colors.border),
      shadowElevation = 1.dp,
    ) {
      if (transactions.isEmpty()) {
        TransactionsEmptyState(
          hasAnyTransactions = totalTransactionCount > 0,
          selectedFilter = selectedFilter,
          onResetFilter = { onFilterChange(TransactionCategory.ALL_FILTER) },
        )
      } else {
        Column(modifier = Modifier.fillMaxWidth()) {
          groupedTransactions.forEachIndexed { groupIndex, (dateLabel, items) ->
            // Sticky-style Date Group Header
            Box(
              modifier =
                Modifier.fillMaxWidth()
                  .background(colors.subtleSurface.copy(alpha = 0.7f))
                  .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
              Text(
                text = dateLabel.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary,
              )
            }
            HorizontalDivider(thickness = 1.dp, color = colors.subtleSurface)

            items.forEachIndexed { index, tx ->
              TransactionRow(
                transaction = tx,
                onDelete = { onDeleteTransaction(tx) },
              )
              if (index < items.lastIndex || groupIndex < groupedTransactions.lastIndex) {
                HorizontalDivider(thickness = 1.dp, color = colors.subtleSurface)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun CategoryFilterDropdown(
  selectedFilter: String,
  options: List<String>,
  onSelect: (String) -> Unit,
) {
  val colors = LedgerTheme.colors
  var expanded by remember { mutableStateOf(false) }

  Box {
    Surface(
      shape = MaterialTheme.shapes.small,
      color = colors.cardSurface,
      border = BorderStroke(1.dp, colors.border),
      modifier =
        Modifier.heightIn(min = 48.dp)
          .clip(MaterialTheme.shapes.small)
          .clickable { expanded = true }
          .testTag("category_filter_dropdown"),
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Icon(
          imageVector = Icons.Outlined.FilterList,
          contentDescription = null,
          tint = colors.brandGreen,
          modifier = Modifier.size(16.dp),
        )
        Text(
          text = selectedFilter,
          style = MaterialTheme.typography.labelMedium,
          color = colors.textPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Icon(
          imageVector = Icons.Rounded.KeyboardArrowDown,
          contentDescription = null,
          tint = colors.textSecondary,
          modifier = Modifier.size(18.dp),
        )
      }
    }

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      containerColor = colors.cardSurface,
    ) {
      options.forEach { option ->
        DropdownMenuItem(
          text = {
            Text(
              text = option,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = if (option == selectedFilter) FontWeight.SemiBold else FontWeight.Normal,
              color = if (option == selectedFilter) colors.brandGreen else colors.textPrimary,
            )
          },
          onClick = {
            onSelect(option)
            expanded = false
          },
          modifier = Modifier.testTag("filter_option_$option"),
        )
      }
    }
  }
}

@Composable
private fun TransactionRow(
  transaction: Transaction,
  onDelete: () -> Unit,
) {
  val colors = LedgerTheme.colors
  val isIncome = transaction.type == TransactionType.INCOME
  val categoryEnum = TransactionCategory.fromDisplayName(transaction.category)
  val timeFormatted = remember(transaction.createdAtMillis) {
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(transaction.createdAtMillis))
  }

  Row(
    modifier =
      Modifier.fillMaxWidth()
        .padding(start = 16.dp, end = 6.dp, top = 12.dp, bottom = 12.dp)
        .testTag("transaction_row_${transaction.id}"),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    // Category Icon Circle
    Box(
      modifier =
        Modifier.size(40.dp)
          .clip(CircleShape)
          .background(if (isIncome) colors.incomeFill else colors.expenseFill),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = categoryIcon(categoryEnum),
        contentDescription = null,
        tint = if (isIncome) colors.incomeAccent else colors.expenseAccent,
        modifier = Modifier.size(20.dp),
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    // Description + Category Tag + Timestamp
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = transaction.title,
        style = MaterialTheme.typography.titleMedium,
        color = colors.textPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Spacer(modifier = Modifier.height(4.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Box(
          modifier =
            Modifier.clip(RoundedCornerShape(999.dp))
              .background(colors.subtleSurface)
              .border(1.dp, colors.border, RoundedCornerShape(999.dp))
              .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = transaction.category,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
        Text(
          text = timeFormatted,
          style = MaterialTheme.typography.bodySmall,
          color = colors.textSecondary,
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Semantic Cash-Flow Chip (+৳ / -৳)
    val chipBg = if (isIncome) colors.incomeFill else colors.expenseFill
    val chipBorder = if (isIncome) colors.incomeBorder else colors.expenseBorder
    val chipText = if (isIncome) colors.incomeText else colors.expenseText

    Box(
      modifier =
        Modifier.clip(RoundedCornerShape(999.dp))
          .background(chipBg)
          .border(1.dp, chipBorder, RoundedCornerShape(999.dp))
          .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
      Text(
        text = CurrencyFormatter.formatSignedAmount(transaction.amount, transaction.type),
        style = TabularAmountStyle,
        color = chipText,
        maxLines = 1,
      )
    }

    // Delete Icon Button (48.dp touch target)
    IconButton(
      onClick = onDelete,
      modifier = Modifier.size(48.dp).testTag("delete_transaction_${transaction.id}"),
    ) {
      Icon(
        imageVector = Icons.Outlined.DeleteOutline,
        contentDescription = stringResource(R.string.delete_transaction_cd),
        tint = colors.textSecondary,
        modifier = Modifier.size(20.dp),
      )
    }
  }
}

@Composable
private fun TransactionsEmptyState(
  hasAnyTransactions: Boolean,
  selectedFilter: String,
  onResetFilter: () -> Unit,
) {
  val colors = LedgerTheme.colors
  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 36.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Box(
      modifier =
        Modifier.size(52.dp)
          .clip(CircleShape)
          .background(colors.incomeFill)
          .border(1.dp, colors.incomeBorder, CircleShape),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.Outlined.ReceiptLong,
        contentDescription = null,
        tint = colors.brandGreen,
        modifier = Modifier.size(24.dp),
      )
    }
    Spacer(modifier = Modifier.height(12.dp))
    Text(
      text =
        if (hasAnyTransactions) {
          stringResource(R.string.empty_filtered_title, selectedFilter)
        } else {
          stringResource(R.string.empty_transactions_title)
        },
      style = MaterialTheme.typography.titleMedium,
      color = colors.textPrimary,
      textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text =
        if (hasAnyTransactions) {
          "Try selecting another category or viewing all entries."
        } else {
          stringResource(R.string.empty_transactions_subtitle)
        },
      style = MaterialTheme.typography.bodyMedium,
      color = colors.textSecondary,
      textAlign = TextAlign.Center,
    )
    if (hasAnyTransactions) {
      Spacer(modifier = Modifier.height(12.dp))
      TextButton(
        onClick = onResetFilter,
        modifier = Modifier.heightIn(min = 48.dp).testTag("reset_filter_button"),
      ) {
        Text(
          text = stringResource(R.string.show_all_categories),
          style = MaterialTheme.typography.labelMedium,
          color = colors.brandGreen,
        )
      }
    }
  }
}

private fun groupTransactionsByDateLabel(
  transactions: List<Transaction>
): List<Pair<String, List<Transaction>>> {
  val todayCal = Calendar.getInstance()
  val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
  val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

  return transactions
    .groupBy { tx ->
      val txCal = Calendar.getInstance().apply { timeInMillis = tx.createdAtMillis }
      when {
        isSameDay(txCal, todayCal) -> "Today"
        isSameDay(txCal, yesterdayCal) -> "Yesterday"
        else -> dateFormat.format(Date(tx.createdAtMillis))
      }
    }
    .toList()
}

private fun isSameDay(a: Calendar, b: Calendar): Boolean =
  a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
    a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)
