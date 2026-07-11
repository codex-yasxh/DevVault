# DevVault

> A developer identity layer — project memory, GitHub signal, and stack evolution in one local-first app.

## Why

GitHub shows commits. Nothing shows what you decided, why you chose a stack, what you shipped versus abandoned, or how your technical identity evolved over time. DevVault fills that gap. Your data never leaves your device.

<!-- TODO: add screenshots / GIF here -->

## Screens

| Screen       | What it does                                                                                                                               |
|--------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| **Home**     | Auto-generated developer type string ("KMP developer. Shipping actively. 2 shipped."), active project cards, GitHub pulse line             |
| **Projects** | Full project list with filter tabs (All / Building / Shipped / Paused / Abandoned), status badges, tech stack chips, add/edit bottom sheet |
| **GitHub**   | Consistency score (0-100), language breakdown bar chart, active repos, pull-to-refresh, offline cache banner                               |
| **Stack**    | Timeline view of technologies grouped by year, filterable by status (Comfortable / Learning / Shipped With / Dropped)                      |
| **Settings** | Placeholder — wired into navigation, not yet implemented                                                                                   |

Project Detail is also a placeholder (navigated to from project cards, shows ID only).

## Three Pillars

**Project Memory** — Every project you've ever touched. Name, status, platforms, tech stack, decision notes, GitHub/live links. CRUD through a bottom sheet form. Local SQLDelight database.

**GitHub Signal** — Connect a username. Fetches public repos, per-repo language bytes, and weekly commit activity via the GitHub API. Calculates a consistency score from the last 12 weeks. Aggregates language usage across your top 10 most-recently-pushed repos. Caches results for 1 hour with stale fallback on network failure.

**Stack Evolution** — Manually tag technologies with a status and date range. Grouped into a visual timeline by year. Filterable.

The full product and technical document is in [`DevVault.md`](./DevVault.md).

## Tech Stack

| Tool                                                                                          | Purpose                                          |
|-----------------------------------------------------------------------------------------------|--------------------------------------------------|
| [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)                        | Shared business logic across Android and Desktop |
| [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)                     | Shared UI                                        |
| [Ktor Client](https://ktor.io/docs/getting-started-ktor-client.html)                          | GitHub REST API calls                            |
| [SQLDelight](https://sqldelight.github.io/sqldelight/)                                        | Typesafe local database (multiplatform)          |
| [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)                      | JSON parsing for API responses and cache         |
| [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)                            | Structured concurrency + Flow                    |
| [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) | Key-value storage (username, display name)       |
| [Koin](https://insert-koin.io/)                                                               | Dependency injection                             |
| [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)                                | Epoch/time parsing for GitHub timestamps         |

## Architecture

Three Gradle modules:

```
androidApp/    — Android entry point (MainActivity, MainApplication, Koin bootstrap)
desktopApp/    — Desktop JVM entry point (Window + Koin, post-MVP)
shared/        — All shared code
  commonMain/  — Data, domain, presentation, DI (all platform-agnostic)
  androidMain/ — AndroidSqliteDriver, DataStore context, platform Koin module
  jvmMain/     — JdbcSqliteDriver, JVM DataStore path, platform Koin module
```

Within `shared/commonMain`:

```
data/
  local/       — SQLDelight schema (.sq files), DatabaseDriverFactory (expect/actual),
                  DataStore factory (expect/actual), entity-to-domain mappers
  remote/      — Ktor GitHubApiClient, DTOs (RepoDto, UserDto, CommitActivityDto)
  repository/  — GitHubRepository (cache-first), ProjectRepository, StackRepository
domain/
  model/       — Project, GitHubSignal, TechEntry data classes
  util/        — ConsistencyCalculator
presentation/
  home/        — HomeScreen + HomeViewModel (developer type generation lives here)
  projects/    — ProjectsScreen, AddEditProjectSheet, ProjectDetailScreen (placeholder),
                  ProjectsViewModel
  github/      — GitHubScreen + GitHubViewModel (error/empty/loading/success states)
  stack/       — StackScreen, AddTechSheet, timeline components, StackViewModel
  settings/    — SettingsScreen (placeholder)
  navigation/  — Screen sealed class (lightweight, no nav library)
  theme        — DevVaultTheme (Material3 light), status colors, spacing constants
di/            — AppModule (Ktor, repositories, ViewModels), platform modules
```

Navigation is a simple sealed class with `mutableStateOf<Screen>` — no Compose Navigation library. Bottom nav bar pushes detail screens (ProjectDetail, Settings) on top with back handling.

## Setup

### Prerequisites

- Android Studio (Ladybug or later)
- JDK 11+

### Run on Android

```bash
./gradlew :androidApp:assembleDebug
```

Or use the Android Studio run configuration on an emulator/device (API 26+).

### Run on Desktop

```bash
./gradlew :desktopApp:run
```

### Tests

```bash
./gradlew :shared:jvmTest
./gradlew :shared:testAndroidHostTest
```

### GitHub API (optional)

DevVault works without a GitHub token — it uses the public API which allows 60 requests/hour. For higher limits, add a Personal Access Token in `local.properties`:

```properties
github.pat=ghp_your_token_here
```

This gets read into `BuildConfig.GITHUB_PAT` and injected into the Ktor client via KOIN.

## Tech Stack Detail

The GitHub API layer fetches four things:

1. **Public repos** — sorted by `pushed_at`, top 50
2. **Per-repo languages** — fetched for the 10 most recently pushed repos (byte counts aggregated into percentages)
3. **Commit activity** — weekly commit counts from `/stats/commit_activity` for those same 10 repos (handles the GitHub 202 "computing" response with retries)
4. **Consistency score** — calculated from the last 12 weeks of aggregated commit data: 70% weight on active weeks ratio, 30% weight on average commits per active week

The caching strategy: check SQLDelight cache on load, serve if < 1 hour old, fetch fresh otherwise. On network error, serve stale cache with a banner. On no cache + no network, show an empty state with retry.

## Status

Code-complete for the Android MVP. Home, Projects, GitHub, and Stack screens are fully functional. Settings and Project Detail screens are navigation-wired placeholders. Desktop module compiles and shares the same codebase but is treated as post-MVP.

Play Store submission is pending developer registration — not an engineering gap.

## Project Doc

[`DevVault.md`](./DevVault.md) contains the full product spec, data models, API design, SQLDelight schema, caching strategy, build plan, and Play Store checklist.
