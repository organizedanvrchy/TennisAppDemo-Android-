# Tennis Application Demo

**Author:** Vimal Ramnarain, Jacob Wansor, Robert Nugent

**Institution:** Syracuse University, College of Engineering and Computer Science

**Location:** Syracuse, US

---

## Introduction
This application will be designed for Android phones and will provide users with easily accessible, up to date, and relevant statistics regarding professional tennis players and tournaments. 

The application main screen will include a feed regarding well known tennis players and tournaments. A persistent search bar will allow users to look for specific players and tournaments. Core player information will include full name, date of birth/age, nationality, dominant hand, height and weight, and profile picture—-along with career and performance data. Career and performance data will include win-loss records, current form (last 5 matches played or streaks), performance by surface (hard, clay, grass, indoor), head-to-head record against other players (such as rivals or current opponent in live tournaments), best of 3 and best of 5 performances, current ranking (ATP/ITF), highest career ranking, lowest career ranking, grand-slam titles won, Olympic medals won, years active, career prize money, prize money earned in the current season, sponsorships, and social media links. Tennis tournament information will encompass match scores, match dates, tournament rounds (for live tournaments, this will update as the tournament progresses), court surface, tournament location and category (Grand Slam, Masters 1000, or ATP 500). A persistent bottom navigation bar will allow users to return to the home screen, adjust profile settings, and log out of their account and return to the authentication page with just a single tap. 

The application will include an optional and free account creation feature that enables users to follow and receive notifications on specific players and tournaments. Users who choose not to create an account may still use the app as a guest, but will not be able to receive notifications from the application.

---

## System Architecture
The system architecture for this Android application relies on the Model-View-ViewModel (MVVM) pattern. This architecture was chosen because it provides a clear separation of concerns, making the codebase easier to maintain and extend over time. The model layer handles the data and business logic, the view is responsible only for the user interface, and the ViewModel acts as the mediator that binds data to the UI. This separation ensures that changes in one layer, such as updating the UI design or modifying data sources, can be done independently without heavily impacting the other layers. Additionally, using MVVM enables reactive UI updates through LiveData or StateFlow, which reduces boilerplate code and minimizes the risk of bugs when synchronizing UI with underlying data.
The system was primarily designed with the qualities of maintainability, testability, and scalability in mind. MVVM directly supports these qualities by isolating logic in the ViewModel, which makes it easier to write unit tests without relying on UI components. It also supports scalability because new features can be added by extending the model or adding new ViewModels without restructuring the entire application. Furthermore, this architecture enhances readability and collaboration, as different developers can focus on different layers of the system in parallel. Overall, MVVM is a strong fit for modern Android applications, such as this Tennis App, because it aligns well with lifecycle-aware components and supports long-term system growth while keeping the codebase organized and efficient.

---

## Requirements
| Requirement Number | Description                            | Priority    |
| ------------------ | -------------------------------------- | ----------- |
| UR 4.1             | Navigation via Bottom Bar              | 3           |
| UR 4.2             | Player Search Functionality            | 2           |
| UR 4.3             | View Player and Match Details          | 1 (Highest) |
| UR 4.4             | Authentication                         | 6           |
| UR 4.5             | User Privacy and Data Protection       | 7           |
| UR 4.6             | Track Specific Players and Tournaments | 9 (Lowest)  |
| UR 4.7             | Home Feed and Live Highlights          | 4           |
| UR 4.8             | Tournament and Match Browsing          | 5           |
| UR 4.9             | Profile Management and Account Actions | 8           |

### UR 4.1 — Navigation via Bottom Bar
- Description:
The system shall allow users to navigate between the Home Page, Profile Page, and Logout using a persistent bottom navigation bar.
- Priority: 3
- Functional System Requirements:
    - 4.1.1F The system shall display a bottom navigation bar on all primary user-facing pages (Home, Profile, Player, Match, Tournament).
    - 4.1.2F The system shall route the user to the appropriate page when a navigation item (Home, Profile, or Logout) is tapped.
    - 4.1.3F The system shall maintain the user session across navigations and only terminate the session when Logout is tapped.
    - 4.1.4F The system shall visually highlight the currently active navigation item. 
- Non-Functional System Requirements:
    - 4.1.5NF The bottom navigation bar shall be rendered within 200 milliseconds after any page load. (Performance)
    - 4.1.6NF If the target page fails to load due to a network issue, the system shall display a retry prompt and allow the user to attempt reloading.
    - 4.1.7NF The navigation bar shall be accessible and usable with one hand on standard smartphone screen sizes (≥ 5"). (Usability)
    - 4.1.8NF The navigation bar shall remain visually consistent and fixed at the bottom across all pages. (Design Consistency)
    - 4.1.9NF The system shall ignore rapid repeated taps (<300 ms apart) on the same navigation item to prevent accidental double navigation.
    - 4.1.10NF If the user selects Logout, the system shall clear all cached user data before redirecting to the Login screen.

### UR 4.2 — Player Search Functionality
- Description:
The system shall allow users to search for tennis players using a top search bar accessible across all relevant pages.
- Priority: 2
- Functional System Requirements:
    - 4.2.1F The system shall display a top search bar on the Home, Search, Player, Match, and Tournament pages.
    - 4.2.2F The system shall return a list of player cards (name, short info) based on the user’s input query.
    - 4.2.3F The system shall allow users to click on any player in the search results to open the Player Info Page.
    - 4.2.4F The system shall retain the last search query and results until the user clears the search bar or exits the Search Page.
    - 4.2.5F The system shall display a “No Results Found” message if no player matches the search query.
    - 4.2.6F The system shall suggest autocomplete search terms as the user types.
- Non-Functional System Requirements:
    - 4.2.7NF The search function shall return relevant results within 1 second of input completion. (Performance)
    - 4.2.8NF The system shall support partial and fuzzy search queries (e.g., typo tolerance or substring matches). (Functionality/Robustness)
    - 4.2.9NF The search bar shall have a consistent appearance (font size, padding, border) across all pages. (Design Consistency)
    - 4.2.10NF The system shall handle and recover gracefully if the search API request fails, displaying a “Retry” option.
    - 4.2.11NF Search suggestions shall not display more than 10 results at a time to maintain UI clarity.

### UR 4.3 — View Player and Match Details
- Description:
The system shall allow users to view a selected player’s profile, including recent match and tournament information.
- Priority: 1
- Functional System Requirements:
    - 4.3.1F The system shall display a Player Info Page with player name, image (optional), stats, and basic info.
    - 4.3.2F The system shall provide links or interactive elements to navigate to recent Match Pages for that player.
    - 4.3.3F The system shall provide links or interactive elements to navigate to the player's associated Tournament Pages.
    - 4.3.4F The system shall display a “No Data Available” message if player stats or match history cannot be retrieved.
    - 4.3.5F The system shall display placeholders for player image and stats while data is being fetched.
    - 4.3.6F The system shall sort matches chronologically, with the most recent matches appearing   
- Non-Functional System Requirements:
    - 4.3.7F The Player Info Page shall load within 2 seconds on a 4G network. (Performance)
    - 4.3.8F All match and tournament links shall be accessible with a minimum 44x44dp touch target size. (Accessibility)
    - 4.3.9F The layout of the Player Info Page shall be responsive to various screen sizes and orientations. (Usability/Responsive Design)
    - 4.3.10F The system shall allow retrying data retrieval if the initial load fails due to a network error.
    - 4.3.11F Player images shall be cached locally to speed up subsequent loads.

### UR 4.4 — Authentication
- Description:
The system shall allow users to register an account on the app to personalize their feed. Users may skip authentication and proceed as a guest. Guest accounts will have a default feed.
- Priority: 6
- Functional System Requirements:
    - 4.4.1F When opening the app, the first screen displayed will be a login page where the user can enter their username and password to login. 
    - 4.4.2F The login page will include a “Register”,“Forgot Password” and “Contact Us” button for account registration and recovery.
    - 4.4.3F The system shall have a register screen where the user provides an e-mail, username, password, confirmation of password, and security question to create an account.
    - 4.4.4F When a user registers an account, the system will ensure an account associated with that e-mail does not already exist. If one does exist, the system will notify the user and not create the account. 
    - 4.4.5F The system will enable account recovery strictly within the app when “Forgot Password” is selected. Users must correctly answer their security question to reset their password.
    - 4.4.6F The “Contact Us” display allows users to send an e-mail to a support team for further account issues. 
    - 4.4.7F The Log In screen will include a “Guest” button to skip the login process.
    - 4.4.8F Users can log out of their account by clicking the “Logout” icon on the bottom navigation tool bar after logging in.
- Non-Functional System Requirements:
    - 4.4.9NF After a user provides the correct username and password, navigation to the home page will take less than 1 second with a 5G mobile network . (Performance)
    - 4.4.10NF The system will be linked to and communicate with a Firebase authentication server to store registered user emails and passwords. (Design Constraint)
    - 4.4.11NF The system will be linked to and communicate with Firebase Firestore to store additional account information to include security questions, hashed answers to security questions, whether the account is locked, and most recent login timestamp. (Design Constraint).

### UR 4.5 — User privacy and data protection
- Description:
The system shall ensure user information retained by the system uses best security practices and protects the user’s privacy and data.
- Priority: 7
- Functional System Requirements:
    - 4.5.1F After user registration, the app displays a security policy pop up reminding users to not share their password and that the app will not initiate unsolicited contact with user or distribute/sell user data to 3rd parties.
    - 4.5.2F System will notify user and lock their account for 30 minutes after 3 unsuccessful login attempts
    - 4.5.3F System will notify user and lock their account for 30 minutes after 3 unsuccessful attempts to answer their security question
    - 4.5.4F When a user attempts to register an account with a password that does not meet security requirements, the user will be notified that account creation was unsuccessful due to the password not meeting requirements. The system then persistently displays password length and character requirements.
    - 4.5.5F System will automatically delete accounts that have been inactive for 365 consecutive days.
- Non-Functional System Requirements:
    - 4.5.6NF User passwords must be at least 8 characters and contain at least 1 special character. (Organizational Security Policy)
    - 4.5.7NF User account passwords will be hashed with the “bcrypt” algorithm prior to storage in Firebase Authentication Server. (Organizational Security Policy)
    - 4.5.8NF User account security questions will be hashed with the “bcrypt” algorithm prior to storage in Firestore database. (Organizational Security Policy)
    - 4.5.9NF System will ensure that each account is associated with a unique e-mail. (Organizational Security Policy)

### UR 4.6  — Track specific players and tournaments
- Description:
The system will enable users to subscribe to specific tennis players and tournaments. The home screen feed will automatically include information about players and tournaments that a user has subscribed to. The system will also send notifications if there are any updates about subscribed items.
- Priority: 9
- Functional System Requirements:
    - 4.6.1F All players and tournaments cards will include a toggleable bell icon in the top right corner  indicating that a user wishes to follow and track updates on those items. 
    - 4.6.2F Notifications will be sent to the user's phone any time a game, set, or match is won or lost involving a subscribed player.
    - 4.6.3F Notifications will be sent to the user’s phone any time a match is won or lost involving a subscribed tournament.
    - 4.6.4F Users may choose to turn off notifications within the profile screen.
    - 4.6.4F After a user has logged in, the home page tennis feed is updated to include the latest information regarding subscribed players and tournaments.
- Non-Functional System Requirements:
    - 4.6.4NF Database will retain player and tournament subscriptions for each account. (Design)
    - 4.6.5NF Firebase cloud messaging will allow push notifications to the user phone even when the application is not running. (Design) 

### UR 4.7 — Home Feed & Live Highlights
- Description:
The system shall present a Home feed with a featured live tournament card and quick modules for player stats and tournaments, each navigable to their detailed pages.
- Priority: 4
- Functional System Requirements:
    - 4.7.1F The Home page shall display a “Live Tournament” highlight card when live data is available; tapping it routes to the Tournament Page.
    - 4.7.2F The Home page shall show “Player Stats” and “Tournaments” modules that deep-link to Player and Tournament pages respectively.
    - 4.7.3F The system shall auto-refresh the Home feed content at most every 60 seconds while the app is foregrounded.
    - 4.7.4F If live data is unavailable, the system shall gracefully degrade to the most recent cached feed.
- Non-Functional System Requirements:
    - 4.7.5NF Live highlight and modules shall render above the fold on devices ≥ 5" screens. (Usability)
    - 4.7.6NF Home feed refresh shall not block user interaction and shall complete within 1.5 seconds on a 4G network. (Performance)

### UR 4.8 — Tournament & Match Browsing
- Description:
The system shall provide a Tournament Page listing matches with core details (players, scores, date/time, location) and enable navigation to Match details.
- Priority: 5
- Functional System Requirements:
    - 4.8.1F The Tournament Page shall list matches with opponent names, latest score, date/time, location, and a brief stats snippet.
    - 4.8.2F Tapping a match row shall open the Match Page with expanded details.
    - 4.8.3F Users shall be able to filter matches by status (Upcoming, Live, Completed) and sort by date/time.
    - 4.8.4F The Tournament Page search bar shall filter visible matches by player name or substring.
- Non-Functional System Requirements:
    - 4.8.5NF Match list scrolling shall maintain 60 FPS on mid-tier Android devices (2022+). (Performance)
    - 4.8.6NF Touch targets for each match row shall be ≥ 44×44dp. (Accessibility)

### UR 4.9 — Profile Management & Account Actions
- Description:
The system shall allow users to view and edit profile information (username, email, profile picture) and perform account actions (reset password, delete account) from the Profile page.
- Priority: 8
- Functional System Requirements:
    - 4.9.1F The Profile Page shall display username, email, and profile picture with an option to update each field.
    - 4.9.2F The system shall allow changing the profile picture by selecting a local image or taking a photo; images are stored in the user’s profile record.
    - 4.9.3F The Profile Page shall expose “Reset Password” and “Delete Account” actions; delete requires explicit confirmation and re-authentication.
    - 4.9.5F After a successful update, the system shall show an in-app confirmation and persist changes to Firestore/Auth.
- Non-Functional System Requirements:
    - 1.9.5NF Profile updates shall complete within 2 seconds on a 4G network. (Performance)
    - 1.9.6NF Images uploaded for profile pictures shall be validated (≤ 5 MB, JPEG/PNG/WebP) and downscaled to ≤ 512×512 px server-side. (Reliability/Security)
