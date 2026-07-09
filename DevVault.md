# DevVault — Full Product & Technical Document

> **One-liner:** Your developer identity layer. Not a GitHub wrapper. Not a stat's dashboard. The single place that answers: *"What kind of developer am I right now?"*
 
---

## 1. Product Identity

### What it IS
- A developer self-awareness tool
- Project memory + GitHub signal + stack evolution in one place
- Built by a developer, for developers, during placement season
### What it is NOT
- A GitHub stats clone (those exist, nobody cares)
- A LeetCode tracker (scope creep, different product)
- A to-do app with dev branding
- A vanity metric dashboard
### The Core Problem
Developers build things across months/years. GitHub shows commits. Nothing shows:
- What you actually decided and why
- How your stack evolved
- What you've shipped vs what's dead
- Who you are as a developer right now
  DevVault fills that gap.

---

## 2. Three Pillars

### Pillar 1 — Project Memory
Every project you've ever touched, tracked properly.

Each project entry has:
- Name + one-line description
- Status: `BUILDING` | `SHIPPED` | `PAUSED` | `ABANDONED`
- Tech stack tags (e.g. KMP, Ktor, SQLDelight, Jetpack Compose)
- Platform: Android / iOS / Desktop / Web / Backend
- One decision note — *why you built it, what you chose and why*
- GitHub repo link (optional)
- Play Store / live link (optional)
- Start date + last updated
  This is local-first. User owns their data. No account needed.

---

### Pillar 2 — GitHub Signal
Not raw stats. Filtered signal.

What you fetch:
- Public repos (name, description, language, stars, last pushed)
- Commit activity (weekly breakdown for last 6 months)
- Language usage across all repos (aggregate %)
- Recent pushes (last 5 repos touched)
  What you surface:
- **Consistency Score** — are you coding regularly or in bursts?
- **Language Trend** — top 3 languages by actual usage, trending up/down
- **Active vs Dead repos** — repos pushed in last 30 days vs not
- **Most serious project** — repo with most commits + recent activity
  What you DON'T surface:
- Total commit count (vanity)
- Contribution heatmap (GitHub already has one)
- Follower count (irrelevant)
---

### Pillar 3 — Stack Evolution
A living record of your technical identity.

User manually tags technologies they've actually shipped with (not "learning"). Each tag has:
- Technology name
- Status: `COMFORTABLE` | `LEARNING` | `SHIPPED WITH` | `DROPPED`
- First used (month/year)
- Last used
  Over time this becomes a timeline of who you were technically at each stage.

---

## 3. Screens

### Screen 1 — Home (The Identity Screen)
**One question answered in 3 seconds: "What kind of developer am I right now?"**

Layout:
- Header: "Hey [name]" + current date
- Developer Type Card: Auto-generated from data. e.g. *"Android + KMP developer. Mostly Kotlin. Shipping actively."* (3 lines max, derived from GitHub language data + project statuses)
- Active Projects: Cards showing BUILDING projects only (max 3, tap to see all)
- GitHub Pulse: Single line — "Last pushed 2 days ago · 4 repos active this month"
- Quick Add button: Floating action button to add a new project entry
  No scrolling wall of stats. Clean, readable in 5 seconds.

---

### Screen 2 — Projects
Full project memory list.

- Filter tabs: All | Building | Shipped | Paused | Abandoned
- Each card: Name, status badge, tech stack chips, last updated
- Tap → Project Detail Screen
- FAB → Add Project sheet
  **Add/Edit Project Sheet (bottom sheet):**
- Name (text field)
- Description (text field, 1 line)
- Status (segmented control)
- Platform (multi-select chips)
- Tech stack (chip input — type + add)
- Decision note (text field, optional)
- GitHub URL (text field, optional)
- Live URL (text field, optional)
---

### Screen 3 — GitHub
GitHub Signal screen.

- Username input at top (editable, saved to prefs)
- Consistency Scorecard (calculated from weekly commit data)
- Language breakdown (horizontal bar chart — top 5 languages by %)
- Active repos section (pushed in last 30 days)
- Language trend (this month vs 3 months ago)
- Pull-to-refresh
  Empty state: "Add your GitHub username to see your signal"
  Error state: "Couldn't reach GitHub — cached data shown"

---

### Screen 4 — Stack
Stack Evolution screen.

- Timeline view: Technologies grouped by "first used" year/month
- Filter: All | Comfortable | Learning | Shipped With | Dropped
- Each chip: Technology name + status color
- Tap chip → edit status or remove
- FAB → Add technology sheet
  **Visual:** Not a list. A horizontal scroll timeline by year. Past on left, present on right.

---

### Screen 5 — Settings
- GitHub username (editable)
- Your name (used in home greeting)
- Clear GitHub cache
- Export data (JSON dump of all projects + stack — user owns their data)
- App version
- GitHub repo link (open source? your call)
---

## 4. Architecture

```
DevVault/
├── composeApp/
│   ├── commonMain/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── DatabaseDriverFactory.kt (expect)
│   │   │   │   ├── DevVaultDatabase.kt
│   │   │   │   └── sqldelight/
│   │   │   │       ├── Project.sq
│   │   │   │       ├── TechStack.sq
│   │   │   │       └── GithubCache.sq
│   │   │   ├── remote/
│   │   │   │   ├── GitHubApiClient.kt
│   │   │   │   └── dto/
│   │   │   │       ├── RepoDto.kt
│   │   │   │       ├── CommitActivityDto.kt
│   │   │   │       └── UserDto.kt
│   │   │   └── repository/
│   │   │       ├── ProjectRepository.kt
│   │   │       ├── GitHubRepository.kt
│   │   │       └── StackRepository.kt
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── Project.kt
│   │   │   │   ├── GitHubSignal.kt
│   │   │   │   └── TechEntry.kt
│   │   │   └── usecase/
│   │   │       ├── GetGitHubSignalUseCase.kt
│   │   │       ├── GenerateDeveloperTypeUseCase.kt
│   │   │       └── GetActiveProjectsUseCase.kt
│   │   ├── presentation/
│   │   │   ├── home/
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   └── HomeViewModel.kt
│   │   │   ├── projects/
│   │   │   │   ├── ProjectsScreen.kt
│   │   │   │   ├── ProjectDetailScreen.kt
│   │   │   │   └── ProjectsViewModel.kt
│   │   │   ├── github/
│   │   │   │   ├── GitHubScreen.kt
│   │   │   │   └── GitHubViewModel.kt
│   │   │   ├── stack/
│   │   │   │   ├── StackScreen.kt
│   │   │   │   └── StackViewModel.kt
│   │   │   └── settings/
│   │   │       ├── SettingsScreen.kt
│   │   │       └── SettingsViewModel.kt
│   │   ├── di/
│   │   │   └── AppModule.kt
│   │   └── App.kt
│   ├── androidMain/
│   │   ├── MainActivity.kt
│   │   └── DatabaseDriverFactory.android.kt
│   └── jvmMain/ (Desktop — post-MVP)
│       ├── main.kt
│       └── DatabaseDriverFactory.jvm.kt
```
 
---

## 5. Tech Stack

| Tool                  | Purpose           | Why                                  |
|-----------------------|-------------------|--------------------------------------|
| Kotlin Multiplatform  | Shared logic      | Your core skill, portfolio signal    |
| Compose Multiplatform | Shared UI         | One UI codebase                      |
| Ktor Client           | GitHub API calls  | KMP-native HTTP, coroutine-first     |
| SQLDelight            | Local DB          | KMP-native, typesafe, you've used it |
| kotlinx.coroutines    | Async             | Already know it                      |
| kotlinx.serialization | JSON parsing      | Works with Ktor out of box           |
| DataStore (KMP)       | Preferences       | Username, name storage               |
| Koin                  | DI                | Lightweight, KMP-compatible          |
| AndroidSqliteDriver   | Android DB driver | Same as Burnout.kt                   |

**New things you'll learn in this project vs Burnout.kt:**
- Ktor client setup + interceptors
- kotlinx.serialization with real API responses
- Error handling at network layer (retry, cache fallback)
- DataStore for preferences
- KOIN for dependency injection (optional but worth it)
---

## 6. Data Models

### Project (local)
```kotlin
data class Project(
    val id: Long,
    val name: String,
    val description: String,
    val status: ProjectStatus,
    val platforms: List<Platform>,
    val techStack: List<String>,
    val decisionNote: String?,
    val githubUrl: String?,
    val liveUrl: String?,
    val startedAt: Long,
    val updatedAt: Long
)
 
enum class ProjectStatus { BUILDING, SHIPPED, PAUSED, ABANDONED }
enum class Platform { ANDROID, IOS, DESKTOP, WEB, BACKEND }
```

### GitHubSignal (remote + cached)
```kotlin
data class GitHubSignal(
    val username: String,
    val consistencyScore: Int,        // 0–100, calculated
    val topLanguages: List<LanguageUsage>,
    val activeRepos: List<RepoSummary>,
    val lastPushedDaysAgo: Int,
    val activeRepoCountThisMonth: Int,
    val fetchedAt: Long
)
 
data class LanguageUsage(al language: String, val percentage: Float, val bytes: Long)
data class RepoSummary(val name: String, val description: String?, val language: String?, val pushedAt: Long)
```

### TechEntry (local)
```kotlin
data class TechEntry(
    val id: Long,
    val name: String,
    val status: TechStatus,
    val firstUsedMonthYear: String,   // "2024-03"
    val lastUsedMonthYear: String?
)
 
enum class TechStatus { COMFORTABLE, LEARNING, SHIPPED_WITH, DROPPED }
```
 
---

## 7. GitHub API Layer

**Base URL:** `https://api.github.com`

**Endpoints you need:**

```
GET /users/{username}
→ avatar_url, name, bio, public_repos, followers
 
GET /users/{username}/repos?sort=pushed&per_page=50
→ name, description, language, stargazers_count, pushed_at, fork
 
GET /users/{username}/stats/commit_activity
→ weekly commit counts for last 52 weeks
Note: Returns 202 (computing) on first hit — retry after 1-2 seconds
 
GET /users/{username}/languages (not a real endpoint)
→ You aggregate this: for each repo, GET /repos/{username}/{repo}/languages
   Returns { "Kotlin": 12400, "Swift": 3200 }
   Aggregate across repos, calculate percentages
```

**Ktor client setup:**
```kotlin
val client = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
    }
    defaultRequest {
        header("Accept", "application/vnd.github.v3+json")
        header("User-Agent", "DevVault-App")
        // Optional: header("Authorization", "token $pat")
        // PAT removes 60 req/hr limit → 5000 req/hr
    }
}
```

**Rate limits (no auth):** 60 requests/hour. With PAT: 5000/hour.
For MVP: let the user optionally add a PAT in settings. Most won't hit 60 without it.

**Consistency Score calculation:**
```kotlin
fun calculateConsistencyScore(weeklyCommits: List<Int>): Int {
    val last12Weeks = weeklyCommits.takeLast(12)
    val activeWeeks = last12Weeks.count { it > 0 }
    val avgCommitsPerActiveWeek = if (activeWeeks > 0)
        last12Weeks.sum().toFloat() / activeWeeks else 0f
    val consistency = (activeWeeks / 12f) * 70     // 70% weight on regularity
    val volume = minOf(avgCommitsPerActiveWeek / 10f, 1f) * 30  // 30% weight on volume
    return (consistency + volume).toInt().coerceIn(0, 100)
}
```

**Language aggregation:**
```kotlin
// Fetch languages for top 10 repos (by last pushed) only
// Don't fetch all 50 — rate limit risk
suspend fun aggregateLanguages(repos: List<RepoDto>): List<LanguageUsage> {
    val totals = mutableMapOf<String, Long>()
    repos.sortedByDescending { it.pushedAt }.take(10).forEach { repo ->
        val langs = client.get("$BASE/repos/${repo.fullName}/languages")
            .body<Map<String, Long>>()
        langs.forEach { (lang, bytes) ->
            totals[lang] = (totals[lang] ?: 0L) + bytes
        }
    }
    val total = totals.values.sum().toFloat()
    return totals.entries
        .sortedByDescending { it.value }
        .take(5)
        .map { LanguageUsage(it.key, it.value / total * 100) }
}
```
 
---

## 8. Developer Type Generator

The home screen's core feature. Auto-generated string from real data.

```kotlin
fun generateDeveloperType(
    signal: GitHubSignal,
    projects: List<Project>,
    stack: List<TechEntry>
): String {
    val topLang = signal.topLanguages.firstOrNull()?.language ?: "code"
    val isKmp = stack.any { it.name.contains("KMP", ignoreCase = true)
        && it.status == TechStatus.SHIPPED_WITH }
    val shippedCount = projects.count { it.status == ProjectStatus.SHIPPED }
    val isActive = signal.lastPushedDaysAgo < 7
 
    return buildString {
        // Platform identity
        if (isKmp) append("KMP developer. ")
        else append("$topLang developer. ")
 
        // Activity signal
        if (isActive && signal.activeRepoCountThisMonth >= 2)
            append("Shipping actively. ")
        else if (isActive)
            append("Building actively. ")
        else
            append("Between projects. ")
 
        // Experience signal
        when {
            shippedCount >= 5 -> append("$shippedCount shipped projects.")
            shippedCount >= 2 -> append("$shippedCount shipped. More in progress.")
            shippedCount == 1 -> append("First ship done.")
            else -> append("First project underway.")
        }
    }
}
```

Example output: *"KMP developer. Shipping actively. 2 shipped. More in progress."*
 
---

## 9. SQLDelight Schema

```sql
-- Project.sq
CREATE TABLE ProjectEntity (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    status TEXT NOT NULL,
    platforms TEXT NOT NULL,
    techStack TEXT NOT NULL,
    decisionNote TEXT,
    githubUrl TEXT,
    liveUrl TEXT,
    startedAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL
);
 
getAllProjects:
SELECT * FROM ProjectEntity ORDER BY updatedAt DESC;
 
getProjectsByStatus:
SELECT * FROM ProjectEntity WHERE status = :status ORDER BY updatedAt DESC;
 
insertProject:
INSERT OR REPLACE INTO ProjectEntity VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
 
deleteProject:
DELETE FROM ProjectEntity WHERE id = :id;
 
 
-- TechStack.sq
CREATE TABLE TechEntity (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    status TEXT NOT NULL,
    firstUsed TEXT NOT NULL,
    lastUsed TEXT
);
 
getAllTech:
SELECT * FROM TechEntity ORDER BY firstUsed DESC;
 
insertTech:
INSERT OR REPLACE INTO TechEntity VALUES (?, ?, ?, ?, ?);
 
updateTechStatus:
UPDATE TechEntity SET status = :status WHERE id = :id;
 
 
-- GithubCache.sq
CREATE TABLE GithubCacheEntity (
    username TEXT NOT NULL PRIMARY KEY,
    signalJson TEXT NOT NULL,
    fetchedAt INTEGER NOT NULL
);
 
getCachedSignal:
SELECT * FROM GithubCacheEntity WHERE username = :username;
 
upsertCache:
INSERT OR REPLACE INTO GithubCacheEntity VALUES (?, ?, ?);
```
 
---

## 10. Caching Strategy

GitHub data doesn't need to be real-time. Here's the rule:

- Cache TTL: **1 hour** for GitHub signal
- On open: check cache age → if < 1hr, serve cache. If > 1hr, fetch fresh + update cache
- On network error: serve stale cache + show "cached data" label
- On no cache + no network: show empty state with retry button
```kotlin
class GitHubRepository(
    private val api: GitHubApiClient,
    private val db: DevVaultDatabase,
    private val prefs: DataStore<Preferences>
) {
    suspend fun getSignal(username: String): Result<GitHubSignal> {
        val cached = db.githubCacheQueries.getCachedSignal(username).executeAsOneOrNull()
        val isStale = cached == null ||
            System.currentTimeMillis() - cached.fetchedAt > 3_600_000L
 
        if (!isStale && cached != null) {
            return Result.success(Json.decodeFromString(cached.signalJson))
        }
 
        return try {
            val fresh = api.fetchSignal(username)
            db.githubCacheQueries.upsertCache(
                username, Json.encodeToString(fresh), System.currentTimeMillis()
            )
            Result.success(fresh)
        } catch (e: Exception) {
            if (cached != null) Result.success(Json.decodeFromString(cached.signalJson))
            else Result.failure(e)
        }
    }
}
```
 
---

## 11. Day-by-Day Build Plan (15 days, 1hr/day)

| Day | What you build                                                        |
|-----|-----------------------------------------------------------------------|
| 1   | Project setup, Ktor dependency, Koin setup, DataStore                 |
| 2   | SQLDelight schema, all 3 tables, DatabaseDriverFactory                |
| 3   | GitHub API client, /users and /repos endpoints working                |
| 4   | Language aggregation, commit activity fetch, GitHubSignal model built |
| 5   | GitHubRepository with caching logic                                   |
| 6   | GitHub screen UI — username input, signal cards                       |
| 7   | ProjectRepository CRUD, all SQL queries wired                         |
| 8   | Projects screen — list, filter tabs, status badges                    |
| 9   | Add/Edit project bottom sheet, all fields working                     |
| 10  | Stack screen — tech entries, timeline grouping by year                |
| 11  | Home screen — Developer Type generator, active projects, GitHub pulse |
| 12  | Navigation (bottom nav, back stack), screen wiring end-to-end         |
| 13  | Empty states, error states, loading states — all screens              |
| 14  | UI polish, app icon, typography consistency                           |
| 15  | Internal testing track submission on Play Store                       |
 
---

## 12. Play Store Checklist (before submission)

- [ ] App icon (512x512 + adaptive icon for Android)
- [ ] Feature graphic (1024x500)
- [ ] Screenshots (min 2, recommended 4-5) — phone screenshots
- [ ] Short description (80 chars max)
- [ ] Full description (4000 chars max)
- [ ] Privacy policy URL (required — even for no-account apps) — use a free generator
- [ ] Content rating questionnaire completed
- [ ] App signed with release keystore (store this safely — losing it = can't update)
- [ ] minSdk set appropriately (recommend 26+)
- [ ] No crashes on cold start, rotation, back navigation
- [ ] Test on at least 2 different screen sizes
  **Privacy policy note:** DevVault stores everything locally. No server. No account. That's actually your USP — put it in the description. "Your data never leaves your device."

---

## 13. What Makes This Portfolio-Worthy

By the time DevVault ships, you will have demonstrated:

| Concept                      | Where it shows up                  |
|------------------------------|------------------------------------|
| Ktor client + error handling | GitHub API layer                   |
| kotlinx.serialization        | DTO parsing                        |
| SQLDelight multiplatform     | All 3 local tables                 |
| Repository pattern           | ProjectRepo, GitHubRepo, StackRepo |
| StateFlow + ViewModel        | All 5 screens                      |
| Offline-first architecture   | Cache strategy                     |
| expect/actual                | DatabaseDriverFactory              |
| Compose Multiplatform        | Entire UI                          |
| Koin DI                      | AppModule                          |
| Real Play Store ship         | The listing itself                 |

That's a complete modern Android/KMP architecture in one app. With a real user problem. Shipped publicly.
 
---

## 14. What to Say in Interviews

**"Tell me about a project you're proud of."**

*"I built DevVault — a developer identity app on the Play Store. Built with Kotlin Multiplatform so the business logic runs on both Android and Desktop. It pulls GitHub data via Ktor, caches it locally with SQLDelight using an offline-first strategy, and generates a developer profile from real activity data. I built it because I needed it myself — no single place showed me what kind of developer I actually was. The architecture uses Repository pattern, StateFlow ViewModels, and Koin for DI. The interesting engineering problem was the language aggregation — GitHub doesn't give you totals, so I had to fetch per-repo language data, aggregate byte counts across repos, and calculate weighted percentages. Runs on Play Store."*

That answer is 90 seconds. It covers architecture, real problem, engineering decisions, and shipping. No interviewer stops you there.
 
---

*Document version: June 2026. Built for DevVault v1 MVP.*
 