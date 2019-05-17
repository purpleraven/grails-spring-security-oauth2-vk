Spring Security OAuth2 VK Plugin
======================================
[ ![Download](https://api.bintray.com/packages/purpleraven/plugins/spring-security-oauth2-vk/images/download.svg) ](https://bintray.com/purpleraven/plugins/spring-security-oauth2-vk/_latestVersion)

Add a VK OAuth2 provider to the [Spring Security OAuth2 Plugin](https://github.com/MatrixCrawler/grails-spring-security-oauth2).

Installation
------------
Add the following dependencies in `build.gradle`
```
repositories {
  ...
  maven { url "http://dl.bintray.com/purpleraven/plugins" }
  ...
}

dependencies {
...
    compile 'org.grails.plugins:spring-security-oauth2:1.1+'
    compile 'org.grails.plugins:spring-security-oauth2-vk:1.0'
...
}
```

Usage
-----
Add this to your application.yml
```
grails:
    plugin:
        springsecurity:
            oauth2:
                providers:
                    vk:
                        api_key: 'vk-api-key'               #needed
                        api_secret: 'vk-api-secret'         #needed
                        successUri: "/oauth2/vk/success"    #optional
                        failureUri: "/oauth2/vk/failure"    #optional
                        callback: "/oauth2/vk/callback"     #optional
```
You can replace the URIs with your own controller implementation.

In your view you can use the taglib exposed from this plugin and from OAuth plugin to create links and to know if the user is authenticated with a given provider:
```xml
<oauth2:connect provider="vk" id="vk-connect-link">VK</oauth2:connect>

Logged with VK?
<oauth2:ifLoggedInWith provider="vk">yes</oauth2:ifLoggedInWith>
<oauth2:ifNotLoggedInWith provider="vk">no</oauth2:ifNotLoggedInWith>
```
License
-------
Apache 2
