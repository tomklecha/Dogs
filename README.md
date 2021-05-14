# Dogs

App presenting pictures of the dogs based breed/sub breed
Given the list fetch from the internet we can pick up a dog and present 10 pictures of a dog
On each refreshing of the dogs picture list we will fetch new pictures, if there are more than 10.

Clicking picture will show the image in bigger scale.

When not connected to the internet, there will be empty list place holder.

When connected to internet we will fetch the list and save it on local storage.

# Technology

- Language: Kotlin - 100%
- Structure - MVVM using LiveData, Coroutines
- Jetpack components - Navigation, Data Binding
- Data storage - Shared Preferences
- Testing - JUnit, Mockk
- 3rd party - Retrofit, Glide, Gson

# TODOs

- UI test using FragmentScrenario
- Pagination of the picture list to fetch always 10 extra pictures
- Update dogs list to include browsing letter view type for clearer search through the list
- Include Flow on Repository layer
