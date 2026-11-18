package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class LedgerExtendedColors(
  val canvasBackground: Color,
  val cardSurface: Color,
  val subtleSurface: Color,
  val border: Color,
  val borderStrong: Color,
  val textPrimary: Color,
  val textSecondary: Color,
  val textMuted: Color,
  val textPlaceholder: Color,
  val brandGreen: Color,
  val heroGradientStart: Color,
  val heroGradientEnd: Color,
  val heroMintAccent: Color,
  val heroTextPrimary: Color,
  val heroTextMuted: Color,
  val incomeAccent: Color,
  val incomeVivid: Color,
  val incomeText: Color,
  val incomeFill: Color,
  val incomeBorder: Color,
  val incomeCardSurface: Color,
  val expenseAccent: Color,
  val expenseBright: Color,
  val expenseText: Color,
  val expenseFill: Color,
  val expenseBorder: Color,
  val expenseCardSurface: Color,
  val destructiveFill: Color,
  val destructiveText: Color,
)

private val HisabExtendedColors =
  LedgerExtendedColors(
    canvasBackground = LedgerBackground,
    cardSurface = LedgerSurface,
    subtleSurface = LedgerSurfaceLow,
    border = LedgerBorder,
    borderStrong = LedgerBorderStrong,
    textPrimary = LedgerTextPrimary,
    textSecondary = LedgerTextSecondary,
    textMuted = LedgerTextMuted,
    textPlaceholder = LedgerTextPlaceholder,
    brandGreen = LedgerPrimary,
    heroGradientStart = LedgerPrimaryContainer,
    heroGradientEnd = LedgerHeroEnd,
    heroMintAccent = LedgerOnPrimaryContainer,
    heroTextPrimary = LedgerOnPrimary,
    heroTextMuted = LedgerHeroTextMuted,
    incomeAccent = LedgerIncome,
    incomeVivid = LedgerIncomeVivid,
    incomeText = LedgerIncomeText,
    incomeFill = LedgerIncomeFill,
    incomeBorder = LedgerIncomeBorder,
    incomeCardSurface = LedgerIncomeCardSurface,
    expenseAccent = LedgerExpense,
    expenseBright = LedgerExpenseBright,
    expenseText = LedgerExpenseText,
    expenseFill = LedgerExpenseFill,
    expenseBorder = LedgerExpenseBorder,
    expenseCardSurface = LedgerExpenseCardSurface,
    destructiveFill = LedgerDestructiveFill,
    destructiveText = LedgerDestructiveText,
  )

val LocalLedgerColors = staticCompositionLocalOf { HisabExtendedColors }

private val HisabColorScheme =
  lightColorScheme(
    primary = LedgerPrimary,
    onPrimary = LedgerOnPrimary,
    primaryContainer = LedgerPrimaryContainer,
    onPrimaryContainer = LedgerOnPrimaryContainer,
    secondary = LedgerIncome,
    onSecondary = LedgerOnPrimary,
    secondaryContainer = LedgerIncomeFill,
    onSecondaryContainer = LedgerIncomeText,
    tertiary = LedgerPrimary,
    onTertiary = LedgerOnPrimary,
    tertiaryContainer = LedgerSurfaceContainer,
    onTertiaryContainer = LedgerExpenseBright,
    error = LedgerExpense,
    onError = LedgerOnPrimary,
    errorContainer = LedgerDestructiveFill,
    onErrorContainer = LedgerDestructiveText,
    background = LedgerBackground,
    onBackground = LedgerTextPrimary,
    surface = LedgerBackground,
    onSurface = LedgerTextPrimary,
    surfaceVariant = LedgerSurfaceHighest,
    onSurfaceVariant = LedgerTextMuted,
    outline = LedgerTextSecondary,
    outlineVariant = LedgerBorderStrong,
  )

val LedgerShapes =
  Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
  )

object LedgerTheme {
  val colors: LedgerExtendedColors
    @Composable get() = LocalLedgerColors.current
}

@Composable
fun LedgerMinimalTheme(
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(LocalLedgerColors provides HisabExtendedColors) {
    MaterialTheme(
      colorScheme = HisabColorScheme,
      typography = Typography,
      shapes = LedgerShapes,
      content = content,
    )
  }
}
