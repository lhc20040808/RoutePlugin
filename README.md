



## Groovy语法





## Gradle插件基础

#### Gradle构建生命周期

- 初始化阶段(root project)
  - 执行setting.gradle脚本
- 配置阶段(project)
  - 执行各个目录下个build.gradle脚本
  - 根据项目配置生成一个任务依赖图，以便在下个阶段执行
- 执行阶段(task)
  - 把配置阶段生成的任务依赖图依次执行



#### 如何使用Gradle插件

- 二进制插件

  - 在根目录build.gradle中声明插件ID与版本号

  ```groovy
  dependencies {
      classpath "com.android.tools.build:gradle:4.1.3"
      classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
  }
  ```

  - 在子工程中应用插件

  ```groovy
  plugins {
      id 'com.android.application'
      id 'kotlin-android'
  }
  ```

  - 配置插件参数（取决于插件是否需要配置）

  ```groovy
  {
      compileSdkVersion 29
      buildToolsVersion "30.0.0"
  
      defaultConfig {
          applicationId "com.marco.gradletest"
          minSdkVersion 21
          targetSdkVersion 29
          versionCode 1
          versionName "1.0"
  
          testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
      }
  
      buildTypes {
          release {
              minifyEnabled false
              proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
          }
      }
      compileOptions {
          sourceCompatibility JavaVersion.VERSION_1_8
          targetCompatibility JavaVersion.VERSION_1_8
      }
      kotlinOptions {
          jvmTarget = '1.8'
      }
  }
  ```

- 脚本插件（轻量，属于工程中的独立脚本）

```groovy
//apply from: 路径
apply from:  project.rootProject.file("other.gradle")
```



#### 如何开发插件

- 建立插件工程
- 实现插件内部逻辑
- 发布与使用插件



APT工作原理

//TODO 



#### APT开发流程

- 定义注解
- 编写注解处理器
- 调用注解与注解处理器



## 页面路由框架开发

#### 页面路由功能梳理

- 标记页面
- 收集页面
- 生成文档
- 注册映射
- 打开页面



#### Gradle脚本管理技巧

- 版本号统一管理

- 维护敏感信息

  - 可以将敏感信息，比如账号密码放入local.properties文件中

  - ```groovy
    Properties properties = new Properties()
    properties.load(project.rootProject.file("local.properties").newDataInputStream())
    def name = properties.getProperty('USER_NAME')
    def pwd = properties.getProperty('USER_PWD')
    ```

- 脚本拆分

  - 将功能移入独立的脚本文件，比如放在工程目录下的`read_local_prop.gradle`中
  - 通过`apply from : project.rootProject.file('read_local_prop.gradle')`使用

- 编译期检测依赖的安全性

  - 应用场景
    - 禁止依赖snapshot版本的库
    - 禁止团队成员依赖额外图片库



#### BuildConfig