# Simple DataStore Sample App

The purpose of this application is to test the Amplify DataStore.

<img src="./screenshot.png" width="300px"/>
<img src="./landscape-screenshot.png" width="500px" />

## Installation
To use, install, and evaluate this application, please do the following:

1. Checkout and build Amplify Android @ `main`. Publish to your local repository:
```sh
./gradlew build publishToMavenLocal
```
Detailed instructions are in the [Contributing
Guide](https://github.com/aws-amplify/amplify-android/blob/master/CONTRIBUTING.md#getting-started).

2. Import this current project into Android studio. Try to build it.

3. Using the [`schema.graphql`](./schema.graphql) as below,
   generate models and deploy an AppSync backend. Make sure that this
   step updates your local configuration.
```graphql
enum PostStatus {
  ACTIVE
  INACTIVE
}

type Post @model {
  id: ID!
  title: String!
  rating: Int!
  status: PostStatus!
}
```

Use `amplify init`, `amplify add api`. Follow the [guide to add a DataStore endpoint](https://docs.amplify.aws/lib/datastore/getting-started/q/platform/android#option-2-use-amplify-cli) --
except choose "Cognito User Pools" as the auth type, instead of API key. Run `amplify push` when done, and wait.

4. Create a valid Cognito user, in your the user pool you just created.
```sh
aws cognito-idp admin-create-user \
    --user-pool-id <pool_id_that_amplify_created> \
    --username <some_username>

aws cognito-idp admin-set-user-password \
    --user-pool-id <pool_id_that_amplify_created> \
    --username <some_username> \
    --password <some_password> \
    --permanent
```

Add the `<some_username>` and `<some_password>` values into
`app/src/main/res/values/sign_in.xml`. For example, your `sign_in.xml`
might look as below:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="username" type="id">koolusr44</string>
    <string name="password" type="id">koolpass99</string>
</resources>
```

5. Re-build this current app and install it on an Android device.

## Usage
For repeatable results, you should always swipe-kill the app, before
running a manual test case:
```sh
adb shell am force-stop com.amplifyframework.datastore.sample
adb uninstall com.amplifyframework.datastore.sample
```

You may also manually update/clear your AppSync backend, while using
this app. It can be useful to do this _before_ opening this app, so that
the app won't sync a bunch of old data. The script in `clear-db.sh` is
useful for this.

The best way to understand the behavior of the different buttons is to
read the
[`MainActivity.java`](https://github.com/jamesonwilliams/simple-datastore/blob/master/app/src/main/java/com/amplifyframework/datastore/sample/MainActivity.java).

## Portrait Mode
These are the behaviors of the buttons in the portrait mode of the UI:

1. _CREATE A POST_: Create a single post, with some random data. Display
   this post on the screen.
2. _UPDATE ALL_: Query all posts, and update them all. Display each
   update on the screen.
3. _LIST ALL POSTS_: List all current posts, onto the screen.
4. _DELETE ALL POSTS_: Query all posts, and delete each one. Display
   each deletion to the screen.
5. _SUBSCRIBE..._: Start a new subscription. The subscription will emit
   information about its lifecycle onto the screen.
6. _STOP EVERYTHING_: Stop/cancel any ongoing operation(s). This will
   cancel mutation, and it would also tear down a subscription.
7. _CLEAR LOG_: This doesn't do anything to the DataStore. This just
   clears the log in the app window.
8. _SIGN IN_: Sign in to Cognito using the username and password provided
   in your `app/src/main/res/values/sign_in.xml`

## Landscape Mode

The left side shows client-side state, the right side shows AppSync side
state. As of this time, the right side doesn't include _delete,
_lastChangedAt, _version, so it is of limited utility.

