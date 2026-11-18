---
name: Ledger Minimal
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#45464d'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#76777d'
  outline-variant: '#c6c6cd'
  surface-tint: '#565e74'
  primary: '#000000'
  on-primary: '#ffffff'
  primary-container: '#131b2e'
  on-primary-container: '#7c839b'
  inverse-primary: '#bec6e0'
  secondary: '#006c49'
  on-secondary: '#ffffff'
  secondary-container: '#6cf8bb'
  on-secondary-container: '#00714d'
  tertiary: '#000000'
  on-tertiary: '#ffffff'
  tertiary-container: '#410004'
  on-tertiary-container: '#ef4444'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dae2fd'
  primary-fixed-dim: '#bec6e0'
  on-primary-fixed: '#131b2e'
  on-primary-fixed-variant: '#3f465c'
  secondary-fixed: '#6ffbbe'
  secondary-fixed-dim: '#4edea3'
  on-secondary-fixed: '#002113'
  on-secondary-fixed-variant: '#005236'
  tertiary-fixed: '#ffdad7'
  tertiary-fixed-dim: '#ffb3ad'
  on-tertiary-fixed: '#410004'
  on-tertiary-fixed-variant: '#930013'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  display-lg:
    fontFamily: Geist
    fontSize: 3rem
    fontWeight: '600'
    lineHeight: '1.15'
    letterSpacing: -0.03em
  display-lg-mobile:
    fontFamily: Geist
    fontSize: 2.25rem
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: -0.025em
  headline-lg:
    fontFamily: Geist
    fontSize: 2rem
    fontWeight: '600'
    lineHeight: '1.25'
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Geist
    fontSize: 1.5rem
    fontWeight: '600'
    lineHeight: '1.3'
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Geist
    fontSize: 1.5rem
    fontWeight: '600'
    lineHeight: '1.3'
    letterSpacing: -0.015em
  headline-sm:
    fontFamily: Geist
    fontSize: 1.25rem
    fontWeight: '600'
    lineHeight: '1.35'
    letterSpacing: -0.01em
  title-md:
    fontFamily: Geist
    fontSize: 1rem
    fontWeight: '500'
    lineHeight: '1.4'
    letterSpacing: -0.005em
  body-lg:
    fontFamily: Geist
    fontSize: 1.125rem
    fontWeight: '400'
    lineHeight: '1.5'
    letterSpacing: '0'
  body-md:
    fontFamily: Geist
    fontSize: 0.875rem
    fontWeight: '400'
    lineHeight: '1.5'
    letterSpacing: '0'
  body-sm:
    fontFamily: Geist
    fontSize: 0.75rem
    fontWeight: '400'
    lineHeight: '1.4'
    letterSpacing: 0.01em
  label-md:
    fontFamily: Geist
    fontSize: 0.875rem
    fontWeight: '500'
    lineHeight: '1'
    letterSpacing: '0'
  label-sm:
    fontFamily: Geist
    fontSize: 0.75rem
    fontWeight: '600'
    lineHeight: '1'
    letterSpacing: 0.02em
  currency-stat:
    fontFamily: Geist
    fontSize: 2.25rem
    fontWeight: '700'
    lineHeight: '1.1'
    letterSpacing: -0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1.5rem
  gutter-mobile: 1rem
  margin: 2rem
  margin-mobile: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system embodies precise minimalism tailored for personal wealth management and real-time expense tracking. The aesthetic balances analytical clarity with an approachable, frictionless user experience. It avoids decorative visual clutter, ensuring data density remains legible, non-intimidating, and deliberate. 

The aesthetic sits at the intersection of modern corporate SaaS and refined utilitarian minimalism:
- **Atmosphere:** Controlled, tranquil, and mathematically rigorous.
- **Visual Values:** High legibility, strict contrast boundaries, crisp hierarchy, and instantaneous data comprehension.
- **Target Experience:** Users should feel in absolute control of their financial state, experiencing zero cognitive strain when categorizing expenses, reviewing cash-flow projections, or reading dense transaction histories.

## Colors

The palette is engineered around high functional contrast and instant semantic recognition, anchoring against neutral slate surfaces.

- **Primary (`#0F172A`):** Deep slate navy serves as the structural anchor for primary interactive actions, high-emphasis text, and navigational focus points.
- **Secondary (`#10B981`):** Vivid emerald green reserved exclusively for positive cash flow, income streams, net-worth growth indicators, and confirmed states.
- **Tertiary (`#EF4444`):** Controlled crimson rose strictly dedicated to outflows, expenses, over-budget warnings, and destructive actions.
- **Neutral (`#64748B`):** Cool slate gray for secondary labels, metadata, chart gridlines, and inactive states.
- **Surface Roles:**
  - Base canvas uses `#F8FAFC` to establish a soft, glare-free backdrop.
  - Interactive cards and elevated containers use pure `#FFFFFF` with structural borders (`#E2E8F0`) to create clear separation.
  - Subtle contextual fills utilize slate tinting (`#F1F5F9`) for input backdrops, hover states, and table headers.

## Typography

Geist powers the entire typographical hierarchy, leveraging its geometric precision, neutral character, and optimized tabular numeral rendering crucial for financial alignment.

- **Tabular Figures:** All numeric displays (balances, line-item totals, ledger tables) must enable OpenType tabular numbers (`tnum`) to maintain vertical columnar alignment across fluctuating values.
- **Scale Usage:**
  - `currency-stat` and `display-lg` are reserved for master portfolio aggregates and primary account balances.
  - `headline-lg` and `headline-md` articulate structural dashboard boundaries.
  - `label-sm` provides micro-copy clarity for categorical expense tags, transaction status badges, and timestamp headers.

## Layout & Spacing

The layout model uses a responsive fluid grid bound within a constrained max-width shell of 1280px to preserve data density on ultra-wide screens.

- **Desktop (1024px+):** 12-column grid with a 1.5rem (`24px`) gutter and 2rem (`32px`) outer canvas margin. Multi-pane structure splits views into navigation sidebars, central ledger streams, and contextual analytical side panels.
- **Tablet (768px - 1023px):** 8-column layout. Analytical side panels collapse into secondary tabs or stacked views. Margins step down to 1.5rem (`24px`).
- **Mobile (Below 768px):** 4-column layout with 1rem (`16px`) gutters and 1rem (`16px`) canvas margins. Navigation relocates to a persistent bottom bar or sticky sub-header.
- **Vertical Rhythm:** Modular components adhere to multiples of 4px and 8px. Dashboard widgets maintain an internal padding of `space-lg` (24px) for desktop and `space-md` (16px) for mobile.

## Elevation & Depth

Visual separation relies on sharp boundary lines layered over diffuse, ambient shadows rather than stark elevation planes.

- **Surface Layering:** The viewport base sits on `#F8FAFC`. Modules, ledger tables, and analytical views sit on `#FFFFFF`.
- **Card Shadows:** Containers use a dual-tier ambient shadow: `0 1px 3px 0 rgba(15, 23, 42, 0.04), 0 1px 2px -1px rgba(15, 23, 42, 0.02)`. This conveys an anchored, tactile card surface without heavy drop artifacts.
- **Borders & Outlines:** Every card and modal features an explicit `1px` structural outline tinted with `#E2E8F0`. Over dark surfaces or interactive hover states, this transitions to `#CBD5E1`.
- **Floating Modals & Flyouts:** Dialogs, dropdown transaction filters, and calendar selectors step up elevation with an ambient shadow: `0 10px 15px -3px rgba(15, 23, 42, 0.06), 0 4px 6px -4px rgba(15, 23, 42, 0.04)` accompanied by a backdrop blur filter (`backdrop-blur-sm` over `rgba(15, 23, 42, 0.2)`).

## Shapes

The shape system leverages structured corner geometry (`roundedness: 2`, base 8px) to soften analytical density while maintaining architectural precision.

- **Base Radius (0.5rem / 8px):** Standard interactive elements including primary buttons, input text fields, balance summary tiles, and select pickers.
- **Container Radius (`rounded-lg` / 1rem / 16px):** Primary content modules, ledger breakdown cards, and chart containers.
- **Modal Radius (`rounded-xl` / 1.5rem / 24px):** Dialog windows, full-screen transaction drawers, and floating command palettes.
- **Micro Radius (0.25rem / 4px):** Checkboxes, inline code tokens, and small status indicator pips.
- **Pill Radius (`rounded-full`):** Category badges, cash-flow chips, and user avatars.

## Components

### Buttons
- **Primary:** Background `#0F172A`, text `#FFFFFF`, 0.5rem radius, height 40px, medium weight typography. Hover shifts to `#1E293B` with active state scaling slightly to 0.99.
- **Secondary / Subtle:** Surface `#FFFFFF`, border 1px `#E2E8F0`, text `#0F172A`. Hover shifts background to `#F8FAFC` and border to `#CBD5E1`.
- **Destructive / Income Actions:** Semantic buttons use light tint fills with high-contrast foreground text (e.g., `#FEF2F2` background with `#DC2626` text for expense removals).

### Form Inputs & Selectors
- Height 40px, background `#FFFFFF`, border 1px `#E2E8F0`, 0.5rem radius.
- **Placeholder:** `#94A3B8`.
- **Focus State:** 1px `#0F172A` outline accompanied by a 3px ring of `rgba(15, 23, 42, 0.08)`.
- **Numeric/Amount Inputs:** Accompanied by fixed left-aligned currency symbols in tabular weight, formatted with ample right-side text padding.

### Financial Chips & Category Tags
- **Income Chip:** Pill-shaped, background `rgba(16, 185, 129, 0.1)`, text `#047857`, border `1px solid rgba(16, 185, 129, 0.2)`. Preceded by a `+` prefix.
- **Expense Chip:** Pill-shaped, background `rgba(239, 68, 68, 0.1)`, text `#B91C1C`, border `1px solid rgba(239, 68, 68, 0.2)`. Preceded by a `-` prefix.
- **Neutral Category Tag:** Background `#F1F5F9`, text `#475569`, border `1px solid #E2E8F0`.

### Cards & Ledger Lists
- **Cards:** Pure `#FFFFFF` fill, 1px `#E2E8F0` border, `rounded-lg` (16px), 24px internal padding.
- **Transaction Rows:** Separated by a 1px border-bottom of `#F1F5F9`. Hover activates an instantaneous transition to `#F8FAFC`. Date groupings use sticky, small caps `label-sm` headers with `#64748B` typography.
- **Metrics Tiles:** Highlight balance aggregates with small muted labels stacked vertically above primary `currency-stat` values, supported by secondary trend indicators.

### Checkboxes & Radios
- Size 16px by 16px, border 1.5px `#CBD5E1`, radius 4px (checkbox) or circular (radio).
- Checked state uses `#0F172A` fill with a crisp white checkmark vector.