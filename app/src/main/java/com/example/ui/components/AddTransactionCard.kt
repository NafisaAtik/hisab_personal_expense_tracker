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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.TransactionCategory
import com.example.data.TransactionType
import com.example.ui.LedgerFormState
import com.example.ui.theme.LedgerTheme
import com.example.ui.theme.TabularAmountStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionCard(
  formState: LedgerFormState,
  onTitleChange: (String) -> Unit,
  onAmountChange: (String) -> Unit,
  onTypeChange: (TransactionType) -> Unit,
  onCategoryChange: (TransactionCategory) -> Unit,
  onSubmit: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LedgerTheme.colors
  val focusManager = LocalFocusManager.current
  var categoryExpanded by remember { mutableStateOf(false) }
  val availableCategories =
    remember(formState.type) { TransactionCategory.categoriesForType(formState.type) }

  Surface(
    modifier = modifier.fillMaxWidth().testTag("add_transaction_card"),
    shape = MaterialTheme.shapes.large,
    color = colors.cardSurface,
    border = BorderStroke(1.dp, colors.border),
    shadowElevation = 1.dp,
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Box(
          modifier =
            Modifier.size(width = 4.dp, height = 18.dp)
              .clip(RoundedCornerShape(999.dp))
              .background(colors.brandGreen)
        )
        Text(
          text = stringResource(R.string.add_transaction),
          style = MaterialTheme.typography.headlineSmall,
          color = colors.textPrimary,
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Transaction Type Segmented Switch
      TransactionTypeToggle(selectedType = formState.type, onTypeSelected = onTypeChange)

      Spacer(modifier = Modifier.height(14.dp))

      // Description Field
      Text(
        text = stringResource(R.string.transaction_title_label),
        style = MaterialTheme.typography.labelMedium,
        color = colors.textMuted,
      )
      Spacer(modifier = Modifier.height(6.dp))
      OutlinedTextField(
        value = formState.title,
        onValueChange = onTitleChange,
        modifier = Modifier.fillMaxWidth().testTag("transaction_title_input"),
        placeholder = {
          Text(
            text = stringResource(R.string.transaction_title_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textPlaceholder,
          )
        },
        isError = formState.titleError != null,
        supportingText =
          formState.titleError?.let { errorMsg ->
            { Text(text = errorMsg, style = MaterialTheme.typography.bodySmall) }
          },
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        keyboardOptions =
          KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next,
          ),
        colors = ledgerTextFieldColors(),
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Amount and Category Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = stringResource(R.string.amount_label),
            style = MaterialTheme.typography.labelMedium,
            color = colors.textMuted,
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = formState.amount,
            onValueChange = onAmountChange,
            modifier = Modifier.fillMaxWidth().testTag("transaction_amount_input"),
            textStyle = TabularAmountStyle.copy(color = colors.textPrimary),
            prefix = {
              Text(
                text = "৳ ",
                style = TabularAmountStyle,
                fontWeight = FontWeight.Bold,
                color = colors.brandGreen,
              )
            },
            placeholder = {
              Text(
                text = stringResource(R.string.amount_hint),
                style = TabularAmountStyle,
                color = colors.textPlaceholder,
              )
            },
            isError = formState.amountError != null,
            supportingText =
              formState.amountError?.let { errorMsg ->
                { Text(text = errorMsg, style = MaterialTheme.typography.bodySmall) }
              },
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            keyboardActions =
              KeyboardActions(
                onDone = {
                  focusManager.clearFocus()
                  onSubmit()
                }
              ),
            colors = ledgerTextFieldColors(),
          )
        }

        Column(modifier = Modifier.weight(1.1f)) {
          Text(
            text = stringResource(R.string.category_label),
            style = MaterialTheme.typography.labelMedium,
            color = colors.textMuted,
          )
          Spacer(modifier = Modifier.height(6.dp))
          ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = it },
          ) {
            OutlinedTextField(
              value = formState.category.displayName,
              onValueChange = {},
              readOnly = true,
              singleLine = true,
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
              modifier =
                Modifier.fillMaxWidth()
                  .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                  .testTag("category_picker_dropdown"),
              textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.textPrimary),
              shape = MaterialTheme.shapes.small,
              colors = ledgerTextFieldColors(),
            )
            ExposedDropdownMenu(
              expanded = categoryExpanded,
              onDismissRequest = { categoryExpanded = false },
              containerColor = colors.cardSurface,
            ) {
              availableCategories.forEach { category ->
                DropdownMenuItem(
                  text = {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                      Icon(
                        imageVector = categoryIcon(category),
                        contentDescription = null,
                        tint = colors.brandGreen,
                        modifier = Modifier.size(18.dp),
                      )
                      Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textPrimary,
                      )
                    }
                  },
                  onClick = {
                    onCategoryChange(category)
                    categoryExpanded = false
                  },
                  modifier = Modifier.testTag("category_option_${category.name}"),
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Emerald Primary Submit Button
      Button(
        onClick = {
          focusManager.clearFocus()
          onSubmit()
        },
        modifier =
          Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("submit_transaction_button"),
        shape = MaterialTheme.shapes.small,
        colors =
          ButtonDefaults.buttonColors(
            containerColor = colors.brandGreen,
            contentColor = MaterialTheme.colorScheme.onPrimary,
          ),
      ) {
        Icon(
          imageVector = Icons.Rounded.Add,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
          text = stringResource(R.string.add_transaction),
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold,
        )
      }
    }
  }
}

@Composable
private fun TransactionTypeToggle(
  selectedType: TransactionType,
  onTypeSelected: (TransactionType) -> Unit,
) {
  val colors = LedgerTheme.colors
  Row(
    modifier =
      Modifier.fillMaxWidth()
        .clip(MaterialTheme.shapes.small)
        .background(colors.subtleSurface)
        .border(1.dp, colors.border, MaterialTheme.shapes.small)
        .padding(4.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    TypeOptionPill(
      label = stringResource(R.string.type_expense),
      selected = selectedType == TransactionType.EXPENSE,
      isIncome = false,
      onClick = { onTypeSelected(TransactionType.EXPENSE) },
      modifier = Modifier.weight(1f).testTag("type_expense_button"),
    )
    TypeOptionPill(
      label = stringResource(R.string.type_income),
      selected = selectedType == TransactionType.INCOME,
      isIncome = true,
      onClick = { onTypeSelected(TransactionType.INCOME) },
      modifier = Modifier.weight(1f).testTag("type_income_button"),
    )
  }
}

@Composable
private fun TypeOptionPill(
  label: String,
  selected: Boolean,
  isIncome: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LedgerTheme.colors
  val containerColor =
    when {
      selected && isIncome -> colors.incomeCardSurface
      selected && !isIncome -> colors.expenseCardSurface
      else -> colors.subtleSurface
    }
  val borderColor =
    when {
      selected && isIncome -> colors.incomeBorder
      selected && !isIncome -> colors.expenseBorder
      else -> colors.subtleSurface
    }
  val textColor =
    when {
      selected && isIncome -> colors.incomeText
      selected && !isIncome -> colors.expenseText
      else -> colors.textSecondary
    }

  Box(
    modifier =
      modifier
        .heightIn(min = 44.dp)
        .clip(RoundedCornerShape(6.dp))
        .background(containerColor)
        .border(1.dp, borderColor, RoundedCornerShape(6.dp))
        .clickable(onClick = onClick)
        .padding(horizontal = 12.dp, vertical = 10.dp),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
      color = textColor,
    )
  }
}

@Composable
private fun ledgerTextFieldColors() =
  OutlinedTextFieldDefaults.colors(
    focusedContainerColor = LedgerTheme.colors.cardSurface,
    unfocusedContainerColor = LedgerTheme.colors.cardSurface,
    focusedBorderColor = LedgerTheme.colors.brandGreen,
    unfocusedBorderColor = LedgerTheme.colors.border,
    errorBorderColor = LedgerTheme.colors.expenseAccent,
    focusedTextColor = LedgerTheme.colors.textPrimary,
    unfocusedTextColor = LedgerTheme.colors.textPrimary,
  )
