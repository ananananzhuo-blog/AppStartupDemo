> 关注我的公众号 **安安安安卓** 学习更多知识
关注公众号学习更多知识
![](https://files.mdnice.com/user/15648/404c2ab2-9a89-40cf-ba1c-02df017a4ae8.jpg)


[代码 git 地址](https://github.com/ananananzhuo-blog/AppStartupDemo.git)

# App Startup

## 介绍作用

![](https://files.mdnice.com/user/15648/856d1e83-e20f-4124-a337-ddbdec0bef6a.png)

这是官网的截图，大意就是 App Startup 是一种用来在 app 启动时候规范初始化数据的 library。同时使用 App Startup 可以解决我们平时滥用 ContentProvider 导致的启动变慢问题。

还有一点，App Startup 可以用于 app 开发，也可以用来进行 sdk 开发

## App Startup 的优势

1. 平时使用 ContentProvider 自动获取 ApplicationContext 的方式管理混乱，并且多个 ContentProvider 初始化的方式也无法保证初始化的顺序

2. 统一管理的方式可以明显提升 app 初始化速度，注：仅限于用较多 ContentProvider 来初始化应用的 app，反之不是不能用，只是没有优化效果

## 项目依赖

```
dependencies {
    implementation("androidx.startup:startup-runtime:1.0.0")
}
```

## 使用 AppStartup 初始化全局单例对象(main 分支)

1. Car 对象

```
class Car(private val name: String) {
    companion object {
        var instance: Car? = null
        fun getInstance(name: String): Car {
            if (instance == null) {
                instance = Car(name)
            }
            return instance!!
        }
    }

    override fun toString(): String {
        return "$name  ${Random.nextInt(100)}"
    }
}
```

2. 首先需要实现一个 Initializer

```
class AndroidInitializer : Initializer<Car> {
    override fun create(context: Context): Car {
        return Car.getInstance("出租车")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
```

3. 在代码中注册 AndroidInitializer

```
 <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="${applicationId}.androidx-startup"
            android:exported="false"
          >
            <meta-data
                android:name="com.ananananzhuo.appstartupdemo.AndroidInitializer"
                android:value="androidx.startup" />
        </provider>
```

3. 分析

本例中 Car 对象，Car 对象内部维护了一个全局单例方法 getInstance。

前面说了，AppStartup 是用来维护全局单例的，那么实际上这个单例的初始化就是通过我们定义的 AndroidInitializer 对象 create 方法来初始化的。

4. 我们会在 MainActivity 中调用 Car 的 toString 方法，代码如下

```
 logEE(Car.getInstance("小汽车：").toString())
        logEE(Car.getInstance("小汽车：").toString())
        logEE(Car.getInstance("小汽车：").toString())
```

我们调用了，三次 toString 方法

代码输出如下：

![](https://files.mdnice.com/user/15648/fb09d0d0-cddf-4e07-8bb0-0fce91e39813.png)

我们 MainActivity 中代码 getInstance 传入的参数是 "小汽车",但是打印的却是 "出租车"。查看 AndroidInitializer 中的代码发现，我们在 AndroidInitializer 中的 create 方法中创建对象的参数是 "出租车"。

由此可以证明，我们的全局 Car 单例在 AndroidInitializer 中就已经初始化完成了。

## 手动初始化组件

上一节中我们使用在 Manifest 中注册组件的方式实现 Car 对象的自动初始化。

但是，实际上我们是可以不在 Manifest 中注册的方式实现初始化的，手动初始化的方式如下：

```
 AppInitializer.getInstance(this)
            .initializeComponent(AndroidInitializer::class.java)
```

这种方式的弊端是一次只能初始化一个组件

## 实现相互依赖的多实例的初始化(分支：multimodule)

通过上一节的学习，你可能会有这样的疑问：AppStartup 啥用没有吧，我直接在 Application 中一行代码初始化不香吗，非要用你这种方式？？？

那么现在我就要用 AppStartup 实现多实例的初始化，让你进一步了解 AppStartup 的应用

我们这一节的逻辑先描述一下：

本例中我们需要创建两个对象，Person 和 Noodle，两者都是全局单例的。

Person 持有 Noodle 对象的引用，

Person 中有一个 eat 方法，本例中我们的 eat 会输出一行 "某某人" 吃 "面条" 的日志

废话不多说，上代码：

不要嫌代码长，都是一看就懂的逻辑

1. Person 和 Noodle

```
class Person(val name:String) {
    private var noodle: Noodle? = null
    companion object {
        private var instance: Person? = null
        fun getInstance(name:String): Person {
            if (instance == null) {
                instance = Person(name)
            }
            return instance!!
        }
    }

    fun addNoodle(paramsnoodle: Noodle) {
        noodle = paramsnoodle
    }


    fun eat() {
        logEE("${name} 吃 ${noodle?.name}")
    }
}
```

```
class Noodle {
    val name = "面条"

    companion object {
        private var instance: Noodle? = null
        fun getInstance(): Noodle {
            if (instance == null) {
                instance = Noodle()
            }
            return instance!!
        }
    }
}
```

2. PersonInitializer、NoodleInitializer

```
class PersonInitializer : Initializer<Person> {
    override fun create(context: Context): Person {
        return Person.getInstance("李白").apply {
            addNoodle(Noodle.getInstance())
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(NoodleInitializer::class.java)
    }
}
```

```

class NoodleInitializer:Initializer<Noodle> {
    override fun create(context: Context): Noodle {
        return Noodle.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
```

这两个组件中 PersonInitializer 的 create 方法中创建了 Person 的实例，并向里面添加 Noodle 的实例。

**划重点：**

PersonInitializer 的 dependencies 方法中返回了 mutableListOf(NoodleInitializer::class.java)。这句代码的意思是在 PersonInitializer 中的 Person 初始化之前会先初始化 NoodleInitializer 中的 Noodle 实例，然后当 PersonInitializer 中 addNoodle 的时候 Noodle 全局单例已经创建好了。

3. 调用吃面条方法

```
Person.getInstance("杜甫").eat()
```

4. 打印日志输出

![](https://files.mdnice.com/user/15648/14c4c4d1-170e-4f58-b383-36066c7e92d4.png)

日志输出符合我们的预期

多实例的注册组件方式如下，我们将 PersonInitializer、NoodleInitializer 都被注册到 meta-data 中了。

> 实际上，NoodleInitializer 的组件是完全可以不注册的，因为在 PersonInitializer 的 dependencies 中已经声明了 NoodleInitializer 组件。

```
  <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="${applicationId}.androidx-startup"
            android:exported="false">
            <meta-data
                android:name="com.ananananzhuo.appstartupdemo.PersonInitializer"
                android:value="androidx.startup" />
            <meta-data
                android:name="com.ananananzhuo.appstartupdemo.NoodleInitializer"
                android:value="androidx.startup" />
        </provider>
```

## 使用 AppStartup 进行 sdk 开发（分支：sdk_develop）

本例介绍 sdk 开发中 AppStartup 的使用，实际上与应用开发是一样的，但是感觉还是有必要说一下。

在本例中我们新建了一个 library 的 module，在 library 里面编写了我们的 AppStartup 的代码逻辑，然后将 Library 打包成 arr，集成到 app 模块中，在 app 的 Manifest 中注册组件，并调用组件的相关方法。

1. aar 集成
   ![](https://files.mdnice.com/user/15648/b34affca-2805-4fa9-9002-ff17d3e71189.png)

2. library 中的代码

```
class LibraryInitializer:Initializer<Student> {
    override fun create(context: Context): Student {
        return Student.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
       return mutableListOf()
    }
}
```

```
class Student(val name: String) {
    companion object {
        private val student = Student("安安安安卓")
        fun getInstance(): Student {
            return student
        }
    }

    fun study() {
        Log.e("tag", "${name}  好好学习")
    }
}
```

3. Manifest 中注册组件

```
 <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="com.ananananzhuo.appstartupdemo.androidx-startup"
            android:exported="false"
          >
            <meta-data
                android:name="com.ananananzhuo.library.LibraryInitializer"
                android:value="androidx.startup" />
        </provider>
```

4. 日志打印

![](https://files.mdnice.com/user/15648/09d0fd9e-e2fb-4650-8a52-e53b0dbfa2ca.png)

5. 结论

通过这种方式，第三方 sdk 只需要定义自己的 AppStartup 组件就可以，我们在注册组件的时候在 manifest 中添加第三方组件的信息就可以完成第三方组件的初始化了。

这极大的避免了某些自以为是的 sdk，打着方便我们集成的名义搞 ContentProvider 初始化恶心我们

以后如果你合作的第三方 sdk 提供方再出现 ContentProvider 的初始化方式恶心你，那么拿出我的文章好好教他做人。

最后一个字：**_爽爽爽爽爽爽爽_**
