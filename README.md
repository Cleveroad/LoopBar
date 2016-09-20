# LoopBar [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>
![Header image](/images/header.png)

## Meet LoopBar - Tab Bar with Infinite Scrolling for Android by Cleveroad

At Cleveroad we’ve recently come to realize that navigation through categories in an app using nothing but a navigation panel is pretty boring and trivial. That’s why, armed with our designer’s creativity, we introduce you our new component for Android-based applications -- LoopBar. The idea was to make the navigation menu right at fingerprints, in a tab bar. What's more the view has a few specific features that make it stand out from the crowd of similar ones. So, try out the LoopBar library in your app and you’ll see the difference.

![Demo image](/images/demo.gif)

If you strive to create an application with unusual looks and navigation, you are welcome to use the LoopBar library. It’s really easy to integrate and can add spice to any app!

If you need more details on how you can use the component and what benefits it provides, read our blog post: <strong><a href="https://www.cleveroad.com/blog/case-study-loop-bar-for-android">Case Study: Loop Bar for Android</a></strong>

[![Article image](/images/article.png)](https://www.cleveroad.com/blog/case-study-loop-bar-for-android)
<br/><br/>
[![Awesome](/images/logo-footer.png)](https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts)
<br/>
## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    compile "com.cleveroad:loopbar:1.0.0"
}
```

or just download zip and import module "LoopBar-widget" to be able to modify the sources

### Features
View consist from two parts:
 - An infinite list of your selectable groups
 - Selected view
 
A list of groups will be infinite if all adapter items didn't fit on screen in other case it will be a static list.
Selected view by request could overlay layout on screen on which it placed. 
Widget has horizontal and vertical layouts and also start or end gravity of selected view. 
<p>You are allowed to use any RecyclerView adapter, which you want. Concrete infinite scroll logic is fully incapsulated</p>

Android Studio layouts preview is supported.

### Usage
```XML
    <com.cleveroad.loopbar.widget.LoopBarView
        android:id="@+id/endlessView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:enls_placeholderId="@id/placeHolder"
        app:enls_orientation="horizontal"
        app:enls_selectionGravity="start"
        app:enls_selectionMargin="10dp"
        app:enls_overlaySize="5dp"
        app:enls_selectionInAnimation="@animator/enls_scale_restore"
        app:enls_selectionOutAnimation="@animator/enls_scale_small"
        app:enls_selectionBackground="@android:color/holo_blue_dark"
        app:enls_menu="@menu/loopbar"
        />
```

```enls_overlaySize``` & ```enls_placeholderId``` are used for overlay feature.

|  attribute name | description |
|---|---|
| enls_overlaySize  | a size of selected view overlaying |
| enls_placeholderId | an id of view on which you should use layout:above or other attributes of RelativeLayouts,  because LoopBarView will have increased height in this case. See more in sample |
| enls_selectionGravity | a gravity of selection view. Can be vertical or horizontal. Default horizontal |
| enls_selectionMargin | a margin of selectionView from bounds of view. Default 5dp |
| enls_selectionInAnimation | an animation of appearing an icon inside selection view |
| enls_selectionOutAnimation | an animation of hiding an icon inside selection view |
| enls_selectionBackground | selection background. Default ```#ff0099cc``` |
| enls_menu | an id of menu which will be used for getting title and icon items  |
| android:background | View have yellow background by default. Use standart ```android:background``` attribute to change it. Default ```#ffc829``` |


To initialize items in widget and work with it you should setup adapter to it and add item click listener:
```
LoopBarView loopBarView = findViewById(..);
categoriesAdapter = new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItems(getContext()));
loopBarView.setCategoriesAdapter(categoriesAdapter);
loopBarView.addOnItemClickListener(this);
```
Here SimpleCategoriesAdapter is used which required collection of ICategoryItem objects (to draw default view with icon and text).
<br /> Also you can setup adapter through:
*   **Menu** via Java code:
``` 
        loopBarView.setCategoriesAdapterFromMenu(R.menu.loopbar);
        //or
        Menu menu = ...;
        loopBarView.setCategoriesAdapterFromMenu(menu);
```
    or via XML:
```
        <com.cleveroad.loopbar.widget.LoopBarView
        ...
        app:enls_menu="@menu/loopbar"
        />
```
* **PagerAdapter**. In this case your adapter must implement [ILoopBarPagerAdapter] interface:
```
    loopBarView.setupWithViewPager(viewPager);
```
Or you can implement your own adapter with custom items. 

To control wrapped ```RecyclerView``` animations you are able to use ```getWrappedRecyclerView()```.

<br />
#### Support ####
* * *
If you have any other questions regarding the use of this library, please contact us for support at info@cleveroad.com (email subject: "LoopBar. Support request.")
Also pull requests are welcome.

<br />
#### License ####
* * *
    The MIT License (MIT)
    
    Copyright (c) 2016 Cleveroad Inc.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
    
    
[ILoopBarPagerAdapter]: /LoopBar-widget/src/main/java/com/cleveroad/loopbar/adapter/ILoopBarPagerAdapter.java
