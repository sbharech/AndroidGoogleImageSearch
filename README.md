# AndroidGoogleImageSearch
An android app to do google search

An android Google Image Search app which allows a user to select search filters and paginate results infinitely.

Time spent: 15 hours spent in total

The app supports the followings:

1. User can enter a search query that will display a grid of image results from the Google Image API.
2. User can click on "settings" which allows selection of advanced search options to filter results
3. User can configure advanced search filters such as:
  Size (small, medium, large, extra-large)
  Color filter (black, blue, brown, gray, green, etc...)
  Type (faces, photo, clip art, line art)
  Site (espn.com)
  Subsequent searches will have any filters applied to the search results
4. User can tap on any image in results to see the image full-screen
5. User can scroll down “infinitely” to continue loading more image results (up to 8 pages)

The following supported features are:

1. Advanced: Robust error handling, check if internet is available, handle error cases, network failures
2. Advanced: Use the ActionBar SearchView or custom layout as the query box instead of an EditText
3. Advanced: User can share an image to their friends or email it to themselves
4. Advanced: Replace Filter Settings Activity with a lightweight modal overlay
5. Advanced: Improve the user interface and experiment with image assets and/or styling and coloring
6. Bonus: Use the StaggeredGridView to display improve the grid of image results
7. Bonus: User can zoom or pan images displayed in full-screen detail view

Additionally, user can open the actual site in the browswer.

The demo video:

![imagesearchdemo](https://cloud.githubusercontent.com/assets/2020366/6204388/50407670-b56f-11e4-8ba0-0aa7a922280e.gif)
