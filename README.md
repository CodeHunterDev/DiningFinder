<h1 align="center">Dining Finder</h1>
<h3 align="center">Android app to display the restaurants around using the current location</h3>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

### Screens 
<p align="center">
<img src="https://user-images.githubusercontent.com/8813304/126625413-3bb817e4-1331-41d8-b82f-c2991cd1e22c.png" width="17%"/>
<img src="https://user-images.githubusercontent.com/8813304/126625449-5c4ffc41-7017-4692-804e-8d37eca47276.png" width="17%"/>
<img src="https://user-images.githubusercontent.com/8813304/126625471-c69f3496-5c96-460c-b9b2-1c282acf6fca.png" width="17%"/>
<img src="https://user-images.githubusercontent.com/8813304/126626903-6ec23447-e762-4ae8-a66e-2d3a49a01deb.gif" width="17%"/>
</p>

## Tech stack & Open-source libraries
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Dagger Hilt](https://dagger.dev/hilt)

- JetPack
  - [Navigation](https://developer.android.com/guide/navigation)
  - [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

- Architecture
  - MVVM Architecture (Declarative View - ViewModel - Model) clean architecture
  - Repository pattern
  
- Android Studio Version
  - Android Studio Arctic Fox | 2020.3.1
  
feel free to test it 🤩
just create your own Api Key from [foursquare](https://foursquare.com/developers) and add it into [build.gradle.kts](https://github.com/Elbehiry/DiningFinder/blob/1c5cac3675bb3887b50e9fbae861b2149cd58475/shared/build.gradle.kts#L74) file. 😉
## License
```xml
Designed and developed by 2021 elbehiry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
