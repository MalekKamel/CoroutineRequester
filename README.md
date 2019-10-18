# CorotineRequester

<img src="https://github.com/ShabanKamell/CoroutineRequester/blob/master/blob/raw/logo.png" height="200">

A simple wrapper for Kotlin Coroutines that helps you:
- [ ] Make clean Coroutine requests
- [ ] Handle errors in a clean and effective way.

CorotineRequester does all the dirty work for you!

### Before CorotineRequester

``` kotlin
             try {
                 toggleLoading(show = true)
                 val result = dm.restaurantsRepo.all()
             } catch (error: Exception) {
                 // handle exception
                 toggleLoading(show = false)
             } finally {
                 toggleLoading(show = false)
             }
```

### After CorotineRequester

``` kotlin
coroutinesRequester.request { val result = dm.restaurantsRepo.all() }
```

#### Gradle:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
        implementation 'com.github.ShabanKamell:CorotineRequester:x.y.z'
}

```
(Please replace x, y and y with the latest version numbers:  [![](https://jitpack.io/v/ShabanKamell/CorotineRequester.svg)](https://jitpack.io/#ShabanKamell/CorotineRequester))

### Usage
#### Setup

``` kotlin
val presentable = object: Presentable {
            override fun showError(error: String) { showError.value = error }
            override fun showError(error: Int) { showErrorRes.value = error }
            override fun showLoading() { toggleLoading.value = true }
            override fun hideLoading() { toggleLoading.value = false }
            override fun onHandleErrorFailed() { showErrorRes.value = R.string.oops_something_went_wrong }
        }

       val requester = CorotineRequester.create(ErrorContract::class.java, presentable)
```

#### Server Error Contract
CoroutineRequester optionally parsers server error for you and shows the error automatically. Just implement `ErrorMessage`
interface in your server error model and return the error message.

``` kotlin
data class ErrorContract(private val message: String): ErrorMessage {
    override fun errorMessage(): String {
        return message
    }
}
```

#### Handle Errors
```kotlin
            CorotineRequester.throwableHandlers = listOf(
                    IoExceptionHandler(),
                    NoSuchElementHandler(),
                    OutOfMemoryErrorHandler()
            )
            CorotineRequester.httpHandlers = listOf(
                    TokenExpiredHandler(),
                    ServerErrorHandler()
            )
```

#### Error Handlers
Error handler is a class that extends
`HttpExceptionHandler`
``` kotlin
class ServerErrorHandler : HttpExceptionHandler() {

    override fun supportedErrors(): List<Int> {
        return listOf(500)
    }

    override fun handle(info: HttpExceptionInfo) {
        info.presentable.showError(R.string.oops_something_went_wrong)
    }
}
```

Or `NonHttpExceptionHandler`
``` kotin
class OutOfMemoryErrorHandler : ThrowableHandler<OutOfMemoryError>() {

    override fun supportedErrors(): List<Class<OutOfMemoryError>> {
        return listOf(OutOfMemoryError::class.java)
    }

    override fun handle(info: ThrowableInfo) {
        info.presentable.showError(R.string.no_memory_free_up_space)
    }
}
```

#### Customizing Requests
CorotineRequester gives you the full controll over any request
- [ ] Inline error handling
- [ ] Enable/Disable loading indicators
- [ ] Set Coroutine dispatcher

``` kotlin
val requestOptions = RequestOptions.Builder()
                .inlineErrorHandling { false }
                .showLoading(true)
                .dispatcher(Dispatchers.Main)
                .build()
requester.request(requestOptions) { dm.restaurantsRepo.all() }
```

Here're all request options and default values

| **Option** | **Type** | **Default** |
| ------------- | ------------- | ------------- |
| **inlineHandling**           | Lambda       | null |
| **showLoading**              | Boolean      | true |
| **subscribeOnScheduler**     | CoroutineDispatcher    | Dispatchers.Main |

### Retrying The Request
You can retry the request in any error handler class by calling `HttpExceptionInfo.retryRequest()`.
This is very useful when you receive `401` indicating the token was EXPIRED. To fix the issue, call the refresh token API inside the handler, then retry the request again without interrupting the user. For more, look at `TokenExpiredHandler` in sample module.

### Best Practices
- [ ] Setup `CorotineRequester` only once in `BaseViewModel` and reuse in the whole app.
- [ ] Initialize error handlers only once.
- [ ] Use a scope for runnig coroutine. i.e. viewModelScope.

#### Look at 'sample' module for the full code. For more advanced example.

### License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
