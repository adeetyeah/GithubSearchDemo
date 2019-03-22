This is a demo Android project which can take in any organization's name and provide it's 3 most starred repositories. Clicking on any of the shown repositories will launch a webview and display the repository's details. This is a great excercise to understand the use of AsyncTasks, RxAndroid(RxJava) and web views.

Currently, there is no authentication for the user making the request to view an org's repositories. As a result of this, the network call maxes out at 60 requests/hour.

The code is currently defaulted to making the call using Observables, a feature introduced in RxAndroid. In ```GithubSearchActivity.java```, within the ```onCreate()``` method, you can find a call that is commented out - ```getRepositoriesUsingAsyncTask(organization)```

This call will perform the same logic using AsyncTasks. To use this call, please first comment out ```getRepositoriesUsingRxAndroid(organization)``` and uncomment ```getRepositoriesUsingAsyncTask(organization)```

To run the project, all that needs to be done is clone it, and then build it using Android Studio. You can directly install the app onto a device or can be installed on an emulator as well.